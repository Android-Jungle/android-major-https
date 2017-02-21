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

package com.jungle.majorhttps.model.listener;

import com.jungle.majorhttps.request.base.NetworkResp;

public class BothProxyModelListener<T> implements ModelListener<T> {

    private ModelSuccessListener<T> mSuccessListener;
    private ModelErrorListener mErrorListener;


    public BothProxyModelListener(ModelListener<T> listener) {
        mSuccessListener = listener;
        mErrorListener = listener;
    }

    public BothProxyModelListener(ModelSuccessListener<T> success, ModelErrorListener error) {
        mSuccessListener = success;
        mErrorListener = error;
    }

    @Override
    public void onSuccess(NetworkResp networkResp, T response) {
        if (mSuccessListener != null) {
            mSuccessListener.onSuccess(networkResp, response);
        }
    }

    @Override
    public void onError(int errorCode, String message) {
        if (mErrorListener != null) {
            mErrorListener.onError(errorCode, message);
        }
    }
}
