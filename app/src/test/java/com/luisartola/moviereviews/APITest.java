package com.luisartola.moviereviews;

import android.util.Log;

import com.luisartola.moviereviews.api.API;
import com.luisartola.moviereviews.api.APIService;
import com.luisartola.moviereviews.api.Reviews;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
@PowerMockIgnore("javax.net.ssl.*")
public class APITest {

    public static final String TAG = APITest.class.getName();

    @Before
    public void setup() {
        PowerMockito.mockStatic(Log.class);
    }

    @Test
    public void getReviews() {
        APIService service = API.getService();
        Call<Reviews> call = service.reviews("by-date", 0);
        try {
            Response<Reviews> response = call.execute();
            if (response.isSuccessful()) {
                Reviews reviews = response.body();
                String text = new StringBuilder()
                    .append("status: ").append(reviews.status)
                    .append("count: ").append(reviews.count)
                    .append("hasMore: ").append(reviews.hasMore)
                    .append("copyright: ").append(reviews.copyright)
                    .toString();
                Log.i(TAG, text);
            } else {
                Log.e(TAG, "Unable to get reviews, error was: " + response.message());
            }
        } catch (IOException exception) {
            Log.e(TAG, "Unable to get reviews, unexpected error.", exception);
        }
    }
}