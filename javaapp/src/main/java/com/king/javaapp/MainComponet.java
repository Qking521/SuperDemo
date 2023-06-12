package com.king.javaapp;

import javax.inject.Singleton;

import dagger.Component;
@Singleton
@Component(modules = OkHttpModule.class)
public interface MainComponet {
    void inject(MainActivity mainActivity);
}
