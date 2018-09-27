package com.luisartola.moviereviews.api;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Contains response with reviews.
 *
 * Example:
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
    public String copyright;

    @SerializedName("has_more")
    public boolean hasMore;

    @SerializedName("num_results")
    public Integer count;

    @SerializedName("status")
    public String status;

    @SerializedName("results")
    public List<Review> results;

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

    public static class Review {

        @SerializedName("byline")
        public String byline;

        @SerializedName("critics_pick")
        public Integer criticsPick;

        @SerializedName("date_updated")
        public Date dateUpdated;

        @SerializedName("display_title")
        public String displayTitle;

        @SerializedName("headline")
        public String headline;

        @SerializedName("link")
        public Link link;

        @SerializedName("mpaa_rating")
        public String mpaaRating;

        @SerializedName("multimedia")
        public Multimedia multimedia;

        @SerializedName("opening_date")
        public Date openingDate;

        @SerializedName("publication_date")
        public Date publicationDate;

        @SerializedName("summary_short")
        public String summaryShort;

    }

    public static class Link {

        @SerializedName("suggested_link_text")
        public String suggestedLinkText;

        @SerializedName("type")
        public String type;

        @SerializedName("url")
        public String url;

    }

    public static class Multimedia {

        @SerializedName("src")
        public String source;

        @SerializedName("type")
        public String type;

        @SerializedName("width")
        public Integer width;

        @SerializedName("height")
        public Integer height;

    }

}

