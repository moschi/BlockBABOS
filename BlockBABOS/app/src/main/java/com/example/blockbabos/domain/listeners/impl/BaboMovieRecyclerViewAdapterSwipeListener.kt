package com.example.blockbabos.domain.listeners.impl

import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import com.example.blockbabos.R
import com.example.blockbabos.domain.listeners.SwipeListener
import com.example.blockbabos.domain.listeners.helper.Swipe

class BaboMovieRecyclerViewAdapterSwipeListener() : SwipeListener() {

    lateinit var view: View
    var swipe = Swipe(200)


    override fun onRightSwipe() {
        view.findViewById<LinearLayout>(R.id.item_delete_button_layout).visibility = View.VISIBLE
    }

    override fun onLeftSwipe() {
        view.findViewById<LinearLayout>(R.id.item_delete_button_layout).visibility = View.GONE
    }

    override fun onTopSwipe() {
    }

    override fun onBotSwipe() {
    }

    override fun onNone() {
        view.performClick()
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        view = v!!
        swipe.update(event!!)

        when (swipe.getSwypeType()) {
            Swipe.SwipeType.RIGHT -> {
                onRightSwipe()
            }
            Swipe.SwipeType.LEFT -> {
                onLeftSwipe()
            }
            Swipe.SwipeType.UP -> {
                onTopSwipe()
            }
            Swipe.SwipeType.DOWN -> {
                onBotSwipe()
            }
            Swipe.SwipeType.NONE -> {
            }
        }
        view.performClick()
        return true

    }
}