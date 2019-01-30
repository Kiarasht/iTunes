package com.application.itunes;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.application.itunes.util.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private static final String TAG = Activity.class.getName();
    private static final String KEY_DATA_SET = "key_data_set";
    private static final String ITUNES_URL = "https://rss.itunes.apple.com/api/v1/us/apple-music/top-albums/all/10/explicit.json";
    private ArrayList<Album> mDataSet = new ArrayList<>();
    private RecyclerView mAlbums;
    private ProgressBar mProgress;
    private AlbumAdapter mAdapter;

    private enum LayoutState {
        LOADED, LOADING, ERROR
    }

    private void setLayoutState(LayoutState state) {
        switch (state) {
            case LOADED:
                break;
            case LOADING:
                break;
            case ERROR:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAlbums = findViewById(R.id.album_recycler_view);
        mProgress = findViewById(R.id.progress_bar);

        setLayoutState(LayoutState.LOADING);
        initializeAlbumList();

        if (savedInstanceState == null) {
            mAdapter.setDataSet(mDataSet);
            mAlbums.setAdapter(mAdapter);
            try {
                new ItunesRequest().execute(new URL(Uri.parse(ITUNES_URL).toString()));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } else {
            mDataSet = savedInstanceState.getParcelableArrayList(KEY_DATA_SET);
            mAdapter.setDataSet(mDataSet);
            mAlbums.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            mProgress.setVisibility(View.GONE);
        }
    }

    /**
     * Save our list of data that we worked to hard to request, parse, and display so that on
     * a configuration change it would still stay with us. Wouldn't be just such a shame to lose
     * a list of such beauty?
     *
     * @param outState Save it in the bundle, we will grab it back onRestoreInstanceState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY_DATA_SET, mDataSet);
    }

    private void initializeAlbumList() {
        int vertical = LinearLayoutManager.VERTICAL;
        mAlbums.setLayoutManager(new LinearLayoutManager(this, vertical, false));
        mAlbums.setHasFixedSize(true);
        mAlbums.setNestedScrollingEnabled(true);
        mAlbums.addItemDecoration(new DividerItemDecoration(this, vertical));
        mAdapter = new AlbumAdapter();
    }

    private class ItunesRequest extends AsyncTask<URL, Void, Void> {
        @Override
        protected void onPreExecute() {
            setLayoutState(LayoutState.LOADING);
        }

        @Override
        protected Void doInBackground(URL... params) {
            try {
                String resultsJSON = NetworkUtils.HttpResponse(params[0]);
                JSONObject albumJSON = new JSONObject(resultsJSON);
                JSONObject feed = albumJSON.getJSONObject("feed");
                JSONArray albumList = feed.getJSONArray("results");

                for (int i = 0; i < albumList.length(); ++i) {
                    JSONObject anAlbum = albumList.getJSONObject(i);
                    String artistName = anAlbum.getString("artistName");
                    String releaseDate = anAlbum.getString("releaseDate");
                    String name = anAlbum.getString("name");
                    String copyright = anAlbum.getString("copyright");
                    String contentAdvisoryRating = anAlbum.getString("contentAdvisoryRating");
                    String artworkUrl = anAlbum.getString("artworkUrl100");
                    JSONArray genres = anAlbum.getJSONArray("genres");

                    List<Genre> genreList = new ArrayList<>();
                    for (int j = 0; j < genres.length(); ++j) {
                        JSONObject aGenre = genres.getJSONObject(j);
                        String genreId = aGenre.getString("genreId");
                        String genreName = aGenre.getString("name");
                        String url = aGenre.getString("url");
                        genreList.add(new Genre(genreId, genreName, url));
                    }

                    mDataSet.add(new Album(artistName, releaseDate, name, copyright,
                            contentAdvisoryRating, artworkUrl, genreList));
                }
            } catch (IOException e) {
                setLayoutState(LayoutState.ERROR);
                Log.e(TAG, "Unable in grabbing data");
                e.printStackTrace();
            } catch (SecurityException e) {
                setLayoutState(LayoutState.ERROR);
                Log.e(TAG, "Permission to access internet was denied");
                e.printStackTrace();
            } catch (JSONException e) {
                setLayoutState(LayoutState.ERROR);
                Log.e(TAG, "Unable in parsing data");
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void ignored) {
            mAdapter.notifyDataSetChanged();
            setLayoutState(LayoutState.LOADED);
        }
    }
}
