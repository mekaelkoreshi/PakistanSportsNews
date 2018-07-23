package com.example.android.pakistansportsnews;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mekaelkoreshi on 23.07.2018.
 */

public class NewsArticleAdapter extends ArrayAdapter<NewsArticle> {

    // Public constructor for the NewsArticleAdapter Class
    public NewsArticleAdapter(Context context, ArrayList<NewsArticle> newsArticles) {
        super(context, 0, newsArticles);
    }

    // Implements abstract methods
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Store the current list item in a variable
        final NewsArticle currentListItem = getItem(position);

        // Store the url in a String
        String url = currentListItem.getUrl();

        // Find the appropriate text views and populate them with data from the Guardian API
        TextView articleNameTextView = (TextView) listItem.findViewById(R.id.article_title_text_view);
        articleNameTextView.setText(currentListItem.getArticleName());

        TextView authorNameTextView = (TextView) listItem.findViewById(R.id.author_text_view);
        authorNameTextView.setText(currentListItem.getAuthorName());

        TextView sectionTextView = (TextView) listItem.findViewById(R.id.section_text_view);
        sectionTextView.setText(currentListItem.getSection());

        TextView dateTextView = (TextView) listItem.findViewById(R.id.date_text_view);
        dateTextView.setText(currentListItem.getDate());

        TextView timeTextView = (TextView) listItem.findViewById(R.id.time_text_view);
        timeTextView.setText(currentListItem.getTime());

        return listItem;
    }
}
