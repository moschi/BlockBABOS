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
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
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
    private var hardcodedVideoList = ArrayList<String>()
    private var i = 0
    private val apiController = ApiController()

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
        super.onCreateView(inflater, container, savedInstanceState);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val executor: Executor = Executors.newSingleThreadExecutor()

        executor.execute {
            val mostViewedMovies = apiController.getMostViewedMovies()
            val max = mostViewedMovies.size

            //Proof
            val link1 =
                apiController.getTrailerLinks(mostViewedMovies[generateRandomIndex(max)])[0].key
            val link2 =
                apiController.getTrailerLinks(mostViewedMovies[generateRandomIndex(max)])[0].key
            val link3 =
                apiController.getTrailerLinks(mostViewedMovies[generateRandomIndex(max)])[0].key
            val link4 =
                apiController.getTrailerLinks(mostViewedMovies[generateRandomIndex(max)])[0].key
            val link5 =
                apiController.getTrailerLinks(mostViewedMovies[generateRandomIndex(max)])[0].key

            hardcodedVideoList.add(link1)
            hardcodedVideoList.add(link2)
            hardcodedVideoList.add(link3)
            hardcodedVideoList.add(link4)
            hardcodedVideoList.add(link5)
        }

        val youTubePlayerView: YouTubePlayerView =
            view.findViewById(R.id.youtube_player) as YouTubePlayerView

        lifecycle.addObserver(youTubePlayerView)

        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                val videoId = hardcodedVideoList[0]
                youtubePlayer = youTubePlayer
                youTubePlayer.loadVideo(videoId, 0f)
            }
        })
    }


    fun onSwipe(type: Swipe.SwipeType) {
        val logTag = "WHATEVER"

        fun onRightToLeftSwipe() {
            Log.i(logTag, "RightToLeftSwipe!")
            Toast.makeText(activity, "RightToLeftSwipe", Toast.LENGTH_SHORT).show()
            youtubePlayer.cueVideo(hardcodedVideoList[i--], 0F)
            youtubePlayer.play()

        }

        fun onLeftToRightSwipe() {
            Log.i(logTag, "LeftToRightSwipe!")
            Toast.makeText(activity, "LeftToRightSwipe", Toast.LENGTH_SHORT).show()

            youtubePlayer.cueVideo(hardcodedVideoList[i++], 0F)
            youtubePlayer.play()

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