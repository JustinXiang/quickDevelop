package com.example.quickdevelop.net;

import android.content.Context;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.GlideModule;
import okhttp3.OkHttpClient;

import java.io.InputStream;

/**
 * @describe describe
 */
public class OkHttpAppGlideModule implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        // Do nothing.
    }
    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        OkHttpClient client = HttpsGlideModule.UnsafeOkHttpClient.getUnsafeOkHttpClient();
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(client));
    }


}

