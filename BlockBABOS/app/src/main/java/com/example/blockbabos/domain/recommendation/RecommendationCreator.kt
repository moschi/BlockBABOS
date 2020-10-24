package com.example.blockbabos.domain.recommendation

import com.example.blockbabos.domain.dao.BaboMovieDao
import com.example.blockbabos.domain.moviedbapi.ApiController
import com.omertron.themoviedbapi.model.movie.MovieInfo
import kotlin.math.abs

class RecommendationCreator(
    private val database: BaboMovieDao,
    private val apiController: ApiController
) {

    private suspend fun getSimilarRecommendation(): MovieInfo? {
        // todo: prefer superliked? maybe use liked as fallback?
        val liked = database.getLikedAsList()
        val randomLike = liked.get(generateRandomIndex(liked.size - 1))

        val similarToLiked = randomLike?.movieDbApiId?.let { apiController.getSimilar(it) }
            ?.filter { !database.movieRated(it.id) }

        // todo: this is possibly a dangerous hack, but I like to live dangerously :)
        if (similarToLiked.isEmpty()) {
            return getSimilarRecommendation()
        }

        return similarToLiked?.get(generateRandomIndex(similarToLiked.size - 1))
    }

    private fun getStartRecommendation(): MovieInfo? {
        val mostViewed = apiController.getMostViewedMovies()

        return mostViewed[generateRandomIndex(mostViewed.size - 1)]
    }

    suspend fun getAnyRecommendation(): MovieInfo? {
        return getSimilarRecommendation() ?: return getStartRecommendation()
    }

    private fun generateRandomIndex(max: Int): Int {
        return abs(Math.random() * max).toInt()
    }
}