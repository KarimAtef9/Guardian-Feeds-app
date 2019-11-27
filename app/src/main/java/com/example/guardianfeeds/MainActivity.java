package com.example.guardianfeeds;

import androidx.appcompat.app.AppCompatActivity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<ArrayList<Feed>> {
    public static final String LOG_TAG = MainActivity.class.getName();
    private int loaderId = 0;
    private ListView feedsListView;
    private FeedsAdapter feedsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ProgressBar progressBar = findViewById(R.id.loading_spinner);
        progressBar.setVisibility(View.INVISIBLE);
        TextView textView = findViewById(R.id.no_results);
        textView.setText(R.string.intro);
        feedsAdapter = new FeedsAdapter(this, new ArrayList<Feed>());
        final LoaderManager.LoaderCallbacks<ArrayList<Feed>> object = this;

        Button button = findViewById(R.id.search_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedsAdapter.clear();
                Log.i(LOG_TAG, "Search Button Clicked.");
                //checking for internet connection first
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                boolean isConnected = (cm.getActiveNetworkInfo() != null);

                if (isConnected) {
                    ProgressBar progressBar = findViewById(R.id.loading_spinner);
                    progressBar.setVisibility(View.VISIBLE);
                    TextView textView = findViewById(R.id.no_results);
                    textView.setText("");
                    // Get a reference to the LoaderManager, in order to interact with loaders.
                    android.app.LoaderManager loaderManager = getLoaderManager();
                    // Initialize the loader. Pass in the int ID constant defined above and pass in null for
                    // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
                    // because this activity implements the LoaderCallbacks interface).
                    loaderManager.initLoader(loaderId, null, object);
                    Log.v(LOG_TAG, "Loader manager initiated");
                } else {
                    TextView emptyView = findViewById(R.id.no_results);
                    emptyView.setText(R.string.no_internet);
                    ProgressBar progressBar = findViewById(R.id.loading_spinner);
                    progressBar.setVisibility(View.INVISIBLE);
                }

                feedsListView = findViewById(R.id.books_list);
                feedsListView.setAdapter(feedsAdapter);


                feedsListView.setEmptyView(findViewById(R.id.no_results));
            }
        });

        feedsListView = findViewById(R.id.books_list);
        feedsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Feed currentFeed = feedsAdapter.getItem(position);
                String url = currentFeed.getUrl();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
    }

    @Override
    public Loader<ArrayList<Feed>> onCreateLoader(int i, Bundle bundle) {
        Log.v(LOG_TAG, "FeedLoader created in onCreateLoader");
        String url = makeUrl();
        return new FeedsLoader(this, url);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Feed>> loader, ArrayList<Feed> feeds) {
        // Clear the feedsAdapter of previous feeds data
        feedsAdapter.clear();
        // If there is a valid list of {@link feeds}, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (feeds != null && !feeds.isEmpty()) {
            feedsAdapter.addAll(feeds);
        }
        TextView emptyView = findViewById(R.id.no_results);
        emptyView.setText(R.string.no_results);
        ProgressBar progressBar = findViewById(R.id.loading_spinner);
        progressBar.setVisibility(View.INVISIBLE);
        Log.v(LOG_TAG, "feedsAdapter updated in onLoadFinished");
        getLoaderManager().destroyLoader(loaderId);

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Feed>> loader) {
        feedsAdapter.clear();
        Log.v(LOG_TAG, "adapter cleared in onLoaderReset");
    }

    private String makeUrl() {
        String url = "http://content.guardianapis.com/search?";
        EditText searchTitle = findViewById(R.id.content_search);
        EditText searchCategory = findViewById(R.id.category_search);

        Uri baseUri = Uri.parse(url);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        if (searchTitle.getText() != null && searchTitle.getText().length() > 0) {
            String text = String.valueOf(searchTitle.getText()).replaceAll(" ", "+");
            uriBuilder.appendQueryParameter("q", text);
        }

        if (searchCategory.getText() != null && searchCategory.getText().length() > 0) {
            String text = String.valueOf(searchCategory.getText()).replaceAll(" ", "+");
            uriBuilder.appendQueryParameter("tag", text+"/"+text);
        }
        uriBuilder.appendQueryParameter("api-key","b10b89cb-f25b-41cf-88d5-636839d93e64");
        url = uriBuilder.toString();
        Log.i("MainActivity.java", "from makeUrl method the new url is : " + url);
        return url;
    }

}
