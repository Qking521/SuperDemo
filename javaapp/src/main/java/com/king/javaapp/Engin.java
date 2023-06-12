package com.king.javaapp;

import javax.inject.Inject;

public class Engin {

    @Inject
    public Engin() {

    }

    public String getEnginName() {
        return "v8";
    }
}
