/**
 * Android Jungle-Share framework project.
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

package com.jungle.easyhttp.model.binary;

import com.jungle.easyhttp.model.base.BaseBizModel;
import com.jungle.easyhttp.request.BizHttpManager;

public class DownloadRequestModel extends BaseBizModel<DownloadRequestModel, byte[]> {

    public static DownloadRequestModel newModel() {
        return new DownloadRequestModel();
    }


    @Override
    protected Request createRequest() {
        return new Request();
    }

    @Override
    public int loadInternal() {
        return BizHttpManager.getInstance().loadDownloadModel(mRequest, this);
    }
}
