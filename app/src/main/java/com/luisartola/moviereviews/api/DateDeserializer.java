/**
 * Copyright 2018. Luis Artola. All rights reserved.
 */

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

/**
 * Deserialize short and long format string dates that lack timezone.
 */
public class DateDeserializer implements JsonDeserializer<Date> {

    public static final String TAG = DateDeserializer.class.getName();

    /**
     * Deserialize short and long format string dates that lack timezone.
     * Return date in UTC.
     *
     * Sample string dates returned by the REST API:
     * <ul>
     *  <li>2018-09-26 10:11:12</li>
     *  <li>2018-09-26</li>
     * </ul>
     *
     * @param element   String date returned by the REST API endpoint.
     * @param typeOfT   Data type of field being parsed.
     * @param context
     * @return {@link Date} instance in UTC.
     * @throws JsonParseException
     */
    @Override
    public Date deserialize(JsonElement element, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            String date = element.getAsString();
            SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formatter = dateFormatter;
            if (date.length() > 10) {
                formatter = dateTimeFormatter;
            }
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            return formatter.parse(date);
        } catch (ParseException e) {
            Log.e(TAG, "Unable to parse date.", e);
            return null;
        }
    }
}