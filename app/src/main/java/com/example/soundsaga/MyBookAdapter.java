package com.example.soundsaga;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.example.soundsaga.databinding.BookItemBinding;
import com.squareup.picasso.Picasso;


public class MyBookAdapter extends RecyclerView.Adapter<MyBookViewHolder> {

    private ArrayList<Book> myBooks;
    private static final String TAG = "MyBookAdapter";
    MyBooksActivity booksActivity;

    public MyBookAdapter(MyBooksActivity booksActivity, ArrayList<Book> myBooks) {
        this.myBooks = myBooks;
        this.booksActivity = booksActivity;
    }

    @NonNull
    @Override
    public MyBookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        BookItemBinding binding =
                BookItemBinding.inflate(
                        LayoutInflater.from(parent.getContext()), parent, false);

        binding.getRoot().setOnClickListener(booksActivity);
        binding.getRoot().setOnLongClickListener(booksActivity);
        return new MyBookViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull MyBookViewHolder holder, int position) {

        Book book = myBooks.get(position);
        Audio audio = book.getAudio();
        Chapter chapter = audio.getChapter(book.getChapter());

        holder.binding.title.setText(book.getAudio().getTitle());
        holder.binding.author.setText(audio.getAuthor());
        holder.binding.chapter.setText(chapter.getTitle());
        holder.binding.lastRead.setText(book.getLastReadDate() + " " + book.getLastReadTime());
        holder.binding.progress.setText(convertMsToMmSs(chapter.getStartTime()) + " of " + convertMsToMmSs(chapter.getDuration()));

        Picasso.get().load(audio.getImage()).into(holder.binding.thumbnail);

    }

    public String convertMsToMmSs(int milliseconds) {
        int totalSeconds = milliseconds / 1000;

        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;

        return String.format("%02d:%02d", minutes, seconds);
    }

    @Override
    public int getItemCount() {
        return myBooks.size();
    }
}
