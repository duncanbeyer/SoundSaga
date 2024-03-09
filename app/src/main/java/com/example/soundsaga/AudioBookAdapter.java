package com.example.soundsaga;

import static androidx.appcompat.content.res.AppCompatResources.getDrawable;

import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soundsaga.databinding.ActivityAudioBookBinding;
import com.example.soundsaga.databinding.AudioPagerEntryBinding;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

public class AudioBookAdapter extends RecyclerView.Adapter<AudioPageHolder>{

    private ArrayList<Chapter> chapters = new ArrayList<>();
    private AudioBookActivity act;
    private static final String TAG = "AudioBookAdapter";
    ActivityAudioBookBinding binding;
    MediaPlayer player;
    float speed;
    Audio audio;
    private PopupMenu popupMenu;

    public AudioBookAdapter(AudioBookActivity act, Chapter[] chapters, Audio a, MediaPlayer player) {
         for (Chapter chapter : chapters) {
             this.chapters.add(chapter);
         }
         this.act = act;
         this.audio = a;
         this.player = player;
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

        holder.binding.chapter.setText(chapter.getTitle());

        Picasso.get().load(audio.getImage()).into(holder.binding.cover);

        holder.binding.speedText.setText(String.valueOf(speed) + 'x');
        holder.binding.speedText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speedClick();
            }
        });

        holder.binding.playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doPlayPause(holder);
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

        setupSpeedMenu(holder);

    }

    @Override
    public int getItemCount() {
        return chapters.size();
    }

    public void doPlayPause(AudioPageHolder holder) {
        if (player.isPlaying()) {
            player.pause();
            holder.binding.playPause.setImageResource(R.drawable.play);
        } else {
            player.start();
            holder.binding.playPause.setImageResource(R.drawable.pause);
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

    private void setupSpeedMenu(AudioPageHolder holder) {
        popupMenu = new PopupMenu(act, holder.binding.speedText);
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

            holder.binding.speedText.setText(menuItem.getTitle());

            player.setPlaybackParams(player.getPlaybackParams().setSpeed(speed));
            return true;
        });
    }

}
