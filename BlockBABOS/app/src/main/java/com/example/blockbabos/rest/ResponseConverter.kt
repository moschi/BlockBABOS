package com.example.blockbabos.rest

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.omertron.themoviedbapi.model.movie.MovieBasic

class ResponseConverter{
    private val mapper = jacksonObjectMapper()

    fun convert (responseJson:String): MovieBasic{
        return  mapper.readValue(responseJson)
    }
}

