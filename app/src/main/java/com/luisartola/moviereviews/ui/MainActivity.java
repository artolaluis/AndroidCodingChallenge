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
    private int previousLastVisibleItem;
    private boolean isLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initReviewsView();
        reloadReviews();
    }

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

                if (lastVisibleItem > previousLastVisibleItem) {
                    previousLastVisibleItem = lastVisibleItem;
                    if (!isLoading && totalItemCount <= (lastVisibleItem + 5)) {
                        // End has been reached
                        loadMoreReviews();
                    }
                }

            }
        });
    }

    private void reloadReviews() {
        fetchReviews(0);
    }

    private void loadMoreReviews() {
        isLoading = true;

        Log.d(TAG, "Loading more reviews");
        if (reviews == null || !reviews.hasMore) {
            Log.i(TAG, "No more reviews to load.");
            isLoading = false;
            return;
        }

        fetchReviews(reviews.count);
    }

    private void fetchReviews(final int offset) {
        isLoading = true;
        Log.d(TAG, "Reloading reviews");
        APIService service = API.getService();
        // Fetch first
        Call<Reviews> call = service.reviews("by-date", offset);
        call.enqueue(new Callback<Reviews>() {
            @Override
            public void onResponse(Call<Reviews> call, Response<Reviews> response) {
                if (response.isSuccessful()) {
                    Reviews reviews = response.body();
                    String text = new StringBuilder()
                        .append("Loaded reviews")
                        .append(", status: ").append(reviews.status)
                        .append(", count: ").append(reviews.count)
                        .append(", hasMore: ").append(reviews.hasMore)
                        .append(", copyright: ").append(reviews.copyright)
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
                Log.e(TAG, "Unable to reload reviews.", t);
                reviewsRefresher.setRefreshing(false);
                isLoading = false;
            }
        });
    }

    private void setReviews(Reviews reviews, int offset) {
        if (offset == 0) {
            this.reviews = reviews;
            ReviewsAdapter adapter = new ReviewsAdapter(this.reviews);
            reviewsView.setAdapter(adapter);
            previousLastVisibleItem = 0;
        } else {
            this.reviews.hasMore = reviews.hasMore;
            this.reviews.status = reviews.status;
            this.reviews.copyright = reviews.copyright;
            this.reviews.results.addAll(reviews.results);
            this.reviews.count = this.reviews.results.size();
            reviewsView.getAdapter().notifyItemRangeInserted(offset, reviews.count);
        }
    }

}
