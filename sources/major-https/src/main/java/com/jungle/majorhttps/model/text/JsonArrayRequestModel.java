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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.jungle.majorhttps.network.CommonError;
import com.jungle.majorhttps.request.base.NetworkResp;

public class JsonArrayRequestModel
        extends AbstractTextRequestModel<JsonArrayRequestModel, JSONArray> {

    @Override
    public void onSuccess(int seqId, NetworkResp networkResp, String response) {
        JSONArray json;
        try {
            json = JSON.parseArray(response);
        } catch (Exception e) {
            e.printStackTrace();
            doError(CommonError.PARSE_JSON_ARRAY_FAILED, e.getMessage());
            return;
        }

        doSuccess(networkResp, json);
    }
}
