package com.jungle.majorhttp.request;

import com.android.volley.NetworkResponse;

import java.util.Map;

public class NetworkResp {

    public int mStatusCode;
    public Map<String, String> mHeaders;
    public boolean mNotModified;
    public long mNetworkTimeMs;


    public NetworkResp(NetworkResponse response) {
        mStatusCode = response.statusCode;
        mHeaders = response.headers;
        mNotModified = response.notModified;
        mNetworkTimeMs = response.networkTimeMs;
    }
}
