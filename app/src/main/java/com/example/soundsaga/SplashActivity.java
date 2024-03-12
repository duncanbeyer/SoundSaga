package com.example.soundsaga;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.splashscreen.SplashScreen;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {
    private boolean keepOn = true;
    private static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SplashScreen.installSplashScreen(this)
                .setKeepOnScreenCondition(
                        new SplashScreen.KeepOnScreenCondition() {
                            @Override
                            public boolean shouldKeepOnScreen() {
                                return keepOn;
                            }
                        }
                );
        try {
            Thread.sleep(2000);
            goOn();
        } catch (InterruptedException e) {
            Log.d(TAG,"Sleep failed: ", e);
        }
    }

    private void goOn() {
        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);
    }


}
