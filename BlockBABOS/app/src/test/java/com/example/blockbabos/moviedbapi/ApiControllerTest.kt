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



}