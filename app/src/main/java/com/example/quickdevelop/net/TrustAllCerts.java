package com.example.quickdevelop.net;

import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

/**
 * @describe describe
 */
public class TrustAllCerts implements X509TrustManager {
    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) {}

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) {}

    @Override
    public X509Certificate[] getAcceptedIssuers() {return new X509Certificate[0];}
}
