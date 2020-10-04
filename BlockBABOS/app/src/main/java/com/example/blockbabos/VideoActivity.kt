package com.example.blockbabos

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.blockbabos.listeners.Swipe
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView

class VideoActivity : YouTubeBaseActivity(){

    var hardcodedVideoList = ArrayList<String>()
    lateinit var youtubePlayer :YouTubePlayer
    var i = 0


    //TODO fix reloading of video, everytime the phone gets turned
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        hardcodedVideoList.add("cN9G4v2jZnM")
        hardcodedVideoList.add("CR7ob354hrA")
        hardcodedVideoList.add("hWfaUdNPCYM")
        hardcodedVideoList.add("4CgCbigmk7o")
        hardcodedVideoList.add("nOK84lLkvM0")

        val youTubePlayerView = findViewById<View>(R.id.youtube_player) as YouTubePlayerView

        youTubePlayerView.initialize("YOUR API KEY",
            object : YouTubePlayer.OnInitializedListener {
                override fun onInitializationSuccess(
                    provider: YouTubePlayer.Provider,
                    youTubePlayer: YouTubePlayer, b: Boolean
                ) {
                    youtubePlayer = youTubePlayer
                    youTubePlayer.cueVideo("N1WRualRBOQ")
                    youTubePlayer.setShowFullscreenButton(false)
                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL)

                    youTubePlayer.play()
                }

                override fun onInitializationFailure(
                    provider: YouTubePlayer.Provider,
                    youTubeInitializationResult: YouTubeInitializationResult
                ) {
                }
            })
    }
    fun onSwipe (type: Swipe.SwipeType){
        val logTag = "WHATEVER"


        //Just a test
        youtubePlayer.cueVideo(hardcodedVideoList.get(i++))
        youtubePlayer.play()

        fun onRightToLeftSwipe() {
            Log.i(logTag, "RightToLeftSwipe!")



            Toast.makeText(this, "RightToLeftSwipe", Toast.LENGTH_SHORT).show()
            // activity.doSomething();
        }

        fun onLeftToRightSwipe() {
            Log.i(logTag, "LeftToRightSwipe!")
            Toast.makeText(this, "LeftToRightSwipe", Toast.LENGTH_SHORT).show()
            // activity.doSomething();
        }

        fun onTopToBottomSwipe() {
            Log.i(logTag, "onTopToBottomSwipe!")
            Toast.makeText(this, "onTopToBottomSwipe", Toast.LENGTH_SHORT).show()
            // activity.doSomething();
        }

        fun onBottomToTopSwipe() {
            Log.i(logTag, "onBottomToTopSwipe!")
            Toast.makeText(this, "onBottomToTopSwipe", Toast.LENGTH_SHORT).show()
            // activity.doSomething();
        }

        when(type){
            Swipe.SwipeType.UP-> onBottomToTopSwipe()
            Swipe.SwipeType.DOWN -> onTopToBottomSwipe()
            Swipe.SwipeType.LEFT -> onRightToLeftSwipe()
            Swipe.SwipeType.RIGHT -> onLeftToRightSwipe()
        }
    }


}