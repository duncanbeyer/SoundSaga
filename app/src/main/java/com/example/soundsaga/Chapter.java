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
    }


    protected Chapter(Parcel in) {
        number = in.readInt();
        title = in.readString();
        url = in.readString();
        startTime = in.readInt();
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
    }
}