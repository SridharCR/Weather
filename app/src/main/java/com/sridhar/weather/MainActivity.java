package com.sridhar.weather;

import android.accounts.NetworkErrorException;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

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

        findViewById(R.id.mainContainer).setVisibility(View.GONE);

        while (!isNetworkAvailable(getApplicationContext())){
            this.errorHandling("INTERNET");
        }
        Log.d("INTERNET", "Success");
        String response = this.getWeatherData();
        Log.d("RESULT", response);
//
//        this.resultDisplay(response);
//        Log.d("RESULT", "display done");


    }
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    protected void errorHandling(String error) {
        Log.d("DIALOG", "Entering");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setTitle("Error occurred");
        alertDialogBuilder.setMessage("An internal error occurred, will be fixed in further update");

        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichButton){
            }
        });
        alertDialogBuilder.setPositiveButton("Cancel", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichButton){
                dialog.cancel();
            }
        });
        Log.d("DIALOG", "Exiting");
    }

    protected String getWeatherData(String... args) {
        String response = "";
            String url = "https://api.openweathermap.org/data/2.5/weather?q=" + CITY + "&units=metric&appid=" + API;
            response = WeatherData.getData(url);
            return response;
    }

    protected void resultDisplay(String result){
        try {
            Log.d("RESULT", result);
            if (result == "None")
                Log.d("","");
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
                String weatherMain = weather.getString("main");
                String weatherDescription = weather.getString("description");

                String address = jsonObj.getString("name") + ", " + sys.getString("country");

                /* Populating extracted data into our views */
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

                /* Views populated, Hiding the loader, Showing the main design */
                findViewById(R.id.mainContainer).setVisibility(View.VISIBLE);
            }

        } catch (JSONException e) {
            Log.d("","");
        }

    }


}