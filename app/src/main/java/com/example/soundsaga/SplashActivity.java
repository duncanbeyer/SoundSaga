package com.example.soundsaga;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.splashscreen.SplashScreen;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {
    private boolean keepOn = true;
    private static final String TAG = "SplashActivity";
    ArrayList<Book> myBooks = new ArrayList<>();
    ArrayList<Audio> audios = new ArrayList<>();
    ArrayList<Book> loadedBooks = new ArrayList<>();

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
        loadFile();

    }

    public void loadFile() {
        myBooks = new ArrayList<>();
        try {
            InputStream is = getApplicationContext().openFileInput("Docs.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            Log.d(TAG,"Saved data: " + sb);
            JSONArray arr = new JSONArray(sb.toString());
            for (int i = 0; i < arr.length();i++) {
                loadedBooks.add(new Book(arr.getJSONObject(i)));
            }
            is.close();
        } catch (Exception e) {}
        downloadData();

    }

    public void downloadData() {

        RequestQueue queue = Volley.newRequestQueue(this);

        String ex = "https://christopherhield.com/ABooks/abook_contents.json";

        JsonArrayRequest jsonArrayRequest =
                new JsonArrayRequest(Request.Method.GET, ex,
                        null,
                        response -> {
                            jsonToArr(response);
                        },
                        error -> {
                            Log.e(TAG, "Exception getting JSON data: " + error.getMessage());
                        }) {
                };

        queue.add(jsonArrayRequest);
    }

    private void jsonToArr(JSONArray arr) {
        myBooks = new ArrayList<>();
        try {
            Log.d(TAG,"length of arr: " + arr.length());

            for (int i = 0;i < arr.length();i++) {
                audios.add(new Audio(arr.getJSONObject(i)));
                myBooks.add(new Book(audios.get(i)));
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception loading JSON: " + e);
        }
        if (loadedBooks.size() > 0) {
            for (int j = 0;j < loadedBooks.size();j++) {
                for (int i = 0; i < myBooks.size(); i++) {
                    if (myBooks.get(i).getAudio().getTitle().equals(loadedBooks.get(j).getAudio().getTitle())) {
                        myBooks.set(i, loadedBooks.get(j));
                        break;
                    }
                }
            }
        }

        goOn();

    }

    private void goOn() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("books", myBooks);

        startActivity(intent);
    }


}
