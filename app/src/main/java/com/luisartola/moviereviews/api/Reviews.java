/**
 * Copyright 2018. Luis Artola. All rights reserved.
 */

package com.luisartola.moviereviews.api;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Deserialized representation of response data from a review page request.
 *
 * Example JSON response from REST API represented by instance of this class:
 * <pre>
 * {
 *     "copyright": "Copyright (c) 2018 The New York Times Company. All Rights Reserved.",
 *     "has_more": true,
 *     "num_results": 1,
 *     "results": [
 *         {
 *             "byline": "MANOHLA DARGIS",
 *             "critics_pick": 1,
 *             "date_updated": "2018-09-23 02:44:24",
 *             "display_title": "Colette",
 *             "headline": "Review: \u2018Colette\u2019 and One Woman\u2019s Lust for Life",
 *             "link": {
 *                 "suggested_link_text": "Read the New York Times Review of Colette",
 *                 "type": "article",
 *                 "url": "http://www.nytimes.com/2018/09/20/movies/colette-review.html"
 *             },
 *             "mpaa_rating": "R",
 *             "multimedia": {
 *                 "height": 140,
 *                 "src": "https://static01.nyt.com/images/2018/09/19/arts/19colette1/merlin_143890539_b04f8a07-099a-46e2-9af4-3de2e7185111-mediumThreeByTwo210.jpg",
 *                 "type": "mediumThreeByTwo210",
 *                 "width": 210
 *             },
 *             "opening_date": null,
 *             "publication_date": "2018-09-20",
 *             "summary_short": "Keira Knightley stars in the attractive biographical movie \u201cColette,\u201d which takes a light, enjoyably fizzy approach to its subject."
 *         }
 *     ],
 *     "status": "OK"
 * }
 * </pre>
 */
public class Reviews {

    @SerializedName("copyright")
    private String copyright;

    @SerializedName("has_more")
    private boolean hasMore;

    @SerializedName("num_results")
    private Integer count;

    @SerializedName("status")
    private String status;

    @SerializedName("results")
    private List<Review> results;

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public boolean hasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Review> getResults() {
        return results;
    }

    public void setResults(List<Review> results) {
        this.results = results;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Reviews)) {
            return false;
        }

        Reviews other = (Reviews)object;
        return this.count == other.count
            && this.hasMore == other.hasMore
            && ((this.copyright == null && other.copyright == null) || this.copyright.equals(other.copyright))
            && ((this.status == null && other.status == null) || this.status.equals(other.status))
            && this.results.equals(other.results);
    }

    /**
     * Add results from given {@link Reviews} instance. Useful to aggregate pages of results
     * from various review requests.
     *
     * @param reviews   {@link Reviews} instance.
     */
    public void add(Reviews reviews) {
        hasMore = reviews.hasMore;
        status = reviews.status;
        copyright = reviews.copyright;
        results.addAll(reviews.results);
        count = results.size();
    }

    /**
     * Single review.
     */
    public static class Review {

        @SerializedName("byline")
        private String byline;

        @SerializedName("critics_pick")
        private Integer criticsPick;

        @SerializedName("date_updated")
        private Date dateUpdated;

        @SerializedName("display_title")
        private String displayTitle;

        @SerializedName("headline")
        private String headline;

        @SerializedName("link")
        private Link link;

        @SerializedName("mpaa_rating")
        private String mpaaRating;

        @SerializedName("multimedia")
        private Multimedia multimedia;

        @SerializedName("opening_date")
        private Date openingDate;

        @SerializedName("publication_date")
        private Date publicationDate;

        @SerializedName("summary_short")
        private String summaryShort;

        public String getByline() {
            return byline;
        }

        public void setByline(String byline) {
            this.byline = byline;
        }

        public Integer getCriticsPick() {
            return criticsPick;
        }

        public void setCriticsPick(Integer criticsPick) {
            this.criticsPick = criticsPick;
        }

        public Date getDateUpdated() {
            return dateUpdated;
        }

        public void setDateUpdated(Date dateUpdated) {
            this.dateUpdated = dateUpdated;
        }

        public String getDisplayTitle() {
            return displayTitle;
        }

        public void setDisplayTitle(String displayTitle) {
            this.displayTitle = displayTitle;
        }

        public String getHeadline() {
            return headline;
        }

        public void setHeadline(String headline) {
            this.headline = headline;
        }

        public Link getLink() {
            return link;
        }

        public void setLink(Link link) {
            this.link = link;
        }

        public String getMpaaRating() {
            return mpaaRating;
        }

        public void setMpaaRating(String mpaaRating) {
            this.mpaaRating = mpaaRating;
        }

        public Multimedia getMultimedia() {
            return multimedia;
        }

        public void setMultimedia(Multimedia multimedia) {
            this.multimedia = multimedia;
        }

        public Date getOpeningDate() {
            return openingDate;
        }

        public void setOpeningDate(Date openingDate) {
            this.openingDate = openingDate;
        }

        public Date getPublicationDate() {
            return publicationDate;
        }

        public void setPublicationDate(Date publicationDate) {
            this.publicationDate = publicationDate;
        }

        public String getSummaryShort() {
            return summaryShort;
        }

        public void setSummaryShort(String summaryShort) {
            this.summaryShort = summaryShort;
        }
    }

    /**
     * Link to review.
     */
    public static class Link {

        @SerializedName("suggested_link_text")
        private String suggestedLinkText;

        @SerializedName("type")
        private String type;

        @SerializedName("url")
        private String url;

        public String getSuggestedLinkText() {
            return suggestedLinkText;
        }

        public void setSuggestedLinkText(String suggestedLinkText) {
            this.suggestedLinkText = suggestedLinkText;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    /**
     * Multimedia asset, e.g. thumbnail image.
     */
    public static class Multimedia {

        @SerializedName("src")
        private String source;

        @SerializedName("type")
        private String type;

        @SerializedName("width")
        private Integer width;

        @SerializedName("height")
        private Integer height;

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Integer getWidth() {
            return width;
        }

        public void setWidth(Integer width) {
            this.width = width;
        }

        public Integer getHeight() {
            return height;
        }

        public void setHeight(Integer height) {
            this.height = height;
        }
    }

}

