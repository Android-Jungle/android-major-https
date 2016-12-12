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

package com.jungle.majorhttp.model.binary;

import com.jungle.majorhttp.model.base.AbstractModel;
import com.jungle.majorhttp.model.base.BaseModel;

public class DownloadFileRequestModel
        extends BaseModel<DownloadFileRequestModel, DownloadFileRequestModel.Request, String> {

    public static DownloadFileRequestModel newModel() {
        return new DownloadFileRequestModel();
    }


    public static class Request extends AbstractModel.Request {

        private String mFilePath;

        public Request filePath(String filePath) {
            mFilePath = filePath;
            return this;
        }

        public String getFilePath() {
            return mFilePath;
        }
    }

    public DownloadFileRequestModel filePath(String filePath) {
        mRequest.filePath(filePath);
        return this;
    }

    @Override
    protected Request createRequest() {
        return new Request();
    }

    @Override
    public int loadInternal() {
        return getHttpClient().loadDownloadFileModel(mRequest, this);
    }
}
