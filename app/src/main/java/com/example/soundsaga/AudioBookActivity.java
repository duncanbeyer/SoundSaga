package com.example.soundsaga;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ImageView;
import android.util.Log;
import android.widget.TextView;

import com.example.soundsaga.databinding.ActivityAudioBookBinding;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;


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
    boolean flag;

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
        chapters = audio.getMyChapters();

        try {
            flag = getIntent().getBooleanExtra("flag", false);
            if(!flag) { // if coming from mainActivity, always refresh
                book.refresh();
                books.set(index,book);
            }
        } catch (Exception e) {
            Log.d(TAG,"Error getting books in AudioBookActivity onCreate: ", e);
        }

//        Log.d(TAG,"Book data:");
//        Log.d(TAG,"Last chapter: " + book.getChapter());
//        Log.d(TAG,"Progress: " + chapters.get(book.getChapter()).getStartTime());

        progress = binding.viewPager.findViewById(R.id.progress);
        duration = binding.viewPager.findViewById(R.id.duration);
        playPause = binding.viewPager.findViewById(R.id.play_pause);

        player = new MediaPlayer();
        player.setOnCompletionListener(mediaPlayer -> {
            adapter.switchChapters(-1);
        });

        adapter = new AudioBookAdapter(this, chapters, audio, player, binding.viewPager, flag);

        binding.viewPager.setAdapter(adapter);
        binding.viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                adapter.switchChapters(position);
            }
        });
        binding.viewPager.setCurrentItem(book.getChapter());

    }

    public void back() {

        if (flag) {
            Intent intent = new Intent(this, MyBooksActivity.class);
            intent.putExtra("books", books);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("books", books);
            startActivity(intent);
        }


    }

    public void saveData() {

        audio.saveChapters(chapters);
        book.save(audio, adapter.pageNum);
        books.set(index, book);

    }

    @Override
    protected void onDestroy() {
//        player.release();
//        player = null;
//
//        saveData();
//
//        adapter.cancelTimer();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

        adapter.saveProgress(adapter.pageNum);

        player.release();
        player = null;

        saveData();

        adapter.cancelTimer();

        back();
    }

}
