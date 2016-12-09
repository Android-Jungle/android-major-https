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

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.jungle.easyhttp.network.CommonError;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class JsonRequestModel<T> extends AbstractTextRequestModel<JsonRequestModel<T>, T> {

    public static <T> JsonRequestModel<T> newModel(Class<T> clazz) {
        return new JsonRequestModel<T>(clazz);
    }


    protected Class<T> mResponseDataClazz;


    public JsonRequestModel(Class<T> clazz) {
        mResponseDataClazz = clazz;
    }

    @Override
    public int loadInternal() {
        Map<String, Object> params = mRequest.getRequestParams();
        if (!params.isEmpty()) {
            try {
                body(JSON.toJSONString(params).getBytes("utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return super.loadInternal();
    }

    @Override
    public void onSuccess(int seqId, Map<String, String> headers, String response) {
        if (mListener == null) {
            return;
        }

        if (TextUtils.isEmpty(response)) {
            mListener.onSuccess(headers, null);
            return;
        }

        try {
            T data = JSON.parseObject(response, mResponseDataClazz);
            mListener.onSuccess(headers, data);
        } catch (Exception e) {
            e.printStackTrace();
            mListener.onError(CommonError.PARSE_BODY_ERROR, e.getMessage());
        }
    }
}
