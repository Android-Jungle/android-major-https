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

package com.jungle.majorhttp.request.text;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.jungle.majorhttp.request.base.BizBaseRequest;
import com.jungle.majorhttp.request.base.BizRequestListener;

import java.util.Map;

public class BizTextRequest extends BizBaseRequest<BizTextResponse> {

    private static final String PROTOCOL_CHARSET = "utf-8";
    private static final String PROTOCOL_CONTENT_TYPE =
            String.format("application/json; charset=%s", PROTOCOL_CHARSET);


    private byte[] mRequestBody;


    public BizTextRequest(
            int seqId, int method, String url,
            Map<String, Object> params, Map<String, String> headers, byte[] requestBody,
            BizRequestListener<BizTextResponse> listener) {

        super(seqId, method, url, params, headers, listener);
        mRequestBody = requestBody;
    }

    @Override
    public byte[] getPostBody() {
        return getBody();
    }

    @Override
    public String getBodyContentType() {
        return PROTOCOL_CONTENT_TYPE;
    }

    @Override
    public byte[] getBody() {
        return mRequestBody;
    }

    @Override
    protected Response<BizTextResponse> parseNetworkResponse(NetworkResponse response) {
        return Response.success(
                new BizTextResponse(response, getResponseContent(response)),
                HttpHeaderParser.parseCacheHeaders(response));
    }
}
