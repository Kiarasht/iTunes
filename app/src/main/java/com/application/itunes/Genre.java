package com.application.itunes;

import android.os.Parcel;
import android.os.Parcelable;

public class Genre implements Parcelable {
    private String genreId;
    private String name;
    private String url;

    public Genre(String genreId, String name, String url) {
        this.genreId = genreId;
        this.name = name;
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.genreId);
        dest.writeString(this.name);
        dest.writeString(this.url);
    }

    private Genre(Parcel in) {
        this.genreId = in.readString();
        this.name = in.readString();
        this.url = in.readString();
    }

    public static final Creator<Genre> CREATOR = new Creator<Genre>() {
        @Override
        public Genre createFromParcel(Parcel source) {
            return new Genre(source);
        }

        @Override
        public Genre[] newArray(int size) {
            return new Genre[size];
        }
    };
}
