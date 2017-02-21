/**
 * Android Jungle-Major-Https demo framework project.
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

package com.jungle.majorhttps.demo;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.jungle.majorhttps.manager.MajorHttpClient;
import com.jungle.majorhttps.model.binary.DownloadFileRequestModel;
import com.jungle.majorhttps.model.binary.DownloadRequestModel;
import com.jungle.majorhttps.model.binary.UploadRequestModel;
import com.jungle.majorhttps.model.listener.ModelListener;
import com.jungle.majorhttps.model.listener.ModelSuccessListener;
import com.jungle.majorhttps.model.text.JsonRequestModel;
import com.jungle.majorhttps.model.text.TextRequestModel;
import com.jungle.majorhttps.network.HttpsUtils;
import com.jungle.majorhttps.request.base.NetworkResp;
import com.jungle.majorhttps.request.queue.HttpsRequestQueueFactory;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String DEMO_WEB_URL =
            "https://www.zhihu.com";

    private static final String DEMO_JSON_URL =
            "https://raw.githubusercontent.com/arnozhang/major-http/master/docs/demo.json";

    private static final String DEMO_UPLOAD_URL =
            "https://raw.githubusercontent.com/upload_test";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] certs = {
                "zhihu.cer",
                "github.cer",
                "githubusercontent.cer"
        };

        String[] domains = {
                "zhihu.com",
                "github.com",
                "githubusercontent.com"
        };

        HttpsRequestQueueFactory factory = new HttpsRequestQueueFactory(this, certs);
        factory.setHostnameVerifier(new HttpsUtils.DomainHostnameVerifier(domains));
        MajorHttpClient.getDefault().setRequestQueueFactory(factory);
    }

    private void showError(int errorCode, String message) {
        message = String.format(Locale.getDefault(),
                "Error: errorCode = %d, message = %s.", errorCode, message);

        Log.e("Main", message);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private Context getContext() {
        return this;
    }

    public void onTextModel(View view) {
        TextRequestModel
                .newModel()
                .url(DEMO_WEB_URL)
                .success((networkResp, response) -> TextViewerActivity.start(getContext(), response))
                .error(this::showError)
                .loadWithProgress(this);
    }

    public void onJsonModel(View view) {
        JsonRequestModel
                .newModel(GithubUserInfo.class)
                .url(DEMO_JSON_URL)
                .error(this::showError)
                .load(new ModelSuccessListener<GithubUserInfo>() {
                    @Override
                    public void onSuccess(NetworkResp networkResp, GithubUserInfo response) {
                        String info = JSON.toJSONString(response, SerializerFeature.PrettyFormat);
                        info = "Load Json object success!\n\n" + info;
                        TextViewerActivity.start(getContext(), info);
                    }
                });
    }

    public void onDownloadModel(View view) {
        DownloadRequestModel
                .newModel()
                .url(DEMO_JSON_URL)
                .loadWithProgress(this, new ModelListener<byte[]>() {
                    @Override
                    public void onSuccess(NetworkResp networkResp, byte[] response) {
                        String content = new String(response);
                        TextViewerActivity.start(getContext(), content);
                    }

                    @Override
                    public void onError(int errorCode, String message) {
                        showError(errorCode, message);
                    }
                });
    }

    private String getDemoFilePath() {
        return Environment.getExternalStorageDirectory().getPath() + "/demo.json";
    }

    public void onDownloadFileModel(View view) {
        final String file = getDemoFilePath();
        String loadingText = String.format("Downloading File: \n%s", file);

        DownloadFileRequestModel
                .newModel()
                .url(DEMO_JSON_URL)
                .filePath(file)
                .error(this::showError)
                .lifeListener(model -> {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                })
                .loadWithProgress(getContext(), loadingText, (networkResp, response) -> {
                    Toast.makeText(getContext(), String.format(
                            "Download file SUCCESS! file = %s.", file), Toast.LENGTH_SHORT).show();

                });
    }

    public void onUploadFileModel(View view) {
        final String file = getDemoFilePath();

        UploadRequestModel
                .newModel()
                .url(DEMO_UPLOAD_URL)
                .addFormItem(file)
                .error(this::showError)
                .loadWithProgress(this, "Uploading...", new ModelSuccessListener<String>() {
                    @Override
                    public void onSuccess(NetworkResp networkResp, String response) {
                        Toast.makeText(getContext(), String.format(
                                "Upload file SUCCESS! file = %s.", file), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
