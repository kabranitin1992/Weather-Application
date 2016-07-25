package com.app.classes;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.JsonReader;
import android.util.Log;
import android.util.Xml;

public class JSONParser{


    public Weather PasreJSON(String toParse) throws JSONException, MalformedURLException {
//Parsing JOSN data
        Weather currentWeather = new Weather();
        JSONObject jsonRootObject = new JSONObject(toParse);
        JSONArray weatherArray = jsonRootObject.getJSONArray("weather");
        JSONObject weatherObject = (JSONObject) weatherArray.get(0);
        currentWeather.setCurrentCondition(weatherObject.getString("main"));

        JSONObject highLow = jsonRootObject.getJSONObject("main");
        currentWeather.setDayHigh(Float.parseFloat(highLow.getString("temp_max"))-273);
        currentWeather.setDayLow(Float.parseFloat(highLow.getString("temp_min"))-273);
        currentWeather.setCurrentPlace(jsonRootObject.getString("name"));
        return currentWeather;
    }

}

