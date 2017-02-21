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

package com.jungle.majorhttps.request.upload;

public class BinaryMultipartFormItem implements MultipartFormItem {

    protected String mFormName;
    protected byte[] mFormContent;
    protected String mMimeType = OCTET_MIME_TYPE;


    public BinaryMultipartFormItem() {
    }

    public BinaryMultipartFormItem(String formName, byte[] content) {
        this(formName, null, content);
    }

    public BinaryMultipartFormItem(String formName, String mimeType, byte[] content) {
        mFormName = formName;
        mFormContent = content;
        mMimeType = mimeType != null ? mimeType : OCTET_MIME_TYPE;
    }

    @Override
    public String getFormName() {
        return mFormName;
    }

    public byte[] getFormContent() {
        return mFormContent;
    }

    @Override
    public String getMimeType() {
        return mMimeType;
    }
}