package com.example.soundsaga;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AudioViewHolder extends RecyclerView.ViewHolder {

    TextView title;
    TextView author;
    ImageView pic;
    public AudioViewHolder(@NonNull View itemView) {
        super(itemView);

        title = itemView.findViewById(R.id.title);
        author = itemView.findViewById(R.id.author);

        pic = itemView.findViewById(R.id.thumbnail);


    }
}
