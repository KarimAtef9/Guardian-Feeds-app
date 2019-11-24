package com.example.guardianfeeds;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class FeedsAdapter extends ArrayAdapter<Feed> {
    private ArrayList<Feed> feeds;
    public FeedsAdapter(Context context, ArrayList<Feed> feedsList) {
        super(context, 0, feedsList);
        this.feeds = feedsList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.feeds_list, parent, false);
        }

        TextView category = listItemView.findViewById(R.id.category);
        TextView title = listItemView.findViewById(R.id.title);
        TextView date = listItemView.findViewById(R.id.date);

        Feed currentFeed = feeds.get(position);

        category.setText(currentFeed.getCategory());
        title.setText(currentFeed.getTitle());
        date.setText(currentFeed.getDate());

        return listItemView;
    }
}
