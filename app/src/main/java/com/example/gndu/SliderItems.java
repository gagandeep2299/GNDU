package com.example.gndu;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
public class SliderItems {
    //set to String, if you want to add image url from internet
    private int image;
    public SliderItems(int image) {
        this.image = image;
    }
    public int getImage() {
        return image;
    }
}