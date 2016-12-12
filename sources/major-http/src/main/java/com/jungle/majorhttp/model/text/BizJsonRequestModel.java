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

package com.jungle.majorhttp.model.text;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jungle.majorhttp.model.base.ModelMethod;
import com.jungle.majorhttp.network.CommonError;
import com.jungle.majorhttp.request.base.NetworkResp;

/**
 * Using json data as follows:
 *
 * {
 *     ret: 0,              // 0 - success
 *     msg: "success",      // error messages.
 *     data: {
 *         // ...           // this object's type is T.
 *     }
 * }
 *
 */
public class BizJsonRequestModel<T> extends JsonRequestModel<T> {

    public static <T> BizJsonRequestModel<T> newModel(Class<T> clazz) {
        return new BizJsonRequestModel<T>(clazz);
    }


    public BizJsonRequestModel(Class<T> clazz) {
        super(clazz);
        method(ModelMethod.POST);
    }

    @Override
    public void onSuccess(int seqId, NetworkResp networkResp, String response) {
        if (TextUtils.isEmpty(response)) {
            doSuccess(networkResp, null);
            return;
        }

        JSONObject json = null;
        try {
            json = JSON.parseObject(response);
            int retCode = json.getIntValue("ret");
            if (retCode != CommonError.SUCCESS) {
                String message = json.getString("msg");
                doError(CommonError.FAILED, message);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            doError(CommonError.PARSE_BODY_ERROR, e.getMessage());
            return;
        }

        try {
            T data = json.getObject("data", mResponseDataClazz);
            doSuccess(networkResp, data);
        } catch (Exception e) {
            e.printStackTrace();
            doError(CommonError.PARSE_BODY_ERROR, e.getMessage());
        }
    }
}
