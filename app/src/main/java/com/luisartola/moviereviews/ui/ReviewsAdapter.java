/**
 * Copyright 2018. Luis Artola. All rights reserved.
 */

package com.luisartola.moviereviews.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.luisartola.moviereviews.R;
import com.luisartola.moviereviews.api.Reviews;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Adapter to recycler view rendering reviews.
 */
public class ReviewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_REVIEW_ITEM = 0;

    private Reviews reviews;

    ReviewsAdapter(Reviews reviews) {
        this.reviews = reviews;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == VIEW_REVIEW_ITEM) {
            View view = inflater.inflate(R.layout.item_review, parent, false);
            viewHolder = new ReviewViewHolder(view);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ReviewViewHolder) {
            final Reviews.Review item = reviews.results.get(position);
            ReviewViewHolder videoViewHolder = (ReviewViewHolder) holder;
            videoViewHolder.update(item);
        }
    }

    @Override
    public int getItemCount() {
        return (reviews != null && reviews.results !=null) ? reviews.results.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_REVIEW_ITEM;
    }

    /**
     * Display an individual review showing thumbnail and relevant information like headline,
     * byline, summary, etc.
     */
    private class ReviewViewHolder extends RecyclerView.ViewHolder {

        private ImageView thumbnail;
        private TextView title;
        private TextView byline;
        private TextView summary;
        private TextView rating;
        private TextView publicationDate;

        ReviewViewHolder(View view) {
            super(view);
            thumbnail = view.findViewById(R.id.thumbnail);
            title = view.findViewById(R.id.title);
            byline = view.findViewById(R.id.byline);
            summary = view.findViewById(R.id.summary);
            rating = view.findViewById(R.id.rating);
            publicationDate = view.findViewById(R.id.publication_date);
        }

        /**
         * Render review data. Each review is a card that renders:
         * <ul>
         *     <li>Thumbnail on the left-hand side.</li>
         *     <li>Rating is overlaid on the upper-right hand corner of thumbnail, if available.</li>
         *     <li>Headline, byline, publication date, and summary are displayed on the right-hand
         *     side.</li>
         * </ul>
         *
         * @param review {@link Reviews.Review} instance.
         */
        void update(Reviews.Review review) {
            // Render thumbnail
            String thumbnailUrl = review.multimedia.source;
            if (thumbnailUrl != null && !thumbnailUrl.isEmpty()) {
                Picasso.get()
                    .load(thumbnailUrl)
                    .into(thumbnail);
            } else {
                thumbnail.setImageResource(R.drawable.thumbnail_placeholder);
            }

            // Display relevant information
            try {
                if (review.headline != null) {
                    String headline = review.headline;
                    // We know we are displaying reviews. Remove redundant prefix for a nicer
                    // list that is easier ot read.
                    if (headline.startsWith("Review: ")) {
                        headline = headline.substring(8);
                    }
                    title.setText(URLDecoder.decode(headline, "UTF-8"));
                }

                if (review.mpaaRating != null && !review.mpaaRating.isEmpty()) {
                    rating.setText(URLDecoder.decode(review.mpaaRating, "UTF-8"));
                    rating.setVisibility(View.VISIBLE);
                } else {
                    rating.setVisibility(View.INVISIBLE);
                }

                if (review.byline != null) {
                    byline.setText(URLDecoder.decode(review.byline, "UTF-8"));
                }

                if (review.summaryShort != null) {
                    summary.setText(URLDecoder.decode(review.summaryShort, "UTF-8"));
                }

                if (review.publicationDate != null) {
                    DateFormat formatter = SimpleDateFormat.getDateInstance();
                    String text = formatter.format(review.publicationDate);
                    publicationDate.setText(text);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

    }

}

