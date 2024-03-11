package com.example.soundsaga;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
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
        Log.d(TAG,"start time (in goOn) should be " + allBooks.get(index).getAudio().getChapter(allBooks.get(i).getChapter()).getStartTime());
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

        SpannableString spannableString = new SpannableString("Remove your book history for " + booksWithData.get(ind).getAudio().getTitle() + "?");

        int startIndex = 29;
        int endIndex = spannableString.length()-1;
        spannableString.setSpan(new StyleSpan(Typeface.ITALIC), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        builder.setMessage(spannableString);
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

    void refreshBook(int index) {
        Book book = allBooks.get(index);
        book.refresh();
        allBooks.set(index, book);
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("books", allBooks);
        startActivity(intent);
    }

}
