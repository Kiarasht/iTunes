package com.application.itunes;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Album implements Parcelable {
    private String artistName;
    private String releaseDate;
    private String name;
    private String copyright;
    private String contentAdvisoryRating;
    private String artworkUrl;
    private String albumUrl;
    private List<Genre> genres;

    public Album(String artistName, String releaseDate, String name, String copyright,
                 String contentAdvisoryRating, String artworkUrl, String albumUrl, List<Genre> genres) {
        this.artistName = artistName;
        this.releaseDate = releaseDate;
        this.name = name;
        this.copyright = copyright;
        this.contentAdvisoryRating = contentAdvisoryRating;
        this.artworkUrl = artworkUrl;
        this.albumUrl = albumUrl;

        this.genres = genres;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getName() {
        return name;
    }

    public String getCopyright() {
        return copyright;
    }

    public String getContentAdvisoryRating() {
        return contentAdvisoryRating;
    }

    public String getArtworkUrl() {
        return artworkUrl;
    }

    public String getAlbumUrl() {
        return albumUrl;
    }

    @NonNull
    public List<String> getGenresNames() {
        List<String> values = new ArrayList<>();

        if (genres == null)
            return values;

        for (Genre genre : genres)
            values.add(genre.getName());

        return values;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.artistName);
        dest.writeString(this.releaseDate);
        dest.writeString(this.name);
        dest.writeString(this.copyright);
        dest.writeString(this.contentAdvisoryRating);
        dest.writeString(this.artworkUrl);
        dest.writeString(this.albumUrl);
        dest.writeList(this.genres);
    }

    private Album(Parcel in) {
        this.artistName = in.readString();
        this.releaseDate = in.readString();
        this.name = in.readString();
        this.copyright = in.readString();
        this.contentAdvisoryRating = in.readString();
        this.artworkUrl = in.readString();
        this.albumUrl = in.readString();
        this.genres = new ArrayList<>();
        in.readList(this.genres, Genre.class.getClassLoader());
    }

    public static final Creator<Album> CREATOR = new Creator<Album>() {
        @Override
        public Album createFromParcel(Parcel source) {
            return new Album(source);
        }

        @Override
        public Album[] newArray(int size) {
            return new Album[size];
        }
    };
}
