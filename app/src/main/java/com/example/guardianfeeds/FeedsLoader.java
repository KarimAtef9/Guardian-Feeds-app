package com.example.guardianfeeds;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;

public class FeedsLoader extends AsyncTaskLoader<ArrayList<Feed>> {
    private String url;

    public FeedsLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    public ArrayList<Feed> loadInBackground() {
        ArrayList<Feed> feeds = QueryUtils.extractFeeds(url);
        return feeds;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}
