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

package com.jungle.majorhttp.request.binary;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.jungle.majorhttp.request.BizBaseRequest;
import com.jungle.majorhttp.request.BizRequestListener;

import java.util.Map;

public class BizBinaryRequest extends BizBaseRequest<BizBinaryResponse> {

    private static final String CONTENT_TYPE_BINARY = "application/octet-stream";
    private static final String CONTENT_TYPE_PROTOBUF = "application/x-protobuf";


    private byte[] mData;


    public BizBinaryRequest(
            int seqId, String url,
            Map<String, Object> params, Map<String, String> headers, byte[] data,
            BizRequestListener<BizBinaryResponse> listener) {

        super(seqId, Method.POST, url, params, headers, listener);
        mData = data;
    }

    @Override
    public String getBodyContentType() {
        return CONTENT_TYPE_BINARY;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        return mData != null ? mData : super.getBody();
    }

    @Override
    protected Response<BizBinaryResponse> parseNetworkResponse(NetworkResponse response) {
        return Response.success(
                new BizBinaryResponse(response, response.data),
                HttpHeaderParser.parseCacheHeaders(response));
    }
}
