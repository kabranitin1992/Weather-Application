package com.example.root.appweather_imobile3;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.app.classes.GPSTracker;
import com.app.classes.JSONParser;
import com.app.classes.Weather;

import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    double lat, lon;
    EditText edtZip;
    List<Address> addresses;
    ListView lstWeatherData;
    Button btnSearch;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
//Meun Switch
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                edtZip.setText("");
                getData();
                return true;
            case R.id.exit:
                System.exit(0);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_home);

        android.app.ActionBar actionBar = getActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(
                Color.parseColor("#373836"));
        btnSearch = (Button) findViewById(R.id.btnSearchWeather);
        edtZip = (EditText) findViewById(R.id.edtZip);
        lstWeatherData = (ListView) findViewById(R.id.lstWeatherData);

        getData();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });
    }
//Checking for Internet
    private boolean IsConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf = cm.getActiveNetworkInfo();
        if (nf != null && nf.isConnected()) {
            return true;
        }
        return false;
    }

    private class downloadData extends AsyncTask<String, Void, Weather> {
//Getting the Data from API
        @Override
        protected Weather doInBackground(String... params) {
            // TODO Auto-generated method stub
            HttpURLConnection con;
            try {
                con = (HttpURLConnection) (new URL("http://api.openweathermap.org/data/2.5/weather?zip=" + params[0] + "&APPID=4fd5be1a68b62fad3a30873028b26d1f")).openConnection();
                con.setRequestMethod("GET");
                con.connect();
                int statusCode = con.getResponseCode();
                if (statusCode == HttpURLConnection.HTTP_OK) {
                    InputStream in = con.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line = bufferedReader.readLine();
                    while (line != null) {
                        stringBuilder.append(line);
                        line = bufferedReader.readLine();
                    }
                    JSONParser jsonParser = new JSONParser();

                    try {
                        return jsonParser.PasreJSON(stringBuilder.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                System.out.println(e);
                e.printStackTrace();
            }

            return null;
        }
//Populating the ListView
        @Override
        protected void onPostExecute(Weather result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            SimpleDateFormat simpleDateformat = new SimpleDateFormat("EEEE");
            result.setDay(simpleDateformat.format(new Date()));
            String[] values = {"Current Condition: " + result.getCurrentCondition(), "Current Place: " + result.getCurrentPlace(),
                    "Today's Day: " + result.getDay(), "Day High: " + String.valueOf(result.getDayHigh())+" C",
                    "Day Low: " + String.valueOf(result.getDayLow())+" C"};
            ArrayAdapter adapter = new ArrayAdapter<String>(HomeActivity.this, android.R.layout.simple_list_item_1, values);
            lstWeatherData.setAdapter(adapter);
            //adapter.setAdapter(values);
            Log.d("demo", result.getDay());

        }

    }
// Method to get Current Location using GeoCoder
    private void getData() {
        String zipCode = edtZip.getText().toString();
        downloadData task = new downloadData();
        //If zip code not entered
        if (zipCode.equals("")) {

            GPSTracker gps = new GPSTracker(HomeActivity.this);
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {

                if (gps.canGetLocation()) {

                    double lat = gps.getLatitude();
                    double lon = gps.getLongitude();
                    addresses = geocoder.getFromLocation(lat, lon, 1);
                    if (addresses.size() > 0) {
                        zipCode = addresses.get(0).getPostalCode();
                        task.execute(zipCode);
                    } else {
                        Toast.makeText(HomeActivity.this, "No GPS Connection",
                                Toast.LENGTH_LONG).show();
                        zipCode="28262";
                        task.execute(zipCode);
                        ;
                    }
                    if (!IsConnected()) {
                        Toast.makeText(HomeActivity.this, "No Network Connection",
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(HomeActivity.this, "No GPS Connection",
                            Toast.LENGTH_LONG).show();
                    zipCode="28262";
                    task.execute(zipCode);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //if zipCode Entered
        else {
            if(zipCode.length()>=5)
            task.execute(zipCode);
            else {
                Toast.makeText(HomeActivity.this, "Enter Valid ZipCode",
                        Toast.LENGTH_LONG).show();
                zipCode="28262";
                task.execute(zipCode);
            }
        }
    }

}
