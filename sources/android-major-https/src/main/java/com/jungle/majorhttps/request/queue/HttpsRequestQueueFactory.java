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

package com.jungle.majorhttps.request.queue;

import android.content.Context;
import android.support.annotation.RawRes;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.jungle.majorhttps.network.HttpsUtils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HttpsRequestQueueFactory implements RequestQueueFactory {

    public static HttpsRequestQueueFactory create(
            Context context, String certAssetName, String... domains) {

        HttpsRequestQueueFactory factory = new HttpsRequestQueueFactory(context, certAssetName);
        factory.setHostnameVerifier(new HttpsUtils.DomainHostnameVerifier(domains));
        return factory;
    }

    public static HttpsRequestQueueFactory create(
            Context context, @RawRes int certRawResId, String... domains) {

        HttpsRequestQueueFactory factory = new HttpsRequestQueueFactory(context, certRawResId);
        factory.setHostnameVerifier(new HttpsUtils.DomainHostnameVerifier(domains));
        return factory;
    }


    private Context mContext;
    private HostnameVerifier mHostnameVerifier;
    private List<Certificate> mCertificateList = new ArrayList<>();


    public HttpsRequestQueueFactory(Context context) {
        mContext = context;
    }

    public HttpsRequestQueueFactory(Context context, @RawRes int... certRawResIds) {
        mContext = context;
        for (int certRawResId : certRawResIds) {
            Certificate cert = HttpsUtils.createCertificateByRawResource(mContext, certRawResId);
            if (cert != null) {
                mCertificateList.add(cert);
            }
        }
    }

    public HttpsRequestQueueFactory(Context context, String... certAssetNames) {
        mContext = context;
        for (String certAssetName : certAssetNames) {
            Certificate cert = HttpsUtils.createCertificateByCrtAsset(mContext, certAssetName);
            if (cert != null) {
                mCertificateList.add(cert);
            }
        }
    }

    public HttpsRequestQueueFactory(Context context, Certificate... certs) {
        mContext = context;
        Collections.addAll(mCertificateList, certs);
    }

    public HttpsRequestQueueFactory(Context context, List<Certificate> list) {
        mContext = context;
        mCertificateList.addAll(list);
    }

    public void setHostnameVerifier(HostnameVerifier hostnameVerifier) {
        mHostnameVerifier = hostnameVerifier;
    }

    @Override
    public RequestQueue createRequestQueue() {
        if (mHostnameVerifier == null) {
            mHostnameVerifier = new HttpsUtils.DefaultHostnameVerifier();
        }

        VolleyHttpsStack stack = null;
        if (!mCertificateList.isEmpty()) {
            Certificate[] certs = mCertificateList.toArray(new Certificate[mCertificateList.size()]);
            SSLContext sslContext = HttpsUtils.getSSLContext(
                    HttpsUtils.createTrustManagerByCerts(certs));

            if (sslContext != null) {
                SSLSocketFactory factory = sslContext.getSocketFactory();
                stack = new VolleyHttpsStack(null, factory, mHostnameVerifier);
            }
        }

        return Volley.newRequestQueue(mContext, stack);
    }
}
