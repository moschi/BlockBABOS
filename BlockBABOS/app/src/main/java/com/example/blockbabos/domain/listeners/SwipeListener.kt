package com.example.blockbabos.domain.listeners

import android.view.View

abstract class SwipeListener :View.OnTouchListener {

    abstract fun onRightSwipe()
    abstract fun onLeftSwipe()
    abstract fun onTopSwipe()
    abstract fun onBotSwipe()
    abstract fun onNone()

}