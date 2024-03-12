package com.example.soundsaga;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.soundsaga.databinding.ActivityMyBooksBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MyBooksActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnLongClickListener {
    private static final String TAG = "MyBooksActivity";
    ArrayList<Book> booksWithData = new ArrayList<>();
    ArrayList<Integer> indices = new ArrayList<>();
    ActivityMyBooksBinding binding;
    ArrayList<Book> allBooks = new ArrayList<>();
    MyBookAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMyBooksBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter = new MyBookAdapter(this, booksWithData);
        binding.recycler.setAdapter(adapter);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.recycler.setLayoutManager(new GridLayoutManager(this, 2));
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            binding.recycler.setLayoutManager(new GridLayoutManager(this, 1));
        }

        try {
            allBooks = getIntent().getParcelableArrayListExtra("books");
            getData();
        } catch (Exception e) {
            Log.d(TAG,"no books.");
        }
    }

    void getData() {
        for (int i = 0; i < allBooks.size();i++) {
            if (!allBooks.get(i).getLastReadDate().equals("")) {
                booksWithData.add(allBooks.get(i));
                indices.add(i);
            }
        }
        saveFile();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        goOn(binding.recycler.getChildLayoutPosition(view));
    }

    private void goOn(int i) {
        Intent intent = new Intent(this, AudioBookActivity.class);
        intent.putExtra("books", allBooks);
        int index = indices.get(i);
        intent.putExtra("index", index);
        intent.putExtra("flag", true);
        startActivity(intent);
    }

    @Override
    public boolean onLongClick(View view) {
        doDialog(binding.recycler.getChildLayoutPosition(view));
        return false;
    }

    void doDialog(int ind) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View dialogView = getLayoutInflater().inflate(R.layout.delete_alert, null);
        ImageView thumbnailImageView = dialogView.findViewById(R.id.thumbnail);
        TextView title = dialogView.findViewById(R.id.title);
        builder.setView(dialogView);

        SpannableString spannableString = new SpannableString("Remove your book history for " + booksWithData.get(ind).getAudio().getTitle() + "?");

        int startIndex = 29;
        int endIndex = spannableString.length()-1;
        spannableString.setSpan(new StyleSpan(Typeface.ITALIC), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        Picasso.get().load(booksWithData.get(ind).getAudio().getImage()).into(thumbnailImageView);

        title.setText(spannableString);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                booksWithData.remove(ind);
                adapter.notifyDataSetChanged();
                refreshBook(indices.get(ind));
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    void saveFile() {
        try {
            FileOutputStream fos = getApplicationContext().openFileOutput("Docs.json", Context.MODE_PRIVATE);
            OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);

            JSONArray jsonArray = new JSONArray();

            for (Book book : allBooks) {
                jsonArray.put(book.toJson());
            }
            for (int i = 0;i < allBooks.size();i++) {
                if (!allBooks.get(i).getLastReadDate().equals("")) {
                    jsonArray.put(allBooks.get(i).toJson());
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

    void refreshBook(int index) {
        Book book = allBooks.get(index);
        book.refresh();
        allBooks.set(index, book);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.recycler.setLayoutManager(new GridLayoutManager(this, 2));
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            binding.recycler.setLayoutManager(new GridLayoutManager(this, 1));
        }

    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("books", allBooks);
        startActivity(intent);
    }

}
