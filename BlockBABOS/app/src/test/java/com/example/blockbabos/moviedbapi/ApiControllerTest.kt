package com.example.blockbabos.moviedbapi

import org.junit.Assert
import org.junit.Test

class ApiControllerTest{

    private val restConverter = ApiController()

    @Test
    fun test(){
        val topRatedMovies = restConverter.getTopRatedMovies()
        println("top rated movie: " + topRatedMovies[0].title)
        Assert.assertNotNull(topRatedMovies)
    }

    @Test
    fun trailerLink(){
        val link = restConverter.getTrailerLink(restConverter.getMostViewedMovies()[0])
        println("link of top rated movie is: $link")
        Assert.assertTrue(link != "")
    }



}