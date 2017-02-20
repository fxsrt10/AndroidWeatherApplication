package sample.optech.weatherapp;

import android.icu.text.DateFormat;
import android.icu.text.DecimalFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Date;

import data.JSONWeatherParser;
import data.WeatherHTTPClient;
import model.Weather;

public class MainActivity extends AppCompatActivity {

    private TextView cityName;
    private TextView temp;
    private ImageView iconView;
    private TextView humidity;
    private TextView pressure;
    private TextView wind;
    private TextView sunset;
    private TextView sunrise;
    private TextView updated;
    private TextView description;

    Weather weather = new Weather();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityName = (TextView) findViewById(R.id.cityText);
        temp = (TextView) findViewById(R.id.tempText);
        iconView = (ImageView) findViewById(R.id.thumbnailIcon);
        humidity = (TextView) findViewById(R.id.humidityText);
        pressure = (TextView) findViewById(R.id.pressureText);
        wind = (TextView) findViewById(R.id.windCatText);
        sunset = (TextView) findViewById(R.id.sunsetText);
        sunrise = (TextView) findViewById(R.id.sunriseText);
        updated = (TextView) findViewById(R.id.lastUpdatedText);
//        description = (TextView) findViewById(R.id.description);

        renderWeatherData("Houston,US");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void renderWeatherData(String city) {
        WeatherTask weatherTask = new WeatherTask();
        weatherTask.execute(new String[]{city});

    }

    private class WeatherTask extends AsyncTask<String, Void, Weather> {

        @Override
        protected Weather doInBackground(String... params) {
            String data = ( (new WeatherHTTPClient()).getWeatherData(params[0] ));

            weather = JSONWeatherParser.getWeather(data);

            Log.v("Data :", weather.place.getCity());

            return weather;
        }

        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);

            DateFormat df = DateFormat.getDateTimeInstance();

            String sunriseData = df.format(new Date(weather.place.getSunrise()));
            String sunsetData = df.format(new Date(weather.place.getSunset()));
            String updateData = df.format(new Date(weather.place.getLastUpdate()));

            DecimalFormat decimalFormat = new DecimalFormat("#.#");
            String tempFormat = decimalFormat.format(weather.currentCondition.getTemperature());

            cityName.setText(weather.place.getCity() + ", " + weather.place.getCountry());
            humidity.setText("Humidity: " + String.valueOf(weather.currentCondition.getHumidity()) + "%");
            temp.setText(" "+ tempFormat + "C");
            pressure.setText("Pressure: " + weather.currentCondition.getPressure() + "psi");
            wind.setText("Wind: " + weather.wind.getSpeed() + "mph");
            sunrise.setText("Sunrise: " + sunriseData);
            sunset.setText("Sunset: " + sunsetData);
            updated.setText("Last Updated: " + updateData);
//            description.setText(weather.currentCondition.getDescription());


        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.addNewCity) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
