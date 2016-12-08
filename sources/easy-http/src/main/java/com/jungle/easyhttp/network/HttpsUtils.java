/**
 * Android Jungle-Share framework project.
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

package com.jungle.easyhttp.network;

import android.content.Context;
import android.support.annotation.RawRes;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class HttpsUtils {

    public static class DomainHostNameVerifier implements HostnameVerifier {

        /**
         * Verifier domain name, such as `biz.main.com`.
         */
        private String[] mVerifyDomain;

        public DomainHostNameVerifier(String... domain) {
            mVerifyDomain = domain;
        }

        @Override
        public boolean verify(String s, SSLSession sslSession) {
            HostnameVerifier verifier = HttpsURLConnection.getDefaultHostnameVerifier();
            for (String domain : mVerifyDomain) {
                if (verifier.verify(domain, sslSession)) {
                    return true;
                }
            }

            return false;
        }
    }

    public static class DefaultHostNameVerifier implements HostnameVerifier {

        @Override
        public boolean verify(String s, SSLSession sslSession) {
            return true;
        }
    }


    /**
     * @param crtText Certificate file content.
     *                Can use `keytool -printcert -rfc -file uwca.crt` command to print it.
     */
    public static X509Certificate createCertificateByCrtText(String crtText) {
        return createCertificateByStream(new ByteArrayInputStream(crtText.getBytes()));
    }

    public static X509Certificate createCertificateByCrtFile(String fileName) {
        InputStream stream = null;
        try {
            stream = new FileInputStream(fileName);
            return createCertificateByStream(stream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static X509Certificate createCertificateByCrtAsset(Context context, String fileName) {
        try {
            return createCertificateByStream(context.getAssets().open(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static X509Certificate createCertificateByRawResource(Context context, @RawRes int resId) {
        return createCertificateByStream(context.getResources().openRawResource(resId));
    }

    public static X509Certificate createCertificateByStream(InputStream stream) {
        Certificate certificate = createCertificateByStream(stream, "X.509");
        return certificate != null ? (X509Certificate) certificate : null;
    }

    public static Certificate createCertificateByStream(InputStream stream, String type) {
        try {
            CertificateFactory factory = CertificateFactory.getInstance(type);
            return factory.generateCertificate(new BufferedInputStream(stream));
        } catch (CertificateException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static TrustManager[] createTrustManagerByCert(Certificate cert) {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", cert);

            TrustManagerFactory factory = TrustManagerFactory.getInstance(
                    TrustManagerFactory.getDefaultAlgorithm());
            factory.init(keyStore);
            return factory.getTrustManagers();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static SSLContext getSslContext(TrustManager... trustManagers) {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagers, null);
            return sslContext;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        return null;
    }
}
