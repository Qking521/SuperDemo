package com.king.javaapp;

import android.util.Log;

import javax.inject.Inject;

public class Car {

    Engin engin;

    @Inject
    public Car(Engin engin) {
        this.engin = engin;
    }

    public void printCarBrand() {
        Log.v("wq", "printCarBrand: bmw");
    }

    public void printEnginName() {
        Log.v("wq", "printEnginName: ="+ engin.getEnginName());
    }
}
