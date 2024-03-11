package com.example.soundsaga;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.soundsaga.databinding.ActivityMainBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnLongClickListener {

    private ActivityMainBinding binding;
    private static final String TAG = "MainActivity";
    Adapter adapter;
    ImageView shelfButton;
    ArrayList<Book> myBooks = new ArrayList<>();
    ArrayList<Audio> audios = new ArrayList<>();
    ArrayList<Book> loadedBooks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter =
                new Adapter(this, audios);
        binding.recycler.setAdapter(adapter);
        binding.recycler.setLayoutManager(
                new GridLayoutManager(this, 2));

        shelfButton = binding.getRoot().findViewById(R.id.shelf);
        shelfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"opening my books");
                openMyBooks();
            }
        });

        try {
            myBooks = getIntent().getParcelableArrayListExtra("books");
            handleData();
        } catch (Exception e) {
            loadFile();
            adapter.notifyDataSetChanged();
        }
//        clearFile();
    }

    void openMyBooks() {
        Intent intent = new Intent(this, MyBooksActivity.class);
        intent.putExtra("books", myBooks);
        startActivity(intent);
    }

    void handleData() {

        while(audios.size() > 0) {
            audios.remove(0);
        }
        for (Book book : myBooks) {
            audios.add(book.getAudio());
        }
        saveFile();
        adapter.notifyDataSetChanged();

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

    void clearFile() {
        try {
            FileOutputStream fos = getApplicationContext().openFileOutput("Docs.json", Context.MODE_PRIVATE);
            OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);


            writer.write("");
            writer.close();
            fos.close();
        } catch (Exception e) {
            Log.d(TAG,"Exception saving file: ", e);
        }
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

        adapter.notifyDataSetChanged();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.recycler.setLayoutManager(new GridLayoutManager(this, 4));
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            binding.recycler.setLayoutManager(new GridLayoutManager(this, 2));
        }

    }

    @Override
    public void onClick(View view) {
        goOn(binding.recycler.getChildLayoutPosition(view));
    }

    private void goOn(int i) {
        Intent intent = new Intent(this, AudioBookActivity.class);
        intent.putExtra("books", myBooks);
        intent.putExtra("index", i);
        intent.putExtra("flag", false);
        startActivity(intent);
    }

    @Override
    public boolean onLongClick(View view) {
        int i = binding.recycler.getChildLayoutPosition(view);
        Log.d(TAG,"onLongClick");

        Audio a = myBooks.get(i).getAudio();

        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.info_alert, null);

        TextView title = v.findViewById(R.id.title_date);
        TextView author = v.findViewById(R.id.author);
        TextView chapters = v.findViewById(R.id.chapters);
        TextView length = v.findViewById(R.id.length);
        TextView language = v.findViewById(R.id.language);
        ImageView thumbnail = v.findViewById(R.id.thumbnail);

        title.setText(a.getTitle() + " (" + a.getDate() + ")");
        author.setText(a.getAuthor());
        chapters.setText(a.getMyChapters().size() + " Chapters");
        length.setText(a.getDuration());
        language.setText(a.getLanguage());
        Picasso.get().load(a.getImage()).into(thumbnail);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(v);

        builder.setPositiveButton("ok", null);
        AlertDialog dialog = builder.create();
        dialog.show();

        return true;
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
//        downloadData();

    }

    void saveFile() {
        try {
            FileOutputStream fos = getApplicationContext().openFileOutput("Docs.json", Context.MODE_PRIVATE);
            OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);

            JSONArray jsonArray = new JSONArray();

            for (Book book : myBooks) {
                jsonArray.put(book.toJson());
            }
            for (int i = 0;i < myBooks.size();i++) {
                if (!myBooks.get(i).getLastReadDate().equals("")) {
                    jsonArray.put(myBooks.get(i).toJson());
                }
            }
            String jsonString = jsonArray.toString();
            writer.write(jsonString);
            writer.close();
            fos.close();
        } catch (Exception e) {
            Log.d(TAG,"Exception saving file: ", e);
        }
    }


}