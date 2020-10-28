package com.example.blockbabos.domain.moviedbapi

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.omertron.themoviedbapi.model.AbstractJsonMapping
import com.omertron.themoviedbapi.model.media.Video
import com.omertron.themoviedbapi.model.movie.MovieBasic
import com.omertron.themoviedbapi.model.movie.MovieInfo
import org.apache.hc.client5.http.classic.methods.ClassicHttpRequests
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder
import org.apache.hc.core5.http.ClassicHttpRequest
import java.io.BufferedInputStream

private const val API_KEY = "f6789f8a7c4ea3cc8c3b4fc4b1e75973"
private const val LANGUAGE = "en_US"
private const val BASE_URI = "https://api.themoviedb.org/3/"
private const val TOP_RATED = "movie/top_rated"
private const val POPULAR = "movie/popular"

class ApiController {
    private val httpClient: CloseableHttpClient = HttpClientBuilder.create().build()
    private val mapper: ObjectMapper =
        jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(
                JsonParser.Feature.AUTO_CLOSE_SOURCE, true
            )

    private fun createRequest(uri: String): ClassicHttpRequest {
        return ClassicHttpRequests.get(uri)
    }

    private fun uriBuilder(path: String, isList: Boolean): String {
        return if (isList) {
            "$BASE_URI$path?api_key=$API_KEY&language=$LANGUAGE&page=1"

        } else {
            "$BASE_URI$path?api_key=$API_KEY"
        }
    }

    private inline fun <reified T : AbstractJsonMapping> castResponse(response: CloseableHttpResponse): T {
        val inputStream = BufferedInputStream(response.entity.content)
        val jsonObject = mapper.readTree(inputStream)
        response.close()
        return mapper.readValue(jsonObject.toString())
    }
    private inline fun <reified T : List<AbstractJsonMapping>> castResponseList(response: CloseableHttpResponse): T {
        val inputStream = BufferedInputStream(response.entity.content)
        val jsonObject = mapper.readTree(inputStream)
        response.close()
        val resultObject = jsonObject.findValue("results")
        var returnValue = "[]"
        if (resultObject != null) {
            returnValue = resultObject.toString()
        }
        return mapper.readValue(returnValue)
    }

    fun getTopRatedMovies(): List<MovieBasic> {
        val uri = (uriBuilder(TOP_RATED, true))
        println(uri)
        val response = httpClient.execute(createRequest(uri))
        return castResponseList(response)
    }

    fun getMostViewedMovies(): List<MovieInfo> {
        val uri = (uriBuilder(POPULAR, true))
        println(uri)
        val response = httpClient.execute(createRequest(uri))
        return castResponseList(response)
    }

    fun getTrailerLinks(movieInfo: MovieInfo): List<Video> {
        val uri = (uriBuilder("movie/" + movieInfo.id.toString() + "/videos", true))
        val response = httpClient.execute(createRequest(uri))
        return castResponseList(response)
    }

    fun getSimilar(movieId: Int): List<MovieInfo> {
        val uri = (uriBuilder("movie/$movieId/similar", true))
        val response = httpClient.execute(createRequest(uri))
        return castResponseList(response)
    }
    fun getMovieById(movieId: Int): MovieInfo{
        val uri = (uriBuilder("movie/$movieId", false))
        val response = httpClient.execute(createRequest(uri))
        return castResponse(response)
    }


}