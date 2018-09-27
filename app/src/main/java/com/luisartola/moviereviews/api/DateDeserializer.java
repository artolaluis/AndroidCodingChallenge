package com.luisartola.moviereviews.api;

import android.util.Log;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class DateDeserializer implements JsonDeserializer<Date> {

    public static final String TAG = DateDeserializer.class.getName();

    @Override
    public Date deserialize(JsonElement element, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            String date = element.getAsString();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            return formatter.parse(date);
        } catch (ParseException e) {
            Log.e(TAG, "Unable to parse date.", e);
            return null;
        }
    }
}