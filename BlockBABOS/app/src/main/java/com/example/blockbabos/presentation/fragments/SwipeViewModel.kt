package com.example.blockbabos.presentation.fragments

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.blockbabos.domain.dao.BaboMovieDao
import com.example.blockbabos.domain.model.BaboMovie
import com.example.blockbabos.domain.model.SwipeResult
import com.omertron.themoviedbapi.model.media.Video
import com.omertron.themoviedbapi.model.movie.MovieInfo
import kotlinx.coroutines.launch
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class SwipeViewModel(private val database: BaboMovieDao, application: Application) :
    AndroidViewModel(application) {

    private var currentBaboMovie = MutableLiveData<BaboMovie?>()
    
    fun setCurrentMovie(movieInfo: MovieInfo) {
        viewModelScope.launch {
            var movie = database.get(movieInfo.id)

            if (movie == null) {
                var newMovie = BaboMovie();
                newMovie.movieDbApiId = movieInfo.id
                newMovie.title = movieInfo.title
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
}