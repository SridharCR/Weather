package com.sridhar.weather;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    String CITY = "delhi,in";
    String API = "8118ed6ee68db2debfaaa5a44c832918";

    TextView addressTxt, updated_atTxt, statusTxt, tempTxt, temp_minTxt, temp_maxTxt, sunriseTxt,
            sunsetTxt, windTxt, pressureTxt, humidityTxt;
    ProgressBar progressBar;

    /**
     * Overriding the onCreate()
     * @param savedInstanceState - Application Instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addressTxt = findViewById(R.id.address);
        updated_atTxt = findViewById(R.id.updated_at);
        statusTxt = findViewById(R.id.status);
        tempTxt = findViewById(R.id.temp);
        temp_minTxt = findViewById(R.id.temp_min);
        temp_maxTxt = findViewById(R.id.temp_max);
        sunriseTxt = findViewById(R.id.sunrise);
        sunsetTxt = findViewById(R.id.sunset);
        windTxt = findViewById(R.id.wind);
        pressureTxt = findViewById(R.id.pressure);
        humidityTxt = findViewById(R.id.humidity);

        progressBar = findViewById(R.id.progressBar);

        findViewById(R.id.mainContainer).setVisibility(View.GONE);
        new GetWeatherData().execute();
//        while (!isNetworkAvailable(getApplicationContext())) {
//            this.errorHandling("INTERNET");
//        }

    }

    protected class GetWeatherData extends AsyncTask<String, Void, String> {
        /**
         * Overriding the onPreExecute()
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        /**
         * Returns the JSON response received from the Open Weather API.
         *
         * @param data Supposed to contain the city/geolocation information, not used for now
         * @return String containing the JSON response from open weather api
         */
        protected String doInBackground(String... data) {
            String response = "";
            String url = "https://api.openweathermap.org/data/2.5/weather?q=" + CITY + "&units=metric&appid=" + API;
            response = WeatherData.getData(url);
            return response;
        }

        /**
         * Overriding the onPostExecute()
         * @param result String containing the JSON response from open weather api
         */
        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.GONE);
            this.resultDisplay(result);
        }

//    public static boolean isNetworkAvailable(Context context) {
//        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
//    }

        /**
         * Handles the UI by showing the alert prompts
         * Invoke this method when in terms of error handling
         * @param error String title for the alert prompt
         */
        protected void errorHandling(String error) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
            alertDialogBuilder.setCancelable(true);
            alertDialogBuilder.setTitle(error);
            alertDialogBuilder.setMessage("An internal error occurred, will be fixed in further update");

            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            });
            alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.cancel();
                }
            });
            alertDialogBuilder.show();
        }

        /**
         * Displays the JSON response in correspondence to the UI
         * In case of exception, errorHandling() is called
         * @param result String response contains the weather data
         */
        protected void resultDisplay(String result) {
            try {
                if (result == "")
                    this.errorHandling("No response Error");
                else {
                    JSONObject jsonObj = new JSONObject(result);
                    JSONObject main = jsonObj.getJSONObject("main");
                    JSONObject sys = jsonObj.getJSONObject("sys");
                    JSONObject wind = jsonObj.getJSONObject("wind");
                    JSONObject weather = jsonObj.getJSONArray("weather").getJSONObject(0);

                    Long updatedAt = jsonObj.getLong("dt");
                    String updatedAtText = "Updated at: " + new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(new Date(updatedAt * 1000));
                    String temp = main.getString("temp") + "°C";
                    String tempMin = "Min Temp: " + main.getString("temp_min") + "°C";
                    String tempMax = "Max Temp: " + main.getString("temp_max") + "°C";
                    String pressure = main.getString("pressure");
                    String humidity = main.getString("humidity");

                    Long sunrise = sys.getLong("sunrise");
                    Long sunset = sys.getLong("sunset");
                    String windSpeed = wind.getString("speed");
                    String weatherDescription = weather.getString("description");

                    String address = jsonObj.getString("name") + ", " + sys.getString("country");

                    addressTxt.setText(address);
                    updated_atTxt.setText(updatedAtText);
                    statusTxt.setText(weatherDescription.toUpperCase());
                    tempTxt.setText(temp);
                    temp_minTxt.setText(tempMin);
                    temp_maxTxt.setText(tempMax);
                    sunriseTxt.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunrise * 1000)));
                    sunsetTxt.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunset * 1000)));
                    windTxt.setText(windSpeed);
                    pressureTxt.setText(pressure);
                    humidityTxt.setText(humidity);

                    findViewById(R.id.mainContainer).setVisibility(View.VISIBLE);
                }

            } catch (JSONException e) {
                this.errorHandling("Response processing error");

            }

        }
    }

}