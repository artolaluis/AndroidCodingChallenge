package com.luisartola.moviereviews.ui;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.luisartola.moviereviews.R;
import com.luisartola.moviereviews.api.API;
import com.luisartola.moviereviews.api.APIService;
import com.luisartola.moviereviews.api.Reviews;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    private Reviews reviews;
    private RecyclerView reviewsView;
    private SwipeRefreshLayout reviewsRefresher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initReviewsView();
        reloadReviews();
    }

    private void initReviewsView() {
        // Recycler view for reviews
        reviewsView = (RecyclerView)findViewById(R.id.reviews);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        reviewsView.setLayoutManager(layoutManager);

        // Pull to refresh
        reviewsRefresher = (SwipeRefreshLayout)findViewById(R.id.reviews_refresher);
        reviewsRefresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadReviews();
            }
        });
    }

    private void reloadReviews() {
        APIService service = API.getService();
        Call<Reviews> call = service.reviews("by-date", 0);
        call.enqueue(new Callback<Reviews>() {
            @Override
            public void onResponse(Call<Reviews> call, Response<Reviews> response) {
                if (response.isSuccessful()) {
                    Reviews reviews = response.body();
                    String text = new StringBuilder()
                        .append("Loaded reviews -")
                        .append(" status: ").append(reviews.status)
                        .append(" count: ").append(reviews.count)
                        .append(" hasMore: ").append(reviews.hasMore)
                        .append(" copyright: ").append(reviews.copyright)
                        .toString();
                    Log.i(TAG, text);
                    setReviews(reviews);
                } else {
                    Log.e(TAG, "Unable to get reviews, error: " + response.message());
                }
                reviewsRefresher.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<Reviews> call, Throwable t) {
                Log.e(TAG, "Unable to reload reviews.", t);
                reviewsRefresher.setRefreshing(false);
            }
        });
    }

    private void setReviews(Reviews reviews) {
        this.reviews = reviews;
        ReviewsAdapter adapter = new ReviewsAdapter(this.reviews);
        reviewsView.setAdapter(adapter);
    }

}
