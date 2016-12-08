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

package com.jungle.easyhttp.model.text;

import com.jungle.easyhttp.model.base.AbstractBizModel;
import com.jungle.easyhttp.network.BaseRequestListener;
import com.jungle.easyhttp.request.EasyHttpManager;

public abstract class AbstractTextRequestModel<Impl extends AbstractTextRequestModel, Data>
        extends AbstractBizModel<AbstractTextRequestModel<Impl, Data>, Data>
        implements BaseRequestListener<String> {

    public static class Request extends AbstractBizModel.Request {

        private String mRequestBody;

        public Request requestBody(String body) {
            mRequestBody = body;
            return this;
        }

        public String getRequestBody() {
            return mRequestBody;
        }
    }


    @Override
    protected AbstractBizModel.Request createRequest() {
        return new Request();
    }

    @Override
    public int loadInternal() {
        return EasyHttpManager.getInstance().loadTextModel((Request) mRequest, this);
    }

    @SuppressWarnings("unchecked")
    public Impl setRequestBody(String body) {
        ((Request) mRequest).requestBody(body);
        return (Impl) this;
    }

    @Override
    public void onError(int seqId, int errorCode, String message) {
        mListener.onError(errorCode, message);
    }
}
