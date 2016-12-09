/**
 * Android Jungle-Major-Http framework project.
 *
 * Copyright 2016 Arno Zhang <zyfgood12@163.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jungle.majorhttp.request;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public abstract class BizBaseRequest<T extends BizBaseResponse> extends Request<T> {

    protected int mSeqId;
    protected Map<String, ?> mRequestParams;
    protected Map<String, String> mRequestHeaders;
    protected ExtraHeadersFiller mExtraHeadersFiller;
    protected BizRequestListener<T> mListener;


    public BizBaseRequest(
            int seqId, int method, String url,
            Map<String, ?> params, Map<String, String> headers,
            BizRequestListener<T> listener) {

        super(method, url, null);

        mSeqId = seqId;
        mListener = listener;
        mRequestParams = params;
        mRequestHeaders = headers;
    }

    public void setExtraHeadersFiller(ExtraHeadersFiller filler) {
        mExtraHeadersFiller = filler;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();
        headers.putAll(mRequestHeaders);

        if (mExtraHeadersFiller != null) {
            mExtraHeadersFiller.fillHeaders(headers);
        }

        return super.getHeaders();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return (Map<String, String>) mRequestParams;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void deliverResponse(T response) {
        if (mListener != null) {
            mListener.onSuccess(mSeqId, response);
        }
    }

    @Override
    public void deliverError(VolleyError error) {
        if (mListener != null) {
            mListener.onError(mSeqId, error);
        }
    }

    protected String getResponseContent(NetworkResponse response) {
        String content;
        try {
            content = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            content = new String(response.data);
        }

        return content;
    }
}
