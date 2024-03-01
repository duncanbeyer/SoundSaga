package com.example.soundsaga;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.AlertDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.soundsaga.databinding.ActivityMainBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnLongClickListener {

    private ActivityMainBinding binding;
    private static final String TAG = "MainActivity";
    Adapter adapter;
    ImageView shelfButton;

    ArrayList<Audio> audios = new ArrayList<>();

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
//        shelfButton.setOnClickListener();

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

        try {

            for (int i = 0;i < arr.length();i++) {
                audios.add(new Audio(arr.getJSONObject(i)));
            }

        } catch (Exception e) {
            Log.d(TAG, "Exception loading JSON: " + e);
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
        Log.d(TAG,"onClick");
    }
    @Override
    public boolean onLongClick(View view) {
        int i = binding.recycler.getChildLayoutPosition(view);
        Log.d(TAG,"onLongClick");

        Audio a = audios.get(i);

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
        chapters.setText(a.getChapters().size() + " Chapters");
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
}