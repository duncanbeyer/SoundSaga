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
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;


public class AudioBookActivity extends AppCompatActivity {

    private ActivityAudioBookBinding binding;
    private static final String TAG = "AudioBookActivity";
    public MediaPlayer player;
    private Audio audio;
    private Chapter[] chapters;
    int currentChapter;
    SeekBar seekBar;
    private PopupMenu popupMenu;
    int startTime = 0;
    float speed = 1;
    private Timer timer;
    TextView progress;
    TextView duration;
    TextView speedText;
    ImageView playPause;
    AudioBookAdapter adapter;

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
        progress = binding.viewPager.findViewById(R.id.progress);
        duration = binding.viewPager.findViewById(R.id.duration);
        playPause = binding.viewPager.findViewById(R.id.play_pause);

        seekBar = binding.viewPager.findViewById(R.id.seekBar);

        player = new MediaPlayer();
        player.setOnCompletionListener(mediaPlayer -> {
            Log.d(TAG,"Finished playing.");
        });

        adapter = new AudioBookAdapter(this, chapters, audio, player);

        binding.viewPager.setAdapter(adapter);
        binding.viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                moveChapter(position);
            }
        });
        binding.viewPager.setCurrentItem(0);

        setUpSeekBar();

    }

    void moveChapter(int chapterNum) {
        player.pause();
        playIt(chapters[chapterNum].getUrl(), chapters[chapterNum].getStartTime());
    }

    public void playIt(String url, int startTime) {

        try {

            player.stop();
            player.reset();
            player.setDataSource(url);
            player.prepare();
            int dur = player.getDuration();
            seekBar.setMax(dur);
            player.seekTo(startTime);
            player.start();
            player.setPlaybackParams(player.getPlaybackParams().setSpeed(speed));
            timerCounter();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void timerCounter() {
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    if (player != null && player.isPlaying()) {
                        seekBar.setProgress(player.getCurrentPosition());
                        progress.setText(getTimeStamp(player.getCurrentPosition()));
                        duration.setText(getTimeStamp(player.getDuration()));
                    }
                });
            }
        };
        timer.schedule(task, 0, 1000);
    }

    private String getTimeStamp(int ms) {
        int t = ms;
        int h = ms / 3600000;
        t -= (h * 3600000);
        int m = t / 60000;
        t -= (m * 60000);
        int s = t / 1000;
        return String.format(Locale.getDefault(), "%02d:%02d:%02d", h, m, s);
    }

    private void setUpSeekBar() {


        seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                        // Don't need
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        // Don't need
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        int progress = seekBar.getProgress();
                        player.seekTo(progress);

                    }
                });
    }

    @Override
    protected void onDestroy() {
        timer.cancel();
        player.release();
        player = null;
        super.onDestroy();
    }

}
