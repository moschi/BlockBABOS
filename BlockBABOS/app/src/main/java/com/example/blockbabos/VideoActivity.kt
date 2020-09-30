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

    //TODO fix reloading of video, everytime the phone gets turned
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        val youTubePlayerView = findViewById<View>(R.id.youtube_player) as YouTubePlayerView

        youTubePlayerView.initialize("YOUR API KEY",
            object : YouTubePlayer.OnInitializedListener {
                override fun onInitializationSuccess(
                    provider: YouTubePlayer.Provider,
                    youTubePlayer: YouTubePlayer, b: Boolean
                ) {
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