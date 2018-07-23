package com.example.android.pakistansportsnews;

import java.net.URL;

/**
 * Created by mekaelkoreshi on 23.07.2018.
 * Class for a NewsArticle Object
 */

public class NewsArticle {

    // Variable to hold the Article's Name
    private String mArticleName;

    // Variable to hold the Author's Name
    private String mAuthorName;

    // Variable to hold the Section
    private String mSection;

    // Variable to hold the date
    private String mDate;

    // Variable to hold the time
    private String mTime;

    // Variable to hold the URL
    private String mUrl;

    // Constructor for NewsArticle Object
    public NewsArticle(String articleName, String authorName, String section, String date, String time, String url) {
        mArticleName = articleName;
        mAuthorName = authorName;
        mSection = section;
        mDate = date;
        mTime = time;
        mUrl = url;
    }

    // Method for retrieving Article Name
    public String getArticleName() {return mArticleName ;}

    // Method for retrieving Author Name
    public String getAuthorName() {return mAuthorName;}

    // Method for retrieving Section
    public String getSection() {return mSection;}

    // Method for retrieving Date
    public String getDate() {return mDate;}

    // Method for retrieving Time
    public String getTime() {return mTime;}

    // Method for retrieving URL
    public String getUrl() {return mUrl;}

}
