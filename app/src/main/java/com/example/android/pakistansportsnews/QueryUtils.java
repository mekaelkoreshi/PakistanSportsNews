package com.example.android.pakistansportsnews;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by mekaelkoreshi on 23.07.2018.
 * Helper methods related to requesting and recieving news stories from The Guardian API
 */

public class QueryUtils {


    private static final String LOG_TAG = QueryUtils.class.getName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Return a list of {@link NewsArticle} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<NewsArticle> extractFeatureFromJson(String newsArticleJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsArticleJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding News Articles to
        ArrayList<NewsArticle> newsArticles = new ArrayList<>();

        // Try to parse the JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Store the root object in a JSON object
            JSONObject root = new JSONObject(newsArticleJSON);

            // Store the response object in a JSON object
            JSONObject response = root.getJSONObject("response");

            // Store the results object in a JSON array
            JSONArray results = response.getJSONArray("results");

            // Set up counter for total number of News Articles
            int numberOfNewsArticles = results.length();

            // Set up loop to parse data from JSON and add store in variables
            for (int i = 0; i < numberOfNewsArticles; i++) {

                // Store the data needed for a NewsArticle in a JSON Object
                JSONObject newsArticleObject = results.getJSONObject(i);

                // Store the Article's Name (and Author's Name if available) in a variable
                String articleNameAuthorName = newsArticleObject.getString("webTitle");

                // Store the Section in a variable
                String section = newsArticleObject.getString("sectionName");

                // Store the date and time in a variable
                String dateTime = newsArticleObject.getString("webPublicationDate");

                // Store the article URL in a variable
                String url = newsArticleObject.getString("webUrl");

                // Declare individual variables for Author and Article Name
                String articleName = "";
                String authorName = "";

                // Declare individual variables for Date and Time
                String date = "";
                String time = "";

                /**
                 * Split the Article's Name and Author's Name if Author's Name is available, and
                 * store them in different variables
                 **/
                if (articleNameAuthorName.contains(" \\| ")) {
                    String[] parts = articleNameAuthorName.split("\\|");
                    articleName = parts[0];
                    authorName = parts[1];
                } else {
                    articleName = articleNameAuthorName;
                    authorName = "";
                }

                Log.e(LOG_TAG, articleName);
                Log.e(LOG_TAG, authorName);

                // Split the Date and Time and store in separate variables
                if (dateTime.contains("T")) {
                    String[] parts2 = dateTime.split("(T)", 2);
                    date = parts2[0];
                    Log.e(LOG_TAG, date);
                    time = parts2[1];

                    // Remove extra character at the end of time string
                    time = time.substring(0, time.length() - 1);
                    Log.e(LOG_TAG, time);

//                    // Format the Date and update date variable
//                    Date date1 = new Date(date);
//                    SimpleDateFormat dateFormatter = new SimpleDateFormat("LLL DD, yyyy");
//                    date = dateFormatter.format(date1);
//
//                    // Format the Time and update time variable
//                    Date time1 = new Date(time);
//                    SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a");
//                    time = timeFormatter.format(time1);
                } else {
                    date = "";
                    time = "";
                }

                newsArticles.add(new NewsArticle(articleName, authorName, section, date, time, url));
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return newsArticles;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Query the Guardian dataset and return a list of {@link NewsArticle} objects.
     */
    public static List<NewsArticle> fetchNewsArticleData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Earthquake}s
        List<NewsArticle> newsArticles = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link NewsArticle}s
        return newsArticles;
    }
}