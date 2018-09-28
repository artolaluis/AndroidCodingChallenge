/**
 * Copyright 2018. Luis Artola. All rights reserved.
 */

package com.luisartola.moviereviews.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * REST API endpoints.
 */
public interface APIService {

    /**
     * Returns movie reviews.
     *
     * <pre>
     * GET api.nytimes.com/svc/movies/v2/reviews/dvd-picks.json?order={order}&api-key={api_key}&offset={offset}
     * </pre>
     *
     * Sample request:
     * <pre>
     * GET api.nytimes.com/svc/movies/v2/reviews/dvd-picks.json?order=by-date&api-key=b75da00e12d54774a2d362adddcc9bef&offset=0
     * </pre>
     *
     * Sample response:
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
    @GET("reviews/dvd-picks.json")
    Call<Reviews> reviews(
        @Query("order") String order,
        @Query("offset") Integer offset
        );

}
