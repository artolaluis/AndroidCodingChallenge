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
            final Reviews.Review item = reviews.getResults().get(position);
            ReviewViewHolder videoViewHolder = (ReviewViewHolder) holder;
            videoViewHolder.update(item);
        }
    }

    @Override
    public int getItemCount() {
        return (reviews != null && reviews.getResults() !=null) ? reviews.getResults().size() : 0;
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
        private TextView headline;
        private TextView summary;
        private TextView rating;
        private TextView publicationDate;

        ReviewViewHolder(View view) {
            super(view);
            thumbnail = view.findViewById(R.id.thumbnail);
            title = view.findViewById(R.id.title);
            headline = view.findViewById(R.id.headline);
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
            String thumbnailUrl = review.getMultimedia().getSource();
            if (thumbnailUrl != null && !thumbnailUrl.isEmpty()) {
                Picasso.get()
                    .load(thumbnailUrl)
                    .into(thumbnail);
            } else {
                thumbnail.setImageResource(R.drawable.thumbnail_placeholder);
            }

            // Display relevant information
            try {
                if (review.getDisplayTitle() != null) {
                    title.setText(URLDecoder.decode(review.getDisplayTitle(), "UTF-8"));
                }

                if (review.getHeadline() != null) {
                    String text = URLDecoder.decode(review.getHeadline());
                    // We know we are displaying reviews. Remove redundant prefix for a nicer
                    // list that is easier ot read.
                    if (text.startsWith("Review: ")) {
                        text = text.substring(8);
                    }
                    headline.setText(text);
                }

                if (review.getMpaaRating() != null && !review.getMpaaRating().isEmpty()) {
                    rating.setText(URLDecoder.decode(review.getMpaaRating(), "UTF-8"));
                    rating.setVisibility(View.VISIBLE);
                } else {
                    rating.setVisibility(View.INVISIBLE);
                }

                if (review.getByline() != null) {
                    byline.setText(URLDecoder.decode(review.getByline(), "UTF-8"));
                }

                if (review.getSummaryShort() != null) {
                    summary.setText(URLDecoder.decode(review.getSummaryShort(), "UTF-8"));
                }

                if (review.getPublicationDate() != null) {
                    DateFormat formatter = SimpleDateFormat.getDateInstance();
                    String text = formatter.format(review.getPublicationDate());
                    publicationDate.setText(text);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

    }

}

