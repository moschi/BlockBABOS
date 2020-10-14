package com.example.blockbabos

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.blockbabos.listeners.Swipe
import com.example.blockbabos.moviedbapi.ApiController
import com.google.android.material.appbar.MaterialToolbar
import com.omertron.themoviedbapi.model.media.Video
import com.omertron.themoviedbapi.model.movie.MovieInfo
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlin.math.abs


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [VideoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class VideoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var youtubePlayer: YouTubePlayer

    private val apiController = ApiController()
    private var currentPlayedVideo: MovieInfo? = null
    private var nextPlayedVideo: MovieInfo? = null

    private lateinit var toolbar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
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
                val nextVideo = getNextVideoToShow()
                showYoutubeVideo(nextVideo)

            }
        })
    }

    private fun getNextVideoToShow(): List<Video> {
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
            currentPlayedVideo = nextPlayedVideo
        } else {
            currentPlayedVideo = videoList[generateRandomIndex(max)]
        }
        nextPlayedVideo = videoList[generateRandomIndex(max)]

        val fetchLinksLatch = CountDownLatch(1)

        executor.execute {
            nextVideoToShow =
                apiController.getTrailerLinks(currentPlayedVideo as MovieInfo) as ArrayList<Video>
            fetchLinksLatch.countDown()
        }
        fetchLinksLatch.await()

        return nextVideoToShow
    }

    private fun showYoutubeVideo(nextVideo: List<Video>) {
        toolbar.title = currentPlayedVideo?.title
        youtubePlayer.loadVideo(nextVideo[0].key, 0F)
        youtubePlayer.play()
    }


    fun onSwipe(type: Swipe.SwipeType) {
        val logTag = "WHATEVER"

        fun onRightToLeftSwipe() {
            Log.i(logTag, "RightToLeftSwipe!")
            Toast.makeText(activity, "RightToLeftSwipe", Toast.LENGTH_SHORT).show()
            showYoutubeVideo(getNextVideoToShow())
        }

        fun onLeftToRightSwipe() {
            Log.i(logTag, "LeftToRightSwipe!")
            Toast.makeText(activity, "LeftToRightSwipe", Toast.LENGTH_SHORT).show()
            showYoutubeVideo(getNextVideoToShow())

        }

        fun onTopToBottomSwipe() {
            Log.i(logTag, "onTopToBottomSwipe!")
            Toast.makeText(activity, "onTopToBottomSwipe", Toast.LENGTH_SHORT).show()
        }

        fun onBottomToTopSwipe() {
            Log.i(logTag, "onBottomToTopSwipe!")
            Toast.makeText(activity, "onBottomToTopSwipe", Toast.LENGTH_SHORT).show()
        }

        when (type) {
            Swipe.SwipeType.UP -> onBottomToTopSwipe()
            Swipe.SwipeType.DOWN -> onTopToBottomSwipe()
            Swipe.SwipeType.LEFT -> onRightToLeftSwipe()
            Swipe.SwipeType.RIGHT -> onLeftToRightSwipe()
            else -> println("whatever")
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment VideoFragment2.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            VideoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}