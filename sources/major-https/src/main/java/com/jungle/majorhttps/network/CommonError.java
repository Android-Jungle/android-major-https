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

package com.jungle.majorhttps.network;

import com.android.volley.VolleyError;

public class CommonError {

    public static final int SUCCESS = 0;
    public static final int FAILED = -4000;
    public static final int PARSE_BODY_ERROR = -40001;
    public static final int REQUEST_QUEUE_NOT_INITIALIZED = -4002;
    public static final int PARSE_JSON_OBJECT_FAILED = -4003;
    public static final int PARSE_JSON_ARRAY_FAILED = -4004;


    public static int fromError(VolleyError error) {
        if (error != null && error.networkResponse != null) {
            return error.networkResponse.statusCode;
        }

        return FAILED;
    }
}
