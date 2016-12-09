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

import android.text.TextUtils;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.jungle.majorhttp.request.BizBaseRequest;
import com.jungle.majorhttp.request.BizRequestListener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public class BizTextRequest extends BizBaseRequest<BizTextResponse> {

    private static final String PROTOCOL_CHARSET = "utf-8";
    private static final String PROTOCOL_CONTENT_TYPE =
            String.format("application/json; charset=%s", PROTOCOL_CHARSET);


    private String mRedirectUrl;
    private byte[] mRequestBody;


    public BizTextRequest(
            int seqId, int method, String url,
            Map<String, Object> params, Map<String, String> headers, byte[] requestBody,
            BizRequestListener<BizTextResponse> listener) {

        super(seqId, method, url, params, headers, listener);
        mRequestBody = requestBody;

        redirectRequest();
    }

    private void redirectRequest() {
        if (getMethod() != Method.GET) {
            return;
        }

        String url = getOriginalUrl();
        if (TextUtils.isEmpty(url)) {
            return;
        }

        String encodeParams = encodeParameters(getParamsEncoding());
        if (!url.contains("?")) {
            url += "?" + encodeParams;
        } else {
            url += "&" + encodeParams;
        }

        mRedirectUrl = url;
    }

    @Override
    public String getUrl() {
        return mRedirectUrl != null ? mRedirectUrl : getOriginalUrl();
    }

    public String getOriginalUrl() {
        return super.getUrl();
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
                new BizTextResponse(getResponseContent(response), response.headers),
                HttpHeaderParser.parseCacheHeaders(response));
    }

    private String encodeParameters(String encoding) {
        StringBuilder builder = new StringBuilder();

        try {
            if (mRequestParams != null && mRequestParams.size() > 0) {
                for (Map.Entry<String, ?> entry : mRequestParams.entrySet()) {
                    builder.append(URLEncoder.encode(entry.getKey(), encoding));
                    builder.append('=');
                    builder.append(URLEncoder.encode(String.valueOf(entry.getValue()), encoding));
                    builder.append('&');
                }

                builder.deleteCharAt(builder.length() - 1);
            }

            return builder.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }
}
