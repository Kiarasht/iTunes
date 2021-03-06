package com.application.itunes;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.itunes.util.DateUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumAdapterViewHolder> {
    private List<Album> dataSet;
    private final ListItemClickListener mOnListItemClickListener;

    AlbumAdapter(ListItemClickListener mOnListItemClickListener) {
        this.mOnListItemClickListener = mOnListItemClickListener;
    }

    interface ListItemClickListener {
        void onListItemClick(int index);
    }

    @NonNull
    @Override
    public AlbumAdapter.AlbumAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new AlbumAdapterViewHolder(LayoutInflater.from(
                viewGroup.getContext()).inflate(R.layout.layout_album_row, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final AlbumAdapter.AlbumAdapterViewHolder viewHolder, int i) {
        Album album = dataSet.get(i);

        Picasso.get().load(album.getArtworkUrl()).into(viewHolder.albumCover, picassoCallback(viewHolder));
        viewHolder.artistName.setText(album.getArtistName());
        viewHolder.albumName.setText(album.getName());
        Date time = DateUtils.parseDate(album.getReleaseDate());
        viewHolder.releaseDate.setText(DateUtils.getDateDifference(time.getTime()));
    }

    private Callback picassoCallback(final AlbumAdapterViewHolder viewHolder) {
        return new Callback() {
            @Override
            public void onSuccess() {
                viewHolder.albumCoverProgress.setVisibility(View.GONE);
                viewHolder.albumCover.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(Exception e) {

            }
        };
    }

    @Override
    public int getItemCount() {
        return dataSet != null ? dataSet.size() : 0;
    }

    void setDataSet(List<Album> dataSet) {
        this.dataSet = dataSet;
    }

    public class AlbumAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final View albumCoverProgress;
        private final ImageView albumCover;
        private final TextView artistName;
        private final TextView releaseDate;
        private final TextView albumName;
        //private final TextView copyRights;
        //private final TextView advisoryRating;
        //private final TextView genres;

        AlbumAdapterViewHolder(@NonNull View view) {
            super(view);

            view.setOnClickListener(this);
            albumCoverProgress = view.findViewById(R.id.album_cover_progress);
            albumCover = view.findViewById(R.id.album_cover);
            albumName = view.findViewById(R.id.album);
            artistName = view.findViewById(R.id.artist);
            releaseDate = view.findViewById(R.id.release_date);
        }

        @Override
        public void onClick(View v) {
            mOnListItemClickListener.onListItemClick(getAdapterPosition());
        }
    }
}
