package com.example.soundsaga;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONObject;

public class Chapter implements Parcelable {
    private static final String TAG = "Chapter";
    private int number;
    private String title;
    private String url;
    private int startTime = 0;
    private int duration = 0;
    public Chapter(JSONObject j) {

        try {
            number = j.getInt("number");
        } catch (Exception e) {
            Log.d(TAG,"error getting chapter num");
            number = 0;
        }
        try {
            title = j.getString("title");
            if (title.equals("null")) {
                throw new Exception();
            }
        } catch (Exception e) {
            title = "No title Found";
        }
        try {
            url = j.getString("url");
            if (url.equals("null")) {
                throw new Exception();
            }
        } catch (Exception e) {
            url = "";
        }
        try {
            startTime = j.getInt("startTime");
        } catch (Exception e) {
            startTime = 0;
        }
        try {
            duration = j.getInt("duration");
        } catch (Exception e) {
            duration = 0;
        }
    }


    protected Chapter(Parcel in) {
        number = in.readInt();
        title = in.readString();
        url = in.readString();
        startTime = in.readInt();
        duration = in.readInt();
    }

    public static final Creator<Chapter> CREATOR = new Creator<Chapter>() {
        @Override
        public Chapter createFromParcel(Parcel in) {
            return new Chapter(in);
        }

        @Override
        public Chapter[] newArray(int size) {
            return new Chapter[size];
        }
    };

    public int getNumber() {
        return number;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }
    public int getStartTime() { return startTime; }

    public void updateStartTime(int i) {
        startTime = i;
    }
    public int getDuration() {
        return duration;
    }
    public void updateDuration(int d) {
        duration = d;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(number);
        parcel.writeString(title);
        parcel.writeString(url);
        parcel.writeInt(startTime);
        parcel.writeInt(duration);
    }

    public JSONObject toJson() {
        JSONObject j = new JSONObject();
        try {
            j.put("number", number);
            j.put("title", title);
            j.put("url", url);
            j.put("startTime", startTime);
            j.put("duration", duration);
        } catch (Exception e) {
            Log.d(TAG,"Error converting chapter to Json: ", e);
        }
        return j;
    }
}