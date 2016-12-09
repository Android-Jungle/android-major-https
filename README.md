# major-http

**major-http** 是一款 Android 上的 Http/Https 组件。内部使用 [Volley](https://android.googlesource.com/platform/frameworks/volley/) 作为底层 Http 组件。主要特性有：

- 支持 GET、 POST、 DELETE、 PUT、 HEAD 等；
- 支持 Http、 **Https**；
- 对 Https 支持**自定义证书验证**、**域名验证**等；
- 支持 Text、 Binary、 Json、 文件下载、 文件上传等；
- 支持 Json 自动解析为 POJO；
- 接口简洁易用。

### 1、Gradle 配置

```
compile 'com.jungle.majorhttp:major-http:1.0.0'
```

### 2、使用方法

> 使用预定义的各种 Model 来加载 URL，通过各种方法来设置加载参数。最后使用 **`load`** 或 **`loadWithProgress`** 来加载请求。
>
> **load** 只是在后台加载，界面上没有任何表现。
> **loadWithProgress** 在加载的时候会弹出一个加载展示 Dialog（该对话框样式可自定义），并在后台加载。请求返回后，将自动关闭 Dialog。

<br>

```java
private static final String DEMO_WEB_URL =
        "https://www.zhihu.com";

private static final String DEMO_JSON_URL =
        "https://raw.githubusercontent.com/arnozhang/major-http/master/docs/demo.json";

private static final String DEMO_UPLOAD_URL =
        "https://raw.githubusercontent.com/upload_test";
```

- 文本请求，使用 **`TextRequestModel`**：

```java
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
```

- Json 请求，使用 **`JsonRequestModel`**：

```java
public class GithubUserInfo {

    public static class Project {
        public String name;
        public String url;
    }

    public String uid;
    public String userName;
    public String site;
    public String[] languages;
    public List<Project> projects;
}

JsonRequestModel
        .newModel(GithubUserInfo.class)
        .url(DEMO_JSON_URL)
        .method(ModelMethod.GET)
        .load(new BizModelListener<GithubUserInfo>() {
            @Override
            public void onSuccess(Map<String, String> headers, GithubUserInfo response) {
                String info = JSON.toJSONString(response, SerializerFeature.PrettyFormat);
                info = "Load Json object success!\n\n" + info;
                TextViewerActivity.start(getContext(), info);
            }

            @Override
            public void onError(int errorCode, String message) {
                showToast(errorCode, message);
            }
        });
```

> Json 解析为 POJO 的过程，使用 [fastjson](https://github.com/alibaba/fastjson)。

- Binary / DownloadRequestModel 二进制数据请求，使用 **`DownloadRequestModel`**：

```java
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
```

- 文件下载请求，使用 **`DownloadFileRequestModel`**：

```java
final String file = getDemoFilePath();
String loadingText = String.format("Downloading File: \n%s", file);

DownloadFileRequestModel
        .newModel()
        .url(DEMO_JSON_URL)
        .filePath(file)
        .lifeListener(new ModelLoadLifeListener<DownloadFileRequestModel>() {
            @Override
            public void onBeforeLoad(DownloadFileRequestModel model) {
                ActivityCompat.requestPermissions(context,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            }
        })
        .loadWithProgress(getContext(), loadingText, new BizModelListener<String>() {
            @Override
            public void onSuccess(Map<String, String> headers, String response) {
                Toast.makeText(getContext(), String.format(
                        "Download file SUCCESS! file = %s.", file), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int errorCode, String message) {
                showToast(errorCode, message);
            }
        });
```

- 文件上传请求，使用 **`UploadRequestModel`**：

```java
final String file = getDemoFilePath();

UploadRequestModel
        .newModel()
        .url(DEMO_UPLOAD_URL)
        .addFormItem(file)
        .loadWithProgress(this, "Uploading...", new BizModelListener<String>() {
            @Override
            public void onSuccess(Map<String, String> headers, String response) {
                Toast.makeText(getContext(), String.format(
                        "Upload file SUCCESS! file = %s.", file), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int errorCode, String message) {
                showToast(errorCode, message);
            }
        });
```

### 3、Http / Https 支持

通过 **`MajorHttpManager`** 这个组件可以配置 Http / Https：

|方法|解释|
|---|---|
|setRequestQueueFactory|设置请求队列 Factory|
|setDefaultTimeoutMilliseconds|设置基础请求的超时时间|
|setUploadTimeoutMilliseconds|设置上传类请求的超时时间|
|setExtraHeadersFiller|为每个请求设置额外的 Header 填充回调（具体项目中比如动态填入 uid、ticket 等等）|

#### 3.1、Http 支持

```java
MajorHttpManager.getInstance().setRequestQueueFactory(new HttpRequestQueueFactory(context));
```

#### 3.2、Https 支持

```java
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
factory.setHostnameVerifier(new HttpsUtils.DomainHostNameVerifier(domains));

MajorHttpManager.getInstance().setRequestQueueFactory(factory);
```

> Https 可以将证书文件放在 **assets** 文件夹下，或者放在 **res/raw** 文件夹下，然后在代码中创建相应的 **`HttpsRequestQueueFactory`**。通过 **`HttpsRequestQueueFactory.setHostnameVerifier`** 可以设置域名验证。

#### 3.3、Https 验证失败异常

如果请求的 URL **证书验证**不通过，则错误如下：

```
com.android.volley.NoConnectionError:
    javax.net.ssl.SSLHandshakeException:
        java.security.cert.CertPathValidatorException:
            Trust anchor for certification path not found..
```

如果请求的 URL **域名验证**不通过，则错误如下：

```
com.android.volley.NoConnectionError:
    javax.net.ssl.SSLPeerUnverifiedException: Hostname github.com not verified:
            certificate: sha1/1O6dKmcSs2FMJy0ViwT8yMoIoLY=
            DN: CN=github.com,O=GitHub\, Inc.,L=San Francisco,ST=California,C=US,2.5.4.17=#13053934313037,STREET=88 Colin P Kelly\, Jr Street,2.5.4.5=#130735313537353530,1.3.6.1.4.1.311.60.2.1.2=#130844656c6177617265,1.3.6.1.4.1.311.60.2.1.3=#13025553,2.5.4.15=#0c1450726976617465204f7267616e697a6174696f6e
            subjectAltNames: [github.com, www.github.com].
```

<br>

## License

```
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
```
