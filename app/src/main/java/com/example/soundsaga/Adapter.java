package com.example.soundsaga;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import com.squareup.picasso.Picasso;


public class Adapter extends RecyclerView.Adapter<AudioViewHolder> {

    private ArrayList<Audio> audios = new ArrayList<>();
    MainActivity mainActivity;

    public Adapter(MainActivity mainActivity, ArrayList<Audio> audios) {
        this.audios = audios;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public AudioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View audioView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_item, parent, false);

        audioView.setOnClickListener(mainActivity);
        audioView.setOnLongClickListener(mainActivity);
        return new AudioViewHolder(audioView);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioViewHolder holder, int position) {

        Audio a = audios.get(position);
        holder.title.setText(a.getTitle());
        holder.author.setText(a.getAuthor());
        Picasso.get().load(a.getImage()).into(holder.pic);
        holder.title.setSelected(true);
        holder.author.setSelected(true);

    }

    @Override
    public int getItemCount() {
        return audios.size();
    }
}
