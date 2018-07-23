package com.example.android.pakistansportsnews;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsArticle>> {

    public static final String LOG_TAG = MainActivity.class.getName();

    private TextView emptyStateTextView;

    public NewsArticleAdapter adapter;

    String url = "https://content.guardianapis.com/search?section=sport&order-by=newest&use-date=published&q=pakistan&api-key=36459d32-5d88-4881-8e8a-b61a69ae6968";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new NewsArticleAdapter(this, new ArrayList<NewsArticle>());

        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected news article.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
                NewsArticle currentNewsArticle = adapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri newsArticleUri = Uri.parse(currentNewsArticle.getUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsArticleUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        emptyStateTextView = (TextView) findViewById(R.id.text_view);
        listView.setEmptyView(emptyStateTextView);

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(0, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            emptyStateTextView.setText(R.string.no_connection);
        }

    }

    @Override
    public Loader<List<NewsArticle>> onCreateLoader ( int i, Bundle bundle){
        // Create a new loader for the given URL
        return new NewsArticleLoader(this, url);
    }

    @Override
    public void onLoadFinished
            (Loader < List < NewsArticle >> loader, List < NewsArticle > newsArticles){
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No news articles found."
        emptyStateTextView.setText(R.string.no_articles);

        // Clear the adapter of previous news article data
        adapter.clear();

        // If there is a valid list of {@link NewsArticle}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (newsArticles != null && !newsArticles.isEmpty()) {
            adapter.addAll(newsArticles);
        }
    }

    @Override
    public void onLoaderReset (Loader < List < NewsArticle >> loader) {
        // Loader reset, so we can clear out our existing data.
        adapter.clear();
    }

}







