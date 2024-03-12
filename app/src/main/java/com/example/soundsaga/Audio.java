package com.example.soundsaga;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Audio implements Parcelable {
    private static final String TAG = "Audio";
    private String title;
    private String author;
    private String date;
    private String language;
    private String duration;
    private String image;
    private ArrayList<Chapter> myChapters = new ArrayList<>();

    public Audio(JSONObject j) {

        try {
            title = j.getString("title");
            if (title.equals("null")) {
                throw new Exception();
            }
        } catch (Exception e) {
            title = "No Title Found";
        }
        try {
            author = j.getString("author");
            if (author.equals("null")) {
                throw new Exception();
            }
        } catch (Exception e) {
            author = "No author Found";
        }
        try {
            date = j.getString("date");
            if (date.equals("null")) {
                throw new Exception();
            }
        } catch (Exception e) {
            date = "No date Found";
        }
        try {
            language = j.getString("language");
            if (language.equals("null")) {
                throw new Exception();
            }
        } catch (Exception e) {
            language = "No language Found";
        }
        try {
            duration = j.getString("duration");
            if (duration.equals("null")) {
                throw new Exception();
            }
        } catch (Exception e) {
            duration = "No duration Found";
        }
        try {
            image = j.getString("image");
            if (image.equals("null")) {
                throw new Exception();
            }
        } catch (Exception e) {
            image = "No image Found";
        }
        try {
            JSONArray arr = j.getJSONArray("contents");
            if (arr == null || arr.length() == 0) {
                Log.d(TAG,"contents null or length 0");
                throw new Exception();
            }
            JSONObject temp;
            for (int i = 0;i < arr.length();i++) {
                temp = arr.getJSONObject(i);
                myChapters.add(new Chapter(temp));
            }
        } catch (Exception e) {
            Log.d(TAG,"contents arr error", e);
        }
    }

    public void saveChapters(ArrayList<Chapter> chapters) {
        this.myChapters = chapters;
    }

    protected Audio(Parcel in) {
        title = in.readString();
        author = in.readString();
        date = in.readString();
        language = in.readString();
        duration = in.readString();
        image = in.readString();
        myChapters = in.createTypedArrayList(Chapter.CREATOR);
    }

    public static final Creator<Audio> CREATOR = new Creator<Audio>() {
        @Override
        public Audio createFromParcel(Parcel in) {
            return new Audio(in);
        }

        @Override
        public Audio[] newArray(int size) {
            return new Audio[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    public String getLanguage() {
        return language;
    }

    public String getDuration() {
        return duration;
    }

    public String getImage() {
        return image;
    }

    public ArrayList<Chapter> getMyChapters() {
        return myChapters;
    }

    public Chapter getChapter(int i) {
        return myChapters.get(i);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(author);
        parcel.writeString(date);
        parcel.writeString(language);
        parcel.writeString(duration);
        parcel.writeString(image);
        parcel.writeTypedList(myChapters);
    }

    public JSONObject toJson() {
        JSONObject j = new JSONObject();
        JSONArray arr = new JSONArray();

        try {
            j.put("title", title);
            j.put("author", author);
            j.put("date", date);
            j.put("language", language);
            j.put("duration", duration);
            j.put("image", image);
            for (Chapter chapter : myChapters) {
                arr.put(chapter.toJson());
            }
            j.put("contents", arr);
        } catch (Exception e) {
            Log.d(TAG,"Error converting chapter to Json: ", e);
        }
        return j;
    }

    public void refresh() {
        Chapter chapter;
        for (int i = 0; i < myChapters.size(); i++) {
            chapter = myChapters.get(i);
            chapter.resetVals();
            myChapters.set(i,chapter);
        }
    }


}
