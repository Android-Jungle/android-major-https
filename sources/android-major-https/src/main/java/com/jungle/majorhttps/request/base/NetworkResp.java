/**
 * Android Jungle-Major-Https framework project.
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

package com.jungle.majorhttps.request.base;

import com.android.volley.NetworkResponse;

import java.util.Map;

public class NetworkResp {

    public int mStatusCode;
    public Map<String, String> mHeaders;
    public boolean mNotModified;
    public long mNetworkTimeMs;


    public NetworkResp(NetworkResponse response) {
        mStatusCode = response.statusCode;
        mHeaders = response.headers;
        mNotModified = response.notModified;
        mNetworkTimeMs = response.networkTimeMs;
    }

    public String getCookie() {
        return mHeaders != null ? mHeaders.get("Set-Cookie") : null;
    }
}
