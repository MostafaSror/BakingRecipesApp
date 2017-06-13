package com.example.moustafamamdouh.bakingrecipes.Services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Moustafa.Mamdouh on 6/10/2017.
 */

public class FetchInternetDataService  extends IntentService {

    public static final String ACTION_MyIntentService = "com.example.moustafamamdouh.bakingrecipes.Services.RESPONSE";
    public static final String EXTRA_KEY_OUT = "EXTRA_OUT";

    public FetchInternetDataService() {
        super("FetchInternetDataService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        URL url = null;
        try {
            url = new URL("https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            String jsonResponse =getResponseFromHttpUrl(url);
            Intent intentResponse = new Intent();
            intentResponse.setAction(ACTION_MyIntentService);
            intentResponse.addCategory(Intent.CATEGORY_DEFAULT);
            intentResponse.putExtra(EXTRA_KEY_OUT, jsonResponse);
            sendBroadcast(intentResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
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

