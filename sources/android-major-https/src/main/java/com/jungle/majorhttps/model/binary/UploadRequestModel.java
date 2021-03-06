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

package com.jungle.majorhttps.model.binary;

import com.jungle.majorhttps.model.base.AbstractModel;
import com.jungle.majorhttps.model.base.BaseModel;
import com.jungle.majorhttps.model.base.ModelMethod;
import com.jungle.majorhttps.request.upload.BinaryMultipartFormItem;
import com.jungle.majorhttps.request.upload.FileUploadFormItem;
import com.jungle.majorhttps.request.upload.MultipartFormItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UploadRequestModel
        extends BaseModel<UploadRequestModel, UploadRequestModel.Request, String> {

    public static UploadRequestModel newModel() {
        return new UploadRequestModel();
    }


    public static class Request extends AbstractModel.Request {

        private List<MultipartFormItem> mFormItems = new ArrayList<>();

        public Request addFormItem(MultipartFormItem item) {
            mFormItems.add(item);
            return this;
        }

        public List<MultipartFormItem> getFormItems() {
            return mFormItems;
        }
    }


    public UploadRequestModel() {
        super();
        method(ModelMethod.POST);
    }

    public UploadRequestModel addFormItem(MultipartFormItem item) {
        mRequest.addFormItem(item);
        return this;
    }

    public UploadRequestModel addFormItem(String filePath) {
        if (!new File(filePath).exists()) {
            return this;
        }

        return addFormItem(new FileUploadFormItem(filePath));
    }

    public UploadRequestModel addFormItem(String fileName, byte[] content) {
        if (content == null || content.length <= 0) {
            return this;
        }

        return addFormItem(new BinaryMultipartFormItem(fileName, content));
    }

    @Override
    protected Request createRequest() {
        return new Request();
    }

    @Override
    public int loadInternal() {
        return getHttpClient().loadUploadModel(mRequest, this);
    }
}
