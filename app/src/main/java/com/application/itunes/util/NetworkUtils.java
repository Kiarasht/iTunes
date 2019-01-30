package com.application.itunes.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * A helper class that deals with sending and processing network requests
 */
public class NetworkUtils {
    /**
     * Process HTTP responses using scanner which will tokenize the steam. A "\A" represents the
     * beginning of the stream affectively causing the whole stream to be read in the next token stream.
     *
     * @param url Used to open an HTTP connection
     * @return String object representing the data that was just processed
     * @throws IOException If an I/O error occurs while creating the input stream.
     */
    public static String HttpResponse(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        InputStream inputStream;

        inputStream = urlConnection.getInputStream();
        try (Scanner scanner = new Scanner(inputStream)) {
            scanner.useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        } finally {
            urlConnection.disconnect();
        }
    }
}
