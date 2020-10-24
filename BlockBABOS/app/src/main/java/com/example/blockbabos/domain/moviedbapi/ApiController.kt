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


class ApiController {

    private val API_KEY = "f6789f8a7c4ea3cc8c3b4fc4b1e75973"
    private val LANGUAGE = "en_US"
    private val BASE_URI = "https://api.themoviedb.org/3/"
    private val httpClient: CloseableHttpClient = HttpClientBuilder.create().build()

    private val TOP_RATED = "movie/top_rated"
    private val POPULAR = "movie/popular"
    val mapper: ObjectMapper =
        jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(
                JsonParser.Feature.AUTO_CLOSE_SOURCE, true
            );


    private fun createRequest(uri: String): ClassicHttpRequest {
        val request = ClassicHttpRequests.get(uri)
        return request
    }

    private fun uriBuilder(path: String, isList: Boolean): String {
        if (isList) {
            return "$BASE_URI$path?api_key=$API_KEY&language=$LANGUAGE&page=1"

        } else {
            return "$BASE_URI$path?api_key=$API_KEY"
        }
    }

    //    inline fun <reified T:AbstractJsonMapping> castResponse(response: CloseableHttpResponse) : T {
//        val inputStream = BufferedInputStream(response.entity.content)
//        val responseString = IOUtils.toString(inputStream)
//        println(responseString)
//        return mapper.readValue(responseString)
//    }
    inline fun <reified T : List<AbstractJsonMapping>> castResponseList(response: CloseableHttpResponse): T {
        val inputStream = BufferedInputStream(response.entity.content)
        val jsonObject = mapper.readTree(inputStream)
        response.close()
        val resultObject = jsonObject.findValue("results")
        return mapper.readValue(resultObject.toString())
    }

    fun getTopRatedMovies(): List<MovieBasic> {
        val uri = (uriBuilder(TOP_RATED, true))
        val response = httpClient.execute(createRequest(uri))
        return castResponseList(response)
    }

    fun getMostViewedMovies(): List<MovieInfo> {
        val uri = (uriBuilder(POPULAR, true))
        val response = httpClient.execute(createRequest(uri))
        return castResponseList(response)
    }

    fun getTrailerLinks(movieInfo: MovieInfo): List<Video> {
        val uri = (uriBuilder("movie/" + movieInfo.id.toString() + "/videos", true))
        val response = httpClient.execute(createRequest(uri))
        return castResponseList(response)
    }

    fun getSimilar(movieId: Int): List<MovieInfo> {
        var uri = (uriBuilder("movie/$movieId/similar", true))
        val response = httpClient.execute(createRequest(uri))
        return castResponseList(response)
    }
}