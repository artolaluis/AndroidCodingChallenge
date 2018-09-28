/**
 * Copyright 2018. Luis Artola. All rights reserved.
 */

package com.luisartola.moviereviews.ui;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.luisartola.moviereviews.R;
import com.luisartola.moviereviews.api.API;
import com.luisartola.moviereviews.api.APIService;
import com.luisartola.moviereviews.api.Reviews;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Shows simple list of reviews with seamless paging on scroll and pull-to-refresh capabilities.
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    private Reviews reviews;
    private RecyclerView reviewsView;
    private SwipeRefreshLayout reviewsRefresher;
    private int previousLastVisibleItem;
    private boolean isLoading;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initReviewsView();
        reloadReviews();
    }

    /**
     * Initializes recycler view with seamless paging on scroll and pull-to-refresh capabilities.
     */
    private void initReviewsView() {
        // Recycler view for reviews
        reviewsView = findViewById(R.id.reviews);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        reviewsView.setLayoutManager(layoutManager);

        // Pull to refresh
        reviewsRefresher = findViewById(R.id.reviews_refresher);
        reviewsRefresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadReviews();
            }
        });

        // Automatically fetch more reviews when scrolling to the end
        reviewsView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItemCount = manager.getItemCount();
                int lastVisibleItem = manager.findLastVisibleItemPosition();
                final int ITEM_COUNT_BEFORE_END = 5;

                if (lastVisibleItem > previousLastVisibleItem) {
                    previousLastVisibleItem = lastVisibleItem;
                    if (!isLoading && (lastVisibleItem + ITEM_COUNT_BEFORE_END) >= totalItemCount) {
                        // End has been reached
                        loadMoreReviews();
                    }
                }

            }
        });
    }

    /**
     * Load first page of reviews.
     */
    private void reloadReviews() {
        showToast(getString(R.string.loading));
        fetchReviews(0);
    }

    /**
     * Load next page of reviews if available. The page offset is automatically determined by the
     * number of reviews previously fetched.
     */
    private void loadMoreReviews() {
        Log.d(TAG, "Loading more reviews");
        if (reviews == null || !reviews.hasMore()) {
            Log.i(TAG, "No more reviews to load.");
            isLoading = false;
            return;
        }
        fetchReviews(reviews.getCount());
    }

    /**
     * Fetch page of reviews via {@link APIService}.
     *
     * @param offset    Starting point.
     */
    private void fetchReviews(final int offset) {
        if (isLoading) {
            Log.i(TAG, "Already fetching reviews, skipping.");
            return;
        }

        isLoading = true;
        String text = new StringBuilder()
            .append("Fetching reviews,")
            .append(" offset: ").append(offset)
            .toString();
        Log.d(TAG, text);

        APIService service = API.getService();
        Call<Reviews> call = service.reviews("by-date", offset);
        call.enqueue(new Callback<Reviews>() {
            @Override
            public void onResponse(Call<Reviews> call, Response<Reviews> response) {
                if (response.isSuccessful()) {
                    Reviews reviews = response.body();
                    String text = new StringBuilder()
                        .append("Loaded reviews")
                        .append(", status: ").append(reviews.getStatus())
                        .append(", count: ").append(reviews.getCount())
                        .append(", hasMore: ").append(reviews.hasMore())
                        .toString();
                    Log.i(TAG, text);
                    setReviews(reviews, offset);
                } else {
                    Log.e(TAG, "Unable to get reviews, error: " + response.message());
                }
                reviewsRefresher.setRefreshing(false);
                isLoading = false;
            }

            @Override
            public void onFailure(Call<Reviews> call, Throwable t) {
                String text = getString(R.string.unable_to_load_reviews);
                Log.e(TAG, text, t);
                showToast(text);
                reviewsRefresher.setRefreshing(false);
                isLoading = false;
            }
        });
    }

    /**
     * Cache and display reviews. The list is initialized fresh when offset is zero. This is
     * the case when the application launches or when pulling down on the list to refresh.
     * Results are aggregated to previously fetched reviews when offset is greater than zero.
     *
     * @param reviews   {@link Reviews} instance with all {@link Reviews.Review} fetched.
     * @param offset    Starting point.
     */
    private void setReviews(final Reviews reviews, final int offset) {
        if (offset == 0) {
            // Setting first page of reviews. Initialize recycler view fresh.
            // if this was called in response to pull-to-refresh it will effectively
            // discard all previously loaded reviews.
            this.reviews = reviews;
            ReviewsAdapter adapter = new ReviewsAdapter(this.reviews);
            reviewsView.setAdapter(adapter);
            previousLastVisibleItem = 0;
        } else {
            // Adding page of reviews to previously loaded ones.
            this.reviews.add(reviews);
            reviewsView.getAdapter().notifyItemRangeInserted(offset, reviews.getCount());
        }
    }

    /**
     * Flash short message to user.
     *
     * @param text  Text to display.
     */
    private void showToast(String text) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.show();
    }

}
