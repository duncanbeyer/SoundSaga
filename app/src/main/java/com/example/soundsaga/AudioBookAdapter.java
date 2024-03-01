package com.example.soundsaga;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soundsaga.databinding.ActivityAudioBookBinding;
import com.example.soundsaga.databinding.AudioPagerEntryBinding;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import androidx.appcompat.app.AppCompatActivity;


import java.util.ArrayList;

public class AudioBookAdapter extends RecyclerView.Adapter<AudioPageHolder>{

    private ArrayList<Audio> audios;
    private AudioBookActivity act;
    private static final String TAG = "AudioBookAdapter";
    ActivityAudioBookBinding binding;
    public AudioBookAdapter(AudioBookActivity act, ArrayList<Audio> audios) {
        this.audios = audios;
        this.act = act;

    }
    @NonNull
    @Override
    public AudioPageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        AudioPagerEntryBinding binding =
                AudioPagerEntryBinding.inflate(
                        LayoutInflater.from(parent.getContext()), parent, false);

        return new AudioPageHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioPageHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return audios.size();
    }
}
