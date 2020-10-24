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
import com.omertron.themoviedbapi.model.media.Video
import com.omertron.themoviedbapi.model.movie.MovieInfo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class SwipeViewModel(
    private val database: BaboMovieDao,
    private val apiController: ApiController,
    application: Application
) :
    AndroidViewModel(application) {
    private var currentBaboMovie = MutableLiveData<BaboMovie?>()
    private lateinit var currentMovieInfo: MovieInfo
    private var recommendationCreator = RecommendationCreator(database, apiController)

    fun getCurrentMovie(): MovieInfo {
        return currentMovieInfo
    }

    fun restoreCurrentMovie(id: Int, title: String) {
        currentMovieInfo = MovieInfo()
        currentMovieInfo.id = id
        currentMovieInfo.title = title
        setCurrentMovie(id, title)
    }

    private fun setCurrentMovie(movieInfo: MovieInfo) {
        currentMovieInfo = movieInfo
        setCurrentMovie(movieInfo.id, movieInfo.title)
    }

    private fun setCurrentMovie(id: Int, title: String) {
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
        val result = nextMovie.await()
        setCurrentMovie(result)
        return@runBlocking result
    }

    fun getMovieTrailerUri(movieInfo: MovieInfo): String {
        var nextVideoToShow: ArrayList<Video>
        var nextTrailerUri = ""
        val executor: Executor = Executors.newSingleThreadExecutor()
        val fetchLinksLatch = CountDownLatch(1)

        executor.execute {
            nextVideoToShow =
                apiController.getTrailerLinks(movieInfo) as ArrayList<Video>
            if (nextVideoToShow.isEmpty()) {
                nextTrailerUri = "MISSING_TRAILER"
            } else {
                nextTrailerUri = nextVideoToShow[0].key
            }
            fetchLinksLatch.countDown()
        }

        fetchLinksLatch.await()
        return nextTrailerUri
    }
}