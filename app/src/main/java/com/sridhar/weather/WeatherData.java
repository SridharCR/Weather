package com.sridhar.weather;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherData {
    public static String getData(String targetURL) {
        URL url;
        HttpURLConnection connection = null;
//        return null;
//        return "{\"coord\":{\"lon\":77.2167,\"lat\":28.6667},\"weather\":[{\"id\":721,\"main\":\"Haze\",\"description\":\"haze\",\"icon\":\"50d\"}],\"base\":\"stations\",\"main\":{\"temp\":37.05,\"feels_like\":39.1,\"temp_min\":37.05,\"temp_max\":37.05,\"pressure\":1002,\"humidity\":34},\"visibility\":4000,\"wind\":{\"speed\":2.83,\"deg\":272,\"gust\":5.67},\"clouds\":{\"all\":20},\"dt\":1622977882,\"sys\":{\"type\":1,\"id\":9165,\"country\":\"IN\",\"sunrise\":1622937178,\"sunset\":1622987215},\"timezone\":19800,\"id\":1273294,\"name\":\"Delhi\",\"cod\":200}";
        try {
            url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            InputStream inputStream;
            int status = connection.getResponseCode();
            Log.i("Backend Response code", Integer.toString(status));
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
