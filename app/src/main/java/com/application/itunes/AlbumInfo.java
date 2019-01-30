package com.application.itunes;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.itunes.util.DateUtils;
import com.squareup.picasso.Picasso;

import java.util.Date;

import static com.application.itunes.util.AlbumConstants.KEY_ALBUM_OBJECT;

public class AlbumInfo extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_info_layout);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null && bundle.getParcelable(KEY_ALBUM_OBJECT) != null) {
            Album album = bundle.getParcelable(KEY_ALBUM_OBJECT);

            if (album == null) {
                return;
            }

            Date time = DateUtils.parseDate(album.getReleaseDate());
            ((TextView) findViewById(R.id.date_value)).setText(DateUtils.getDateDifference(time.getTime()));
            ((TextView) findViewById(R.id.album_value)).setText(album.getName());
            ((TextView) findViewById(R.id.artist_value)).setText(album.getArtistName());
            ((TextView) findViewById(R.id.genre_value)).setText(TextUtils.join(", ", album.getGenresNames()));
            ((TextView) findViewById(R.id.copyright)).setText(album.getCopyright());
            Picasso.get().load(album.getArtworkUrl()).into(((ImageView) findViewById(R.id.album_info_cover)));
        }
    }
}
