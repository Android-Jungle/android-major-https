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

import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileUploadFormItem extends BinaryMultipartFormItem {

    private String mFilePath;


    public FileUploadFormItem() {
    }

    public FileUploadFormItem(String filePath) {
        this(filePath, null, null);
    }

    public FileUploadFormItem(String filePath, byte[] content) {
        this(filePath, null, content);
    }

    public FileUploadFormItem(String filePath, String mimeType) {
        this(filePath, mimeType, null);
    }

    public FileUploadFormItem(String filePath, String mimeType, byte[] content) {
        super(new File(filePath).getName(), mimeType, content);
        mFilePath = filePath;
    }

    public byte[] getFormContent() {
        if (mFormContent == null) {
            mFormContent = getFileContent(mFilePath);
        }

        return mFormContent;
    }

    @Override
    public String getMimeType() {
        return mMimeType;
    }

    public static byte[] getFileContent(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }

        FileInputStream stream = null;
        try {
            stream = new FileInputStream(filePath);
            byte[] content = new byte[stream.available()];
            stream.read(content);
            return content;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }
}
