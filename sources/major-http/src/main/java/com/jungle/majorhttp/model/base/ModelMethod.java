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

package com.jungle.majorhttp.model.base;

import com.android.volley.Request;

public enum ModelMethod {

    GET,
    DELETE,
    POST,
    PUT,
    HEAD,
    OPTIONS,
    TRACE,
    PATCH;


    public int toVolleyMethod() {
        if (this == GET) {
            return Request.Method.GET;
        } else if (this == POST) {
            return Request.Method.POST;
        } else if (this == DELETE) {
            return Request.Method.DELETE;
        } else if (this == PUT) {
            return Request.Method.PUT;
        } else if (this == HEAD) {
            return Request.Method.HEAD;
        } else if (this == OPTIONS) {
            return Request.Method.OPTIONS;
        } else if (this == TRACE) {
            return Request.Method.TRACE;
        } else if (this == PATCH) {
            return Request.Method.PATCH;
        }

        return Request.Method.GET;
    }
}
