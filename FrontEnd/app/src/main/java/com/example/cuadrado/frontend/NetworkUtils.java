package com.example.cuadrado.frontend;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by cuadrado on 09/06/2017.
 */

public class NetworkUtils {
    final static String GITHUB_BASE_URL =
            "https://api.github.com/search/repositories";

    final static String PARAM_QUERY = "q";

    /*
     * The sort field. One of stars, forks, or updated.
     * Default: results are sorted by best match if no field is specified.
     */
    final static String PARAM_SORT = "sort";
    final static String sortBy = "stars";

    public static URL buildUrl(String githubSearchQuery) {
        Uri builtUri = Uri.parse(GITHUB_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_QUERY, githubSearchQuery)
                .appendQueryParameter(PARAM_SORT, sortBy)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
