package com.application.itunes;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumAdapterViewHolder> {
    private List<Album> dataSet;

    @NonNull
    @Override
    public AlbumAdapter.AlbumAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new AlbumAdapterViewHolder(LayoutInflater.from(
                viewGroup.getContext()).inflate(R.layout.layout_album_row, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumAdapter.AlbumAdapterViewHolder viewHolder, int i) {
        Album album = dataSet.get(i);

        viewHolder.artistName.setText(album.getArtistName());
        viewHolder.albumName.setText(album.getName());
    }

    @Override
    public int getItemCount() {
        return dataSet != null ? dataSet.size() : 0;
    }

    public void setDataSet(ArrayList<Album> dataSet) {
        this.dataSet = dataSet;
    }

    public class AlbumAdapterViewHolder extends RecyclerView.ViewHolder {
        private final ImageView albumCover;
        private final TextView artistName;
        //private final TextView releaseDate;
        private final TextView albumName;
        //private final TextView copyRights;
        //private final TextView advisoryRating;
        //private final TextView genres;

        public AlbumAdapterViewHolder(@NonNull View view) {
            super(view);

            albumCover = view.findViewById(R.id.album_cover);
            albumName = view.findViewById(R.id.album);
            artistName = view.findViewById(R.id.artist);
        }
    }
}
