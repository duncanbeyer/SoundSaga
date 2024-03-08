package com.example.soundsaga;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.PopupMenu;
import android.util.Log;

import com.example.soundsaga.databinding.ActivityAudioBookBinding;
import com.example.soundsaga.databinding.ActivityMainBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.MessageFormat;


public class AudioBookActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnLongClickListener {

    private ActivityAudioBookBinding binding;
    private static final String TAG = "AudioBookActivity";
    public MediaPlayer player;
    private Audio audio;

    private PopupMenu popupMenu;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAudioBookBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        try {
            audio = savedInstanceState.getParcelable("audio");
        } catch (Exception e) {
            Log.d(TAG,"Error getting audio in AudioBookActivity onCreate: ", e);
        }

        startAudioPlayer();


    }

    private void startAudioPlayer() {
        Intent intent = new Intent(this, AudioBookActivity.class);
        player = new MediaPlayer();
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {


    }

    @Override
    public boolean onLongClick(View view) {
        return false;
    }

    public void playIt() {

        binding.title.setText(MessageFormat.format("Title {0}", mediaCounter));
        try {

            player.stop();
            player.reset();
            player.setDataSource(url);
            player.prepare();
            int dur = player.getDuration();
            binding.seekBar.setMax(dur);

            player.seekTo(startTime);
            player.start();
            player.setPlaybackParams(player.getPlaybackParams().setSpeed(speed));
            timerCounter();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
