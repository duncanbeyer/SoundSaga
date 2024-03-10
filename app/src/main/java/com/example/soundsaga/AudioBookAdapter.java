package com.example.soundsaga;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.soundsaga.databinding.ActivityAudioBookBinding;
import com.example.soundsaga.databinding.AudioPagerEntryBinding;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class AudioBookAdapter extends RecyclerView.Adapter<AudioPageHolder>{

    private ArrayList<Chapter> chapters = new ArrayList<>();
    private AudioBookActivity act;
    private static final String TAG = "AudioBookAdapter";
    ActivityAudioBookBinding binding;
    private Timer timer;
    private ArrayList<TextView> progresses = new ArrayList<>();
    private ArrayList<TextView> durations = new ArrayList<>();

    SeekBar seekBar;

    MediaPlayer player;
    float speed = 1f;
    Audio audio;
    private PopupMenu popupMenu;
    public int pageNum;
    private ViewPager2 viewPager;
    ImageView backArrow;
    ImageView forwardArrow;
    TextView speedText;

    public AudioBookAdapter(AudioBookActivity act, ArrayList<Chapter> chapters, Audio a, MediaPlayer player, ViewPager2 viewPager) {
         this.chapters.addAll(chapters);
         this.act = act;
         this.audio = a;
         this.player = player;
         this.viewPager = viewPager;
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
        Chapter chapter = chapters.get(position);

        holder.binding.title.setText(audio.getTitle());

        holder.binding.chapter.setText(chapter.getTitle() + "(" + (position+1) + " of " + chapters.size() + ")");

        Picasso.get().load(audio.getImage()).into(holder.binding.cover);

        speedText = holder.binding.speedText;

        speedText.setText(String.valueOf(speed) + 'x');
        holder.binding.speedText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speedClick();
            }
        });

        holder.binding.nextArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() != chapters.size()-1) {
                    int nextItem = viewPager.getCurrentItem() + 1;
                    viewPager.setCurrentItem(nextItem, true);
                }
            }
        });

        if (viewPager.getCurrentItem() == chapters.size()-1) {
            holder.binding.nextArrow.setVisibility(INVISIBLE);
        }

        forwardArrow = holder.binding.nextArrow;

        holder.binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() != 0) {
                    int nextItem = viewPager.getCurrentItem() - 1;
                    viewPager.setCurrentItem(nextItem, true);
                }
            }
        });

        if (position == 0) {
            holder.binding.backArrow.setVisibility(INVISIBLE);
        }
        backArrow = holder.binding.backArrow;

        holder.binding.playPause.setImageResource(R.drawable.play);
        holder.binding.playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doPlayPause(holder.binding.playPause);
            }
        });

        holder.binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });

        holder.binding.forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goForward();
            }
        });

        setupSpeedMenu();

        holder.binding.progress.setText(getTimeStamp(chapter.getStartTime()));
        holder.binding.duration.setText(getTimeStamp(chapter.getDuration()));
        progresses.add(position, holder.binding.progress);
        durations.add(position, holder.binding.duration);

        seekBar = holder.binding.seekBar;

        seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {}
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {}
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        int progress = seekBar.getProgress();
                        player.seekTo(progress);
                    }
                });
        seekBar.setMax(player.getDuration());


        setArrowVisibility();

    }

    @Override
    public int getItemCount() {
        return chapters.size();
    }

    public void doPlayPause(ImageView button) {
        if (player.isPlaying()) {
            player.pause();
            button.setImageResource(R.drawable.play);
        } else {
            player.start();
            button.setImageResource(R.drawable.pause);
        }
    }

    public void goBack() {
        if (player != null && player.isPlaying()) {
            int pos = player.getCurrentPosition();
            pos -= 15000;
            if (pos < 0)
                pos = 0;
            player.seekTo(pos);
        }
    }

    public void goForward() {
        if (player != null && player.isPlaying()) {
            int pos = player.getCurrentPosition();
            pos += 15000;
            if (pos > player.getDuration())
                pos = player.getDuration();
            player.seekTo(pos);
        }
    }

    public void speedClick() {
        popupMenu.show();
    }

    private void setupSpeedMenu() {
        popupMenu = new PopupMenu(act, speedText);
        popupMenu.getMenuInflater().inflate(R.menu.speed_popup, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.menu_075) {
                speed = 0.75f;
            } else if (menuItem.getItemId() == R.id.menu_1) {
                speed = 1f;
            } else if (menuItem.getItemId() == R.id.menu_11) {
                speed = 1.1f;
            } else if (menuItem.getItemId() == R.id.menu_125) {
                speed = 1.25f;
            } else if (menuItem.getItemId() == R.id.menu_15) {
                speed = 1.5f;
            } else if (menuItem.getItemId() == R.id.menu_175) {
                speed = 1.75f;
            } else if (menuItem.getItemId() == R.id.menu_2) {
                speed = 2f;
            }

            speedText.setText(menuItem.getTitle());

            player.setPlaybackParams(player.getPlaybackParams().setSpeed(speed));
            return true;
        });
    }

    public void playIt(String url, int startTime, int position) {
        try {

            player.stop();
            player.reset();
            player.setDataSource(url);
            player.prepare();
            if (seekBar != null) {
                seekBar.setMax(player.getDuration());
            }
            player.seekTo(startTime);
            seekBar.setProgress(startTime);
            player.start();
            player.setPlaybackParams(player.getPlaybackParams().setSpeed(speed));
            Log.d(TAG,"startTime is " + startTime + " duration is " + player.getDuration());

        } catch (Exception e) {
            e.printStackTrace();
        }


        timerCounter();
        player.pause();

    }

    private void timerCounter() {
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                act.runOnUiThread(() -> {
                    if (player != null && player.isPlaying()) {
                        if (seekBar != null) {
                            seekBar.setProgress(player.getCurrentPosition());
                        }
                        if (progresses.size() > pageNum) {
                            progresses.get(pageNum).setText(getTimeStamp(player.getCurrentPosition()));
                        }
                        if (durations.size() > pageNum) {
                            durations.get(pageNum).setText(getTimeStamp(player.getDuration()));
                        }
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

    void cancelTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }

    void switchChapters(int position) {
        if (position == pageNum && player.isPlaying()) return;

        player.stop();

        if (position == -1) {
            chapters.get(pageNum).updateStartTime(0);
            if (pageNum == chapters.size()-1) {
                return;
            }
            pageNum++;
        }
        else {
            pageNum = position;
        }

//        setArrowVisibility();

        cancelTimer();

        playIt(chapters.get(pageNum).getUrl(), chapters.get(pageNum).getStartTime(), position);

    }

    void setArrowVisibility() {
        if (pageNum == 0) {
            Log.d(TAG,"back arrow: invisible");
            backArrow.setVisibility(View.INVISIBLE);
        }
        else if (pageNum == chapters.size()-1) {
            forwardArrow.setVisibility(View.INVISIBLE);
        }
        else {
            if (backArrow.getVisibility() == View.INVISIBLE) {
                Log.d(TAG,"back arrow: visible");
                backArrow.setVisibility(View.VISIBLE);
            }
            if (forwardArrow.getVisibility() == View.INVISIBLE) {
                forwardArrow.setVisibility(View.VISIBLE);
            }
        }
    }
    void saveProgress(int i) {
        Log.d(TAG,"Saving progress, i is " + i);
        chapters.get(i).updateStartTime(player.getCurrentPosition());
        if (chapters.get(i).getDuration() == 0) {
            chapters.get(i).updateDuration(player.getDuration());
        }
    }

}
