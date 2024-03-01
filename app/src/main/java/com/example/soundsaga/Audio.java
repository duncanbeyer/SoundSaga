package com.example.soundsaga;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Audio {
    private static final String TAG = "Audio";
    private String title;
    private String author;
    private String date;



    private String language;
    private String duration;
    private String image;
    private ArrayList<Chapter> chapters = new ArrayList<>();


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
                chapters.add(new Chapter(temp));
            }
        } catch (Exception e) {
            Log.d(TAG,"contents arr error", e);
        }
    }

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

    public ArrayList<Chapter> getChapters() {
        return chapters;
    }

    public Chapter getChapter(int i) {
        return chapters.get(i);
    }

    public class Chapter {

        private int number;
        private String title;
        private String url;
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


        public int getNumber() {
            return number;
        }

        public String getTitle() {
            return title;
        }

        public String getUrl() {
            return url;
        }
    }

}
