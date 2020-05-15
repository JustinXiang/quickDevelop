package com.example.quickdevelop.net;

import android.app.ActivityManager;
import android.content.Context;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.executor.GlideExecutor;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import okhttp3.OkHttpClient;

import javax.net.ssl.*;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

/**
 * Created by Y
 * on 2019/6/26
 */
@GlideModule
public class HttpsGlideModule extends AppGlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
       /* if (activityManager != null) {
            builder.setDefaultRequestOptions(ImageUtil.getOption());
        }*/
        //设置Bitmap的缓存池
        builder.setBitmapPool(new LruBitmapPool(30));

        //设置内存缓存
        builder.setMemoryCache(new LruResourceCache(30));

        //设置磁盘缓存
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context));

        //设置读取不在缓存中资源的线程
        builder.setResizeExecutor(GlideExecutor.newSourceExecutor());

        //设置读取磁盘缓存中资源的线程
        builder.setDiskCacheExecutor(GlideExecutor.newDiskCacheExecutor());

        //设置日志级别
//        builder.setLogLevel(Log.VERBOSE);

        //设置全局选项
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.format(DecodeFormat.PREFER_RGB_565);
        requestOptions.disallowHardwareConfig();
        builder.setDefaultRequestOptions(requestOptions);
    }


    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        //设置请求方式为okhttp 并设置okhttpClient的证书及超时时间
        OkHttpUrlLoader.Factory factory = new OkHttpUrlLoader.Factory(UnsafeOkHttpClient.getUnsafeOkHttpClient());
        registry.replace(GlideUrl.class, InputStream.class, factory);
    }

    //自定义工具类修改OkHttpClient证书和超时时间
    static class UnsafeOkHttpClient {
        public static OkHttpClient getUnsafeOkHttpClient() {
            try {
                // Create a trust manager that does not validate certificate chains
                final TrustManager[] trustAllCerts = new TrustManager[]{
                        new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            }

                            @Override
                            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            }

                            @Override
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return new java.security.cert.X509Certificate[]{};
                            }
                        }
                };

                // Install the all-trusting trust manager
                final SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

                // Create an ssl socket factory with our all-trusting manager
                final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
                builder.hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });

                builder.connectTimeout(20, TimeUnit.SECONDS);
                builder.readTimeout(20, TimeUnit.SECONDS);
                builder.writeTimeout(20, TimeUnit.SECONDS);

                OkHttpClient okHttpClient = builder.build();
                return okHttpClient;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        /**
         * 禁止解析Manifest文件
         * 主要针对V3升级到v4的用户，可以提升初始化速度，避免一些潜在错误
         * @return
         */
       /* @Override
        public boolean isManifestParsingEnabled() {
            return false;
        }*/
    }
}