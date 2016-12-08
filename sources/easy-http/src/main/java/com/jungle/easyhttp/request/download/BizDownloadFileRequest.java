/**
 * Android Jungle-Easy-Http framework project.
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

package com.jungle.easyhttp.request.download;

import android.text.TextUtils;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.jungle.easyhttp.request.BizBaseRequest;
import com.jungle.easyhttp.request.BizRequestListener;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class BizDownloadFileRequest extends BizBaseRequest<BizDownloadFileResponse> {

    private String mFilePath;


    public BizDownloadFileRequest(
            int seqId, String url, Map<String, ?> params, Map<String, String> headers,
            String filePath, BizRequestListener<BizDownloadFileResponse> listener) {

        super(seqId, Method.GET, url, params, headers, listener);
        mFilePath = filePath;
    }

    @Override
    protected Response<BizDownloadFileResponse> parseNetworkResponse(NetworkResponse response) {
        if (TextUtils.isEmpty(mFilePath)) {
            return Response.error(new VolleyError("Download file path must not be null!"));
        }

        File file = new File(mFilePath);
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(new VolleyError(e.getMessage()));
        }

        boolean success = false;
        BufferedOutputStream stream = null;
        try {
            stream = new BufferedOutputStream(new FileOutputStream(mFilePath));
            stream.write(response.data);
            stream.flush();
            success = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return Response.error(new VolleyError(e.getMessage()));
        } catch (IOException e) {
            e.printStackTrace();
            return Response.error(new VolleyError(e.getMessage()));
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return Response.success(
                new BizDownloadFileResponse(mFilePath, response.headers),
                HttpHeaderParser.parseCacheHeaders(response));
    }
}
