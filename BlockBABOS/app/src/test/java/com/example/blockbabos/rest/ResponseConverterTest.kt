package com.example.blockbabos.rest

import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*

class ResponseConverterTest {

    private val responseConverter = ResponseConverter()
    private val responseJson = "{\n" +
            "  \"adult\": true,\n" +
            "  \"backdrop_path\": null,\n" +
            "  \"belongs_to_collection\": null,\n" +
            "  \"budget\": 0,\n" +
            "  \"genres\": [],\n" +
            "  \"homepage\": \"\",\n" +
            "  \"id\": 750523,\n" +
            "  \"imdb_id\": \"tt0127865\",\n" +
            "  \"original_language\": \"en\",\n" +
            "  \"original_title\": \"Sex as You Like It\",\n" +
            "  \"overview\": \"People call in to watch sex acts on a closed-circuit TV.\",\n" +
            "  \"popularity\": 0,\n" +
            "  \"poster_path\": null,\n" +
            "  \"production_companies\": [],\n" +
            "  \"production_countries\": [],\n" +
            "  \"release_date\": \"\",\n" +
            "  \"revenue\": 0,\n" +
            "  \"runtime\": 54,\n" +
            "  \"spoken_languages\": [],\n" +
            "  \"status\": \"Released\",\n" +
            "  \"tagline\": \"\",\n" +
            "  \"title\": \"Sex as You Like It\",\n" +
            "  \"video\": false,\n" +
            "  \"vote_average\": 0,\n" +
            "  \"vote_count\": 0\n" +
            "}"
    @Test
    fun convert() {
        val movie = responseConverter.convert(responseJson)
        assertNotNull(movie)
        assertEquals(movie.isAdult , true)
        assertEquals(movie.title, "Sex as You Like It")
    }
}