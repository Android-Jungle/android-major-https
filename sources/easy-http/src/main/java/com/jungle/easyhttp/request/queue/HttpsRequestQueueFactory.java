/**
 * Android Jungle-Easy-Http framework project.
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

package com.jungle.easyhttp.request.queue;

import android.content.Context;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.jungle.easyhttp.network.HttpsUtils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import java.security.cert.Certificate;

public class HttpsRequestQueueFactory implements RequestQueueFactory {

    public static HttpsRequestQueueFactory create(
            Context context, String certAssetName, String... domains) {

        HttpsRequestQueueFactory factory = new HttpsRequestQueueFactory(context, certAssetName);
        factory.setHostnameVerifier(new HttpsUtils.DomainHostNameVerifier(domains));
        return factory;
    }


    private Context mContext;
    private Certificate mCertificate;
    private HostnameVerifier mHostnameVerifier;


    public HttpsRequestQueueFactory(Context context) {
        mContext = context;
    }

    public HttpsRequestQueueFactory(Context context, int certRawResId) {
        mContext = context;
        mCertificate = HttpsUtils.createCertificateByRawResource(mContext, certRawResId);
    }

    public HttpsRequestQueueFactory(Context context, String certAssetName) {
        mContext = context;
        mCertificate = HttpsUtils.createCertificateByCrtAsset(mContext, certAssetName);
    }

    public HttpsRequestQueueFactory(Context context, Certificate certificate) {
        mContext = context;
        mCertificate = certificate;
    }

    public void setHostnameVerifier(HostnameVerifier hostnameVerifier) {
        mHostnameVerifier = hostnameVerifier;
    }

    @Override
    public RequestQueue createRequestQueue() {
        if (mHostnameVerifier == null) {
            mHostnameVerifier = new HttpsUtils.DefaultHostNameVerifier();
        }

        HttpsStack stack = null;
        SSLContext sslContext = HttpsUtils.getSslContext(
                HttpsUtils.createTrustManagerByCert(mCertificate));
        if (sslContext != null) {
            SSLSocketFactory factory = sslContext.getSocketFactory();
            stack = new HttpsStack(null, factory, mHostnameVerifier);
        }

        return Volley.newRequestQueue(mContext, stack);
    }
}
