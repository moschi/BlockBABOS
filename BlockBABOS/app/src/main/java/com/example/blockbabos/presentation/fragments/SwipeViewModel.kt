package com.example.blockbabos.presentation.fragments

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.blockbabos.domain.dao.BaboMovieDao
import com.example.blockbabos.domain.model.BaboMovie
import com.example.blockbabos.domain.model.SwipeResult
import com.example.blockbabos.domain.moviedbapi.ApiController
import com.example.blockbabos.domain.recommendation.RecommendationCreator
import com.omertron.themoviedbapi.model.movie.MovieInfo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SwipeViewModel(
    private val database: BaboMovieDao,
    apiController: ApiController,
    application: Application
) :
    AndroidViewModel(application) {
    private var currentBaboMovie = MutableLiveData<BaboMovie?>()
    private var recommendationCreator = RecommendationCreator(database, apiController)

    fun setCurrentMovie(id: Int, title: String) {
        viewModelScope.launch {
            var movie = database.get(id)
            if (movie == null) {
                val newMovie = BaboMovie()
                newMovie.movieDbApiId = id
                newMovie.title = title
                newMovie.result = SwipeResult.UNKNOWN
                insert(newMovie)
                movie = newMovie
            }
            currentBaboMovie.value = movie
        }
    }

    private fun update(movie: BaboMovie) {
        database.update(movie)
    }

    private suspend fun insert(movie: BaboMovie) {
        database.insert(movie)
    }

    fun onSwipeRight() {
        viewModelScope.launch {
            currentBaboMovie.value?.result = SwipeResult.LIKED
            update(currentBaboMovie.value!!)
        }
    }

    fun onSwipeLeft() {
        viewModelScope.launch {
            currentBaboMovie.value?.result = SwipeResult.DISLIKED
            update(currentBaboMovie.value!!)
        }
    }

    fun onSwipeUp() {
        viewModelScope.launch {
            currentBaboMovie.value?.result = SwipeResult.SUPERLIKED
            update(currentBaboMovie.value!!)
        }
    }

    fun getNextMovie(): MovieInfo? = runBlocking {
        var nextMovie = GlobalScope.async {
            return@async recommendationCreator.getAnyRecommendation()!!
        }
        return@runBlocking nextMovie.await()
    }
}