package com.application.itunes;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.application.itunes.util.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.application.itunes.util.AlbumConstants.*;
import static com.application.itunes.util.AlbumConstants.LayoutState.*;

public class MainActivity extends Activity implements AlbumAdapter.ListItemClickListener, View.OnClickListener {
    private static final String TAG = Activity.class.getName();
    private static final String KEY_DATA_SET = "key_data_set";
    private List<Album> dataSet = new ArrayList<>();
    private RecyclerView albums;
    private AlbumAdapter adapter;
    private View contentView;
    private View loadingView;
    private View errorView;

    @VisibleForTesting
    public List<Album> getDataSet() {
        return dataSet;
    }

    @Override
    public void onListItemClick(int index) {
        Intent albumInfo = new Intent(this, AlbumInfo.class);
        albumInfo.putExtra(KEY_ALBUM_OBJECT, dataSet.get(index));
        startActivity(albumInfo);
    }

    private void setLayoutState(LayoutState state) {
        contentView.setVisibility(state == LOADED ? View.VISIBLE : View.GONE);
        loadingView.setVisibility(state == LOADING ? View.VISIBLE : View.GONE);
        errorView.setVisibility(state == ERROR ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        albums = findViewById(R.id.album_recycler_view);
        contentView = findViewById(R.id.album_content_parent);
        loadingView = findViewById(R.id.album_loading_parent);
        errorView = findViewById(R.id.album_error_parent);
        findViewById(R.id.error_retry).setOnClickListener(this);

        setLayoutState(LOADING);
        initializeAlbumList();

        if (savedInstanceState == null) {
            adapter.setDataSet(dataSet);
            albums.setAdapter(adapter);
            requestHotAlbums();
        } else {
            dataSet = savedInstanceState.getParcelableArrayList(KEY_DATA_SET);

            if (dataSet == null || dataSet.size() == 0) {
                setLayoutState(ERROR);
                return;
            }

            adapter.setDataSet(dataSet);
            albums.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            setLayoutState(LOADED);
        }
    }

    private void requestHotAlbums() {
        try {
            new ItunesRequest(this).execute(new URL(Uri.parse(ITUNES_URL).toString()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
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
        outState.putParcelableArrayList(KEY_DATA_SET, new ArrayList<Parcelable>(dataSet));
    }

    private void initializeAlbumList() {
        int vertical = LinearLayoutManager.VERTICAL;
        albums.setLayoutManager(new LinearLayoutManager(this, vertical, false));
        albums.setHasFixedSize(true);
        albums.addItemDecoration(new DividerItemDecoration(this, vertical));
        adapter = new AlbumAdapter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.error_retry:
                setLayoutState(LOADING);
                requestHotAlbums();
        }
    }

    private static class ItunesRequest extends AsyncTask<URL, Void, Void> {
        private MainActivity activity;

        public ItunesRequest(MainActivity activity) {
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            activity.setLayoutState(LOADING);
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
                    String albumUrl = anAlbum.getString("url");
                    JSONArray genres = anAlbum.getJSONArray("genres");

                    List<Genre> genreList = new ArrayList<>();
                    for (int j = 0; j < genres.length(); ++j) {
                        JSONObject aGenre = genres.getJSONObject(j);
                        String genreId = aGenre.getString("genreId");
                        String genreName = aGenre.getString("name");
                        String url = aGenre.getString("url");
                        genreList.add(new Genre(genreId, genreName, url));
                    }

                    activity.dataSet.add(new Album(artistName, releaseDate, name, copyright,
                            contentAdvisoryRating, artworkUrl, albumUrl, genreList));
                }

                return null;
            } catch (IOException e) {
                Log.e(TAG, "Unable in grabbing data");
                e.printStackTrace();
            } catch (SecurityException e) {
                Log.e(TAG, "Permission to access internet was denied");
                e.printStackTrace();
            } catch (JSONException e) {
                Log.e(TAG, "Unable in parsing data");
                e.printStackTrace();
            }

            activity.dataSet.clear();
            return null;
        }

        @Override
        protected void onPostExecute(Void ignored) {
            activity.adapter.notifyDataSetChanged();
            activity.setLayoutState(activity.dataSet.size() > 0 ? LOADED : ERROR);
        }
    }
}
