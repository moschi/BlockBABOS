package com.example.blockbabos.moviedbapi

import com.omertron.themoviedbapi.TheMovieDbApi
import com.omertron.themoviedbapi.model.movie.MovieInfo
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClientBuilder

class ApiController {

    private val API_KEY = "f6789f8a7c4ea3cc8c3b4fc4b1e75973"
    private val LANGUAGE = "en_US"
    private val httpClient: CloseableHttpClient = HttpClientBuilder.create().build()

    private val theMovieDbAApi = TheMovieDbApi(API_KEY, httpClient)

    fun getTopRatedMovies(): List<MovieInfo> {
        val topRatedMovies = theMovieDbAApi.getTopRatedMovies(1, LANGUAGE)
        return topRatedMovies.results
    }

    fun getMostViewedMovies(): List<MovieInfo> {
        val mostViewed = theMovieDbAApi.getPopularMovieList(1, LANGUAGE)
        return mostViewed.results
    }

    fun getTrailerLink(movieInfo: MovieInfo): String {
        val movieVideos = theMovieDbAApi.getMovieVideos(movieInfo.id, LANGUAGE)
        return movieVideos.results[0].key
    }
}