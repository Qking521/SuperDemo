package com.king.javaapp;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
public class OkHttpModule {

    @Singleton
    @OkHttpNoParam
    @Provides
    public OkHttpClient pa() {
        return new OkHttpClient();
    }

    @OkHttpWithParam
    @Provides
    public OkHttpClient pb() {
        return new OkHttpClient();
    }
}
