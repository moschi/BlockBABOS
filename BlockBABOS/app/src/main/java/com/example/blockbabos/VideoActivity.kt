package com.example.blockbabos

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.blockbabos.listeners.Swipe
import com.example.blockbabos.moviedbapi.ApiController
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlin.math.abs

class VideoActivity : YouTubeBaseActivity(){

    private var hardcodedVideoList = ArrayList<String>()
    lateinit var youtubePlayer :YouTubePlayer
    private var i = 0
    private val apiController = ApiController()


    fun generateRandomIndex(max : Int):Int{
        return abs(Math.random() * max).toInt()
    }

    //TODO fix reloading of video, everytime the phone gets turned
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        val executor: Executor = Executors.newSingleThreadExecutor()
        executor.execute {
            val mostViewedMovies = apiController.getMostViewedMovies()
            val max = mostViewedMovies.size

            //Proof
            val link1 = apiController.getTrailerLinks(mostViewedMovies[generateRandomIndex(max)])[0].key
            val link2 = apiController.getTrailerLinks(mostViewedMovies[generateRandomIndex(max)])[0].key
            val link3 = apiController.getTrailerLinks(mostViewedMovies[generateRandomIndex(max)])[0].key
            val link4 = apiController.getTrailerLinks(mostViewedMovies[generateRandomIndex(max)])[0].key
            val link5 = apiController.getTrailerLinks(mostViewedMovies[generateRandomIndex(max)])[0].key


            hardcodedVideoList.add(link1)
            hardcodedVideoList.add(link2)
            hardcodedVideoList.add(link3)
            hardcodedVideoList.add(link4)
            hardcodedVideoList.add(link5)
        }

        val youTubePlayerView = findViewById<View>(R.id.youtube_player) as YouTubePlayerView

        youTubePlayerView.initialize("YOUR API KEY",
            object : YouTubePlayer.OnInitializedListener {
                override fun onInitializationSuccess(
                    provider: YouTubePlayer.Provider,
                    youTubePlayer: YouTubePlayer, b: Boolean
                ) {
                    youtubePlayer = youTubePlayer
                    youTubePlayer.cueVideo("N1WRualRBOQ-feE")
                    youTubePlayer.setShowFullscreenButton(false)
                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL)

                    youTubePlayer.play()
                }

                override fun onInitializationFailure(
                    provider: YouTubePlayer.Provider,
                    youTubeInitializationResult: YouTubeInitializationResult
                ) {
                    println(youTubeInitializationResult)
                    println("ERROR OCCURRED")
                }
            })
    }
    fun onSwipe (type: Swipe.SwipeType){
        val logTag = "WHATEVER"

        fun onRightToLeftSwipe() {
            Log.i(logTag, "RightToLeftSwipe!")
            Toast.makeText(this, "RightToLeftSwipe", Toast.LENGTH_SHORT).show()
            youtubePlayer.cueVideo(hardcodedVideoList[i--])
            youtubePlayer.play()

        }

        fun onLeftToRightSwipe() {
            Log.i(logTag, "LeftToRightSwipe!")
            Toast.makeText(this, "LeftToRightSwipe", Toast.LENGTH_SHORT).show()

            youtubePlayer.cueVideo(hardcodedVideoList[i++])
            youtubePlayer.play()

        }

        fun onTopToBottomSwipe() {
            Log.i(logTag, "onTopToBottomSwipe!")
            Toast.makeText(this, "onTopToBottomSwipe", Toast.LENGTH_SHORT).show()
        }

        fun onBottomToTopSwipe() {
            Log.i(logTag, "onBottomToTopSwipe!")
            Toast.makeText(this, "onBottomToTopSwipe", Toast.LENGTH_SHORT).show()
        }

        when(type){
            Swipe.SwipeType.UP-> onBottomToTopSwipe()
            Swipe.SwipeType.DOWN -> onTopToBottomSwipe()
            Swipe.SwipeType.LEFT -> onRightToLeftSwipe()
            Swipe.SwipeType.RIGHT -> onLeftToRightSwipe()
            else -> println("whatever")
        }
    }


}