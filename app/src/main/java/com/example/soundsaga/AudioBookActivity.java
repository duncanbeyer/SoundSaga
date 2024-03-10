package com.example.soundsaga;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.soundsaga.databinding.ActivityAudioBookBinding;
import com.example.soundsaga.databinding.ActivityMainBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;


public class AudioBookActivity extends AppCompatActivity {

    private ActivityAudioBookBinding binding;
    private static final String TAG = "AudioBookActivity";
    public MediaPlayer player;
    private Audio audio;
    private ArrayList<Chapter> chapters;
    TextView progress;
    TextView duration;
    ImageView playPause;
    AudioBookAdapter adapter;
    Book book;
    int index;
    ArrayList<Book> books;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAudioBookBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        try {
            index = getIntent().getIntExtra("index", 0);
        } catch (Exception e) {
            Log.d(TAG,"Error getting index in AudioBookActivity onCreate: ", e);
        }
        try {
            books = getIntent().getParcelableArrayListExtra("books");
        } catch (Exception e) {
            Log.d(TAG,"Error getting books in AudioBookActivity onCreate: ", e);
        }

        book = books.get(index);
        audio = book.getAudio();
        chapters = audio.getChapters();

        progress = binding.viewPager.findViewById(R.id.progress);
        duration = binding.viewPager.findViewById(R.id.duration);
        playPause = binding.viewPager.findViewById(R.id.play_pause);

        player = new MediaPlayer();
        player.setOnCompletionListener(mediaPlayer -> {
            adapter.switchChapters(-1);
        });

        adapter = new AudioBookAdapter(this, chapters, audio, player, binding.viewPager);

        binding.viewPager.setAdapter(adapter);
        binding.viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                adapter.switchChapters(position);
                Log.d(TAG,"Switching chapters");
            }
        });
        binding.viewPager.setCurrentItem(0);

    }

    public void saveData() {

        audio.saveChapters(chapters);
        book.save(audio, adapter.pageNum);
        books.set(index, book);

    }

    @Override
    protected void onDestroy() {
        player.release();
        player = null;

        saveData();

        adapter.cancelTimer();

        super.onDestroy();
    }

}
