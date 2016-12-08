/**
 * Android Jungle-Easy-Http demo framework project.
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

package com.jungle.easyhttp.demo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.jungle.easyhttp.model.base.BizModelListener;
import com.jungle.easyhttp.model.base.ModelMethod;
import com.jungle.easyhttp.model.binary.DownloadRequestModel;
import com.jungle.easyhttp.model.text.JsonRequestModel;
import com.jungle.easyhttp.model.text.TextRequestModel;
import com.jungle.easyhttp.request.EasyHttpManager;
import com.jungle.easyhttp.request.queue.HttpRequestQueueFactory;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String DEMO_WEB_URL =
            "https://github.com/arnozhang/easy-http/blob/master/.gitignore";
    private static final String DEMO_JSON_URL =
            "https://raw.githubusercontent.com/arnozhang/easy-http/master/docs/demo.json";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        EasyHttpManager.getInstance().setRequestQueueFactory(new HttpRequestQueueFactory(this));
    }

    private void showToast(int errorCode, String message) {
        Log.e("Main", String.format("Error! errorCode = %d.", errorCode));

        Toast.makeText(this,
                String.format("Error: errorCode = %d, message = %s.", errorCode, message),
                Toast.LENGTH_SHORT).show();
    }

    private Context getContext() {
        return this;
    }

    public void onTextModel(View view) {
        TextRequestModel
                .newModel()
                .url(DEMO_WEB_URL)
                .method(ModelMethod.GET)
                .loadWithProgress(this, new BizModelListener<String>() {
                    @Override
                    public void onSuccess(Map<String, String> headers, String response) {
                        TextViewerActivity.start(getContext(), response);
                    }

                    @Override
                    public void onError(int errorCode, String message) {
                        showToast(errorCode, message);
                    }
                });
    }

    public void onJsonModel(View view) {
        JsonRequestModel
                .newModel(GithubUserInfo.class)
                .url(DEMO_JSON_URL)
                .method(ModelMethod.GET)
                .loadWithProgress(this, new BizModelListener<GithubUserInfo>() {
                    @Override
                    public void onSuccess(Map<String, String> headers, GithubUserInfo response) {
                    }

                    @Override
                    public void onError(int errorCode, String message) {
                        showToast(errorCode, message);
                    }
                });
    }

    public void onDownloadModel(View view) {
        DownloadRequestModel
                .newModel()
                .url(DEMO_JSON_URL)
                .method(ModelMethod.GET)
                .loadWithProgress(this, new BizModelListener<byte[]>() {
                    @Override
                    public void onSuccess(Map<String, String> headers, byte[] response) {
                        String content = new String(response);
                        TextViewerActivity.start(getContext(), content);
                    }

                    @Override
                    public void onError(int errorCode, String message) {
                        showToast(errorCode, message);
                    }
                });
    }

    public void onDownloadFileModel(View view) {
    }
}
