package com.example.soundsaga;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soundsaga.databinding.ActivityAudioBookBinding;
import com.example.soundsaga.databinding.AudioPagerEntryBinding;

public class AudioPageHolder extends RecyclerView.ViewHolder {

    AudioPagerEntryBinding binding;
    public AudioPageHolder(AudioPagerEntryBinding binding) {
        super(binding.getRoot());

        this.binding = binding;



    }
}
