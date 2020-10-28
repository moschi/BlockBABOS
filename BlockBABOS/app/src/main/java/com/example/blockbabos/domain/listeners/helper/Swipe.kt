package com.example.blockbabos.domain.listeners.helper

import android.view.MotionEvent

class Swipe(thresholdPercentage: Int) {
    var downX = 0F
    var downY = 0F
    var upX = 0F
    var upY = 0F
    var threshold = thresholdPercentage

    var moved = false
    var tobeReleased = false
    private var type =
        SwipeType.NONE


    fun setUp(x: Float, y: Float) {
        upX = x
        upY = y
    }

    fun setDown(x: Float, y: Float) {
        downX = x
        downY = y
    }

    enum class SwipeType {
        UP, LEFT, RIGHT, DOWN, NONE
    }

    fun getSwipeType(): SwipeType {
        var type =
            SwipeType.NONE

        val deltaX = downX - upX

        val deltaY = downY - upY

        println(deltaX)
        println(deltaY)

        if (Math.abs(deltaX) > threshold) {
            if (deltaX < 0) {
                type =
                    SwipeType.RIGHT
            }
            if (deltaX > 0) {
                type =
                    SwipeType.LEFT
            }
        } else if (Math.abs(deltaY) > threshold) {
            if (deltaY < 0) {
                type =
                    SwipeType.DOWN
            }
            if (deltaY > 0) {
                type =
                    SwipeType.UP

            }
        } else {
            type =
                SwipeType.NONE

        }
        return type
    }

    fun update(ev: MotionEvent) {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> moved = true
        }

        if (moved) {
            if (!tobeReleased) {
                setDown(ev.x, ev.y)
                setUp(ev.x, ev.y)
                tobeReleased = true
            }
            if (ev.action == MotionEvent.ACTION_UP) {
                setUp(ev.x, ev.y)
                type = getSwipeType()
                tobeReleased = false
            }
        }

    }


    override fun toString(): String {
        return "$downX $downY $upX $upY"
    }
}