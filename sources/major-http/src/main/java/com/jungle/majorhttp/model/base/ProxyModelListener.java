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

import com.jungle.majorhttp.model.base.BizModelListener;
import com.jungle.majorhttp.request.base.NetworkResp;

public class ProxyModelListener<T> implements BizModelListener<T> {

    private BizModelListener<T> mImplListener;


    public ProxyModelListener(BizModelListener<T> listener) {
        mImplListener = listener;
    }

    @Override
    public void onSuccess(NetworkResp networkResp, T response) {
        if (mImplListener != null) {
            mImplListener.onSuccess(networkResp, response);
        }
    }

    @Override
    public void onError(int errorCode, String message) {
        if (mImplListener != null) {
            mImplListener.onError(errorCode, message);
        }
    }
}