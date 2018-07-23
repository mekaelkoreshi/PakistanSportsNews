package com.example.android.pakistansportsnews;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mekaelkoreshi on 23.07.2018.
 */

public class NewsArticleLoader extends AsyncTaskLoader<List<NewsArticle>> {
    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = NewsArticleLoader.class.getName();

    /**
     * Query URL
     */
    private String mUrl;

    /**
     * Constructs a new {@link NewsArticleLoader}.
     *
     * @param context of the activity
     * @param url     to load data from
     */
    public NewsArticleLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<NewsArticle> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of earthquakes.
        List<NewsArticle> newsArticles = QueryUtils.fetchNewsArticleData(mUrl);
        return newsArticles;
    }
}
