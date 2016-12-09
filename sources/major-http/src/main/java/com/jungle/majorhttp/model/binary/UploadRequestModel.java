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

import com.jungle.majorhttp.model.base.AbstractBizModel;
import com.jungle.majorhttp.model.base.BaseBizModel;
import com.jungle.majorhttp.request.MajorHttpManager;
import com.jungle.majorhttp.request.upload.BinaryUploadFormItem;
import com.jungle.majorhttp.request.upload.FileUploadFormItem;
import com.jungle.majorhttp.request.upload.UploadFormItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UploadRequestModel extends BaseBizModel<UploadRequestModel, String> {

    public static UploadRequestModel newModel() {
        return new UploadRequestModel();
    }


    public static class Request extends AbstractBizModel.Request {

        private List<UploadFormItem> mFormItems = new ArrayList<>();

        public Request addFormItem(UploadFormItem item) {
            mFormItems.add(item);
            return this;
        }

        public List<UploadFormItem> getFormItems() {
            return mFormItems;
        }
    }


    public UploadRequestModel addFormItem(UploadFormItem item) {
        ((Request) mRequest).addFormItem(item);
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

        return addFormItem(new BinaryUploadFormItem(fileName, content));
    }

    @Override
    protected AbstractBizModel.Request createRequest() {
        return new Request();
    }

    @Override
    public int loadInternal() {
        return MajorHttpManager.getInstance().loadUploadModel((Request) mRequest, this);
    }
}
