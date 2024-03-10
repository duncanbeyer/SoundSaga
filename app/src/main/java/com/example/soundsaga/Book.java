package com.example.soundsaga;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Book  implements Parcelable {
    private static final String TAG = "Book";
    private int chapter = 0;
    private String lastReadDate = "";
    private String lastReadTime = "";
    private Audio audio;

    public Book(Audio a) {
        audio = a;
    }

    public Book(JSONObject j) {
        try {
            chapter = j.getInt("chapter");
        } catch (Exception e) {
            chapter = 0;
        }
        try {
            lastReadDate = j.getString("lastReadDate");
            if (lastReadDate.equals(null)) {
                throw new Exception();
            }
        } catch (Exception e) {
            lastReadDate = "";
        }
        try {
            lastReadTime = j.getString("lastReadTime");
            if (lastReadTime.equals(null)) {
                throw new Exception();
            }
        } catch (Exception e) {
            lastReadTime = "";
        }
        try {
            audio = new Audio(j.getJSONObject("audio"));
            if (audio == (null)) {
                throw new Exception();
            }
        } catch (Exception e) {
             Log.d(TAG,"Exception getting audio from json: ", e);
        }
    }

    protected Book(Parcel in) {
        chapter = in.readInt();
        lastReadDate = in.readString();
        lastReadTime = in.readString();
        audio = in.readParcelable(Audio.class.getClassLoader());
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public void setChapter(int chapter) {
        this.chapter = chapter;
    }


    public void setLastReadDate(String lastReadDate) {
        this.lastReadDate = lastReadDate;
    }

    public void setLastReadTime(String lastReadTime) {
        this.lastReadTime = lastReadTime;
    }

    public int getChapter() {
        return chapter;
    }
    public String getLastReadDate() {
        return lastReadDate;
    }
    public String getLastReadTime() {
        return lastReadTime;
    }
    public Audio getAudio() {
        return audio;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(chapter);
        parcel.writeString(lastReadDate);
        parcel.writeString(lastReadTime);
        parcel.writeParcelable(audio, i);
    }

    public JSONObject toJson() {
        JSONObject j = new JSONObject();

        try {
            j.put("chapter", chapter);
            j.put("lastReadDate", lastReadDate);
            j.put("lastReadTime", lastReadTime);
            j.put("audio", audio.toJson());

        } catch (Exception e) {
            Log.d(TAG,"Exception converting Book to Json: ", e);
        }

        return j;
    }

    public void save(Audio audio, int chapter) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date currentDate = new Date();
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm");
        Date currentTime = new Date();
        this.audio = audio;
        this.chapter = chapter;
        this.lastReadDate =dateFormat.format(currentDate);
        this.lastReadTime = timeFormat.format(currentTime);
    }
}
