package com.application.itunes.util;

public class AlbumConstants {
    public static final String ITUNES_URL = "https://rss.itunes.apple.com/api/v1/us/apple-music/top-albums/all/10/explicit.json";
    public static final String KEY_ALBUM_OBJECT = "album_object";

    public enum LayoutState {
        LOADED, LOADING, ERROR
    }
}
