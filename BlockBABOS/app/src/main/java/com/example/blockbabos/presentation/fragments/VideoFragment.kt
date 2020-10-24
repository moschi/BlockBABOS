package com.example.blockbabos.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.blockbabos.R
import com.example.blockbabos.domain.listeners.helper.Swipe
import com.example.blockbabos.domain.moviedbapi.ApiController
import com.example.blockbabos.persistence.BaboMovieRoomDatabase
import com.google.android.material.appbar.MaterialToolbar
import com.omertron.themoviedbapi.model.media.Video
import com.omertron.themoviedbapi.model.movie.MovieInfo
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlin.math.abs

private const val TIME_PLAYED = "TIME_PLAYED"
private const val TITLE = "TITLE"
private const val TO_BE_PLAYED = "TO_BE_PLAYED"
private const val CURRENT_PLAYED_MOVIE_ID = "CURRENT_PLAYED_MOVIE_ID"

/**
 * A simple [Fragment] subclass.
 * Use the [VideoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class VideoFragment : Fragment() {
    private lateinit var swipeViewModel: SwipeViewModel
    lateinit var youtubePlayer: YouTubePlayer
    var youtubeTracker = YouTubePlayerTracker()

    private val apiController = ApiController()
    private var nextPlayedVideo: MovieInfo? = null
    private lateinit var currentPlayedVideo : MovieInfo
    private var currentPlayedMovieId = 0

    private var playTime = 0F
    private var title = ""
    private var toBePlayed = ""

    private lateinit var toolbar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            playTime = savedInstanceState.getFloat(TIME_PLAYED)
            title = savedInstanceState.getString(TITLE)!!
            toBePlayed = savedInstanceState.getString(TO_BE_PLAYED)!!
        }
        val application = requireNotNull(this.activity).application
        val dataSource = BaboMovieRoomDatabase.getDatabase(application).baboMovieDao()
        swipeViewModel =
            ViewModelProvider(this, SwipeViewModelFactory(dataSource, application)).get(
                SwipeViewModel::class.java
            )
        Log.i("VideoFragment", "created swipeViewModel")
    }

    private fun generateRandomIndex(max: Int): Int {
        return abs(Math.random() * max).toInt()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_video, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val youTubePlayerView: YouTubePlayerView =
            view.findViewById(R.id.youtube_player) as YouTubePlayerView
        toolbar = activity?.findViewById(R.id.toolbar) as MaterialToolbar
        lifecycle.addObserver(youTubePlayerView)
        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youtubePlayer = youTubePlayer
                youtubePlayer.addListener(youtubeTracker)
                if (toBePlayed == "") {
                    val nextVideo = nextVideo()
                    showYoutubeVideo(nextVideo[0].key)
                } else {
                    showYoutubeVideo(toBePlayed)
                }
            }
        })
    }


    private fun nextVideo(): List<Video> {
        val fetchVideosLatch = CountDownLatch(1)

        var videoList = ArrayList<MovieInfo>()
        var nextVideoToShow = ArrayList<Video>()
        val executor: Executor = Executors.newSingleThreadExecutor()

        executor.execute {
            videoList = apiController.getMostViewedMovies() as ArrayList<MovieInfo>
            fetchVideosLatch.countDown()
        }
        fetchVideosLatch.await()

        val max = videoList.size
        if (nextPlayedVideo != null) {
            currentPlayedVideo = nextPlayedVideo as MovieInfo
        } else {
            currentPlayedVideo = videoList[generateRandomIndex(max)]
        }
        title = currentPlayedVideo.title ?: ""
        currentPlayedMovieId = currentPlayedVideo.id

        nextPlayedVideo = videoList[generateRandomIndex(max)]

        val fetchLinksLatch = CountDownLatch(1)

        executor.execute {
            nextVideoToShow =
                apiController.getTrailerLinks(currentPlayedVideo) as ArrayList<Video>
            toBePlayed = nextVideoToShow[0].key
            fetchLinksLatch.countDown()
        }

        fetchLinksLatch.await()
        return nextVideoToShow
    }

    private fun showYoutubeVideo(nextVideoUri: String) {
        swipeViewModel.setCurrentMovie(currentPlayedMovieId, title)
        toolbar.title = title
        youtubePlayer.loadVideo(nextVideoUri, playTime)
        playTime = 0F
        youtubePlayer.play()
    }


    fun onSwipe(type: Swipe.SwipeType) {
        val logTag = "WHATEVER"
        if (this::youtubePlayer.isInitialized) {
            fun onRightToLeftSwipe() {
                Log.i(logTag, "RightToLeftSwipe!")
                swipeViewModel.onSwipeLeft()
                showYoutubeVideo(nextVideo()[0].key)
            }

            fun onLeftToRightSwipe() {
                Log.i(logTag, "LeftToRightSwipe!")
                swipeViewModel.onSwipeRight()
                showYoutubeVideo(nextVideo()[0].key)
            }

            fun onTopToBottomSwipe() {
                Log.i(logTag, "onTopToBottomSwipe!")
            }

            fun onBottomToTopSwipe() {
                Log.i(logTag, "onBottomToTopSwipe!")
                swipeViewModel.onSwipeUp()
                showYoutubeVideo(nextVideo()[0].key)
            }

            when (type) {
                Swipe.SwipeType.UP -> onBottomToTopSwipe()
                Swipe.SwipeType.DOWN -> onTopToBottomSwipe()
                Swipe.SwipeType.LEFT -> onRightToLeftSwipe()
                Swipe.SwipeType.RIGHT -> onLeftToRightSwipe()
                else -> println("whatever")
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment VideoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            VideoFragment().apply({})
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putFloat(TIME_PLAYED, youtubeTracker.currentSecond)
        outState.putString(TO_BE_PLAYED, toBePlayed)
        outState.putString(TITLE, title)
        outState.putInt(CURRENT_PLAYED_MOVIE_ID, currentPlayedMovieId)
    }
}