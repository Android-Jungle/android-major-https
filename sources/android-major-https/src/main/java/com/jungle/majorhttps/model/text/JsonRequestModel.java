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

package com.jungle.majorhttps.model.text;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.jungle.majorhttps.network.CommonError;
import com.jungle.majorhttps.request.base.NetworkResp;

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
    public void onSuccess(int seqId, NetworkResp networkResp, String response) {
        if (TextUtils.isEmpty(response)) {
            doSuccess(networkResp, null);
            return;
        }

        try {
            T data = JSON.parseObject(response, mResponseDataClazz);
            doSuccess(networkResp, data);
        } catch (Exception e) {
            e.printStackTrace();
            doError(CommonError.PARSE_BODY_ERROR, e.getMessage());
        }
    }
}
