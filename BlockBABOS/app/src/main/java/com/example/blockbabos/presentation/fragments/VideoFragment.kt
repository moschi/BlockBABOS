package com.example.blockbabos.presentation.fragments

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.blockbabos.R
import com.example.blockbabos.domain.listeners.helper.Swipe
import com.example.blockbabos.domain.moviedbapi.ApiController
import com.example.blockbabos.persistence.BaboMovieRoomDatabase
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationMenu
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView


private const val TIME_PLAYED = "TIME_PLAYED"
private const val CURRENT_PLAYED_MOVIE_TITLE = "TITLE"
private const val TO_BE_PLAYED = "TO_BE_PLAYED"
private const val CURRENT_PLAYED_MOVIE_ID = "CURRENT_PLAYED_MOVIE_ID"

class VideoFragment : Fragment() {
    private lateinit var swipeViewModel: SwipeViewModel
    lateinit var youtubePlayer: YouTubePlayer
    var youtubeTracker = YouTubePlayerTracker()

    private val apiController = ApiController()
    private var currentPlayedMovieId = 0

    private var playTime = 0F
    private var currentPlayedMovieTitle = ""
    private var toBePlayed = ""

    private lateinit var toolbar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            playTime = savedInstanceState.getFloat(TIME_PLAYED)
            currentPlayedMovieId = savedInstanceState.getInt(CURRENT_PLAYED_MOVIE_ID)
            currentPlayedMovieTitle = savedInstanceState.getString(CURRENT_PLAYED_MOVIE_TITLE)!!
            toBePlayed = savedInstanceState.getString(TO_BE_PLAYED)!!
        }
        val application = requireNotNull(this.activity).application
        val dataSource = BaboMovieRoomDatabase.getDatabase(application).baboMovieDao()
        swipeViewModel =
            ViewModelProvider(
                this,
                SwipeViewModelFactory(dataSource, apiController, application)
            ).get(
                SwipeViewModel::class.java
            )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            activity?.findViewById<MaterialToolbar>(R.id.toolbar)?. visibility = View.GONE
            activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)?.visibility = View.GONE
        }
        return inflater.inflate(R.layout.fragment_video, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val youTubePlayerView = view.findViewById<YouTubePlayerView>(R.id.youtube_player)
        toolbar = activity?.findViewById(R.id.toolbar) as MaterialToolbar
        toolbar.title = ""
        lifecycle.addObserver(youTubePlayerView)
        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youtubePlayer = youTubePlayer
                youtubePlayer.addListener(youtubeTracker)
                if (toBePlayed == "") {
                    showYoutubeVideo(nextVideo())
                } else {
                    swipeViewModel.restoreCurrentMovie(
                        currentPlayedMovieId,
                        currentPlayedMovieTitle
                    )
                    showYoutubeVideo(toBePlayed)
                }
            }
        })
    }


    private fun nextVideo(): String {
        val nextMovieInfo = swipeViewModel.getNextMovie()!!
        val nextVideoUri = swipeViewModel.getMovieTrailerUri(nextMovieInfo)
        // todo: this too is kind of a hack, maybe we'll need to fix this someday...
        if (nextVideoUri == "MISSING_TRAILER") {
            return nextVideo()
        }

        currentPlayedMovieTitle = swipeViewModel.getCurrentMovie().title ?: ""
        currentPlayedMovieId = swipeViewModel.getCurrentMovie().id
        toBePlayed = nextVideoUri

        return nextVideoUri
    }

    private fun showYoutubeVideo(nextVideoUri: String) {
        toolbar.title = currentPlayedMovieTitle
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
                showYoutubeVideo(nextVideo())
            }

            fun onLeftToRightSwipe() {
                Log.i(logTag, "LeftToRightSwipe!")
                swipeViewModel.onSwipeRight()
                showYoutubeVideo(nextVideo())
            }

            fun onTopToBottomSwipe() {
                Log.i(logTag, "onTopToBottomSwipe!")
            }

            fun onBottomToTopSwipe() {
                Log.i(logTag, "onBottomToTopSwipe!")
                swipeViewModel.onSwipeUp()
                Toast.makeText(context, getString(R.string.added, swipeViewModel.getCurrentMovie().title), Toast.LENGTH_LONG).show()
                showYoutubeVideo(nextVideo())
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
        @JvmStatic
        fun newInstance() = VideoFragment()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putFloat(TIME_PLAYED, youtubeTracker.currentSecond)
        outState.putString(TO_BE_PLAYED, toBePlayed)
        outState.putString(CURRENT_PLAYED_MOVIE_TITLE, currentPlayedMovieTitle)
        outState.putInt(CURRENT_PLAYED_MOVIE_ID, currentPlayedMovieId)
    }
}