package com.example.soundsaga;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soundsaga.databinding.BookItemBinding;

public class MyBookViewHolder extends RecyclerView.ViewHolder {
    BookItemBinding binding;

    public MyBookViewHolder(BookItemBinding binding) {
        super(binding.getRoot());

        this.binding = binding;


    }
}
