package com.sridhar.weather;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import android.util.Log;

public class WeatherData {
    public static String getData(String targetURL) {
        URL url;
        HttpURLConnection connection = null;
        try {
            Log.d("14", "14 Entering");
            url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            Log.d("19", "status");

            InputStream inputStream;
            int status = connection.getResponseCode();
            Log.d("22", "sdew");
            if (status != HttpURLConnection.HTTP_OK)
                inputStream = connection.getErrorStream();
            else
                inputStream = connection.getInputStream();

            BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
