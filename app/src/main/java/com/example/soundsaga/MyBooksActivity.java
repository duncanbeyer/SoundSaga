package com.example.soundsaga;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.soundsaga.databinding.ActivityMyBooksBinding;

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
        binding.recycler.setLayoutManager(
                new GridLayoutManager(this, 1));

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
        Log.d(TAG,"books: " + booksWithData);
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
        startActivity(intent);
    }

    @Override
    public boolean onLongClick(View view) {
        return false;
    }
}
