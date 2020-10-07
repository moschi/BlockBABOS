package com.example.blockbabos.moviedbapi

import org.junit.Assert
import org.junit.Test

class ApiControllerTest {

    private val apiController = ApiController()

    @Test
    fun topRated() {
        val topRatedMovies = apiController.getTopRatedMovies()
        println("top rated movie: " + topRatedMovies[0].title)
        Assert.assertNotNull(topRatedMovies)
    }

    @Test
    fun trailerLink() {
        val links = apiController.getTrailerLinks(apiController.getMostViewedMovies()[0])
        println("links of top rated movie is: $links")
        Assert.assertTrue(links[0].toString() != "")
    }

    @Test
    fun mostViewed() {
        val mostViewed = apiController.getMostViewedMovies()[0]
        println("Most viewed movie is: $mostViewed")
        Assert.assertNotNull(mostViewed)
    }


}