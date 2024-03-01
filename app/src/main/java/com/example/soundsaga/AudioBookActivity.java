package com.example.soundsaga;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import com.example.soundsaga.databinding.ActivityAudioBookBinding;
import com.example.soundsaga.databinding.ActivityMainBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class AudioBookActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnLongClickListener {

    private ActivityAudioBookBinding binding;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAudioBookBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }

    @Override
    public void onClick(View view) {

        Intent intent = new Intent(this, AudioBookActivity.class);

        startActivity(intent);
    }

    @Override
    public boolean onLongClick(View view) {
        return false;
    }
}
