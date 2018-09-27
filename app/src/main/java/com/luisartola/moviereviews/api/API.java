package com.luisartola.moviereviews.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Date;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class API {

    private static final String BASE_URL = "http://api.nytimes.com/svc/movies/v2/";
    private static final String API_KEY = "b75da00e12d54774a2d362adddcc9bef";

    private static APIService service;

    public static synchronized APIService getService() {
        if (service == null) {
            // Add query parameters, headers, etc. common to every request.
            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
            clientBuilder.addInterceptor(
                new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        HttpUrl url = request.url();
                        HttpUrl newUrl = url.newBuilder()
                            .addQueryParameter("api-key", API_KEY)
                            .build();
                        Request newRequest = request.newBuilder()
                            .url(newUrl)
                            .build();
                        return chain.proceed(newRequest);
                    }
                }
            );

            // Custom field serialization
            Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .create();

            // Build API service
            Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(clientBuilder.build())
                .build();
            service = retrofit.create(APIService.class);
        }

        return service;
    }

}
