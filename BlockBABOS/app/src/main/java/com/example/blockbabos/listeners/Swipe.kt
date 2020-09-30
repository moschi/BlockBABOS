package com.example.blockbabos.listeners

class Swipe {
    var downX = 0F
    var downY = 0F
    var upX = 0F
    var upY = 0F

    val MIN_DISTANCE = 100 // TODO change this runtime based on screen resolution. for 1920x1080 is to small the 100 distance

    fun setUp(x: Float, y: Float){
        upX = x
        upY = y
    }
    fun setDown(x: Float, y: Float){
        downX = x
        downY = y
    }

    enum class SwipeType {
        UP, LEFT, RIGHT, DOWN, NONE
    }

    fun getSwypeType(): SwipeType {
            var type = SwipeType.NONE

            val deltaX = downX - upX
            val deltaY = downY - upY

            if (Math.abs(deltaX) > MIN_DISTANCE) {
                if (deltaX < 0) {
                    type = SwipeType.RIGHT
                }
                if (deltaX > 0) {
                    type = SwipeType.LEFT
                }
            }
            else if (Math.abs(deltaY) > MIN_DISTANCE) {
                if (deltaY < 0) {
                    type = SwipeType.DOWN
                }
                if (deltaY > 0) {
                    type = SwipeType.UP

                }
            }else{
                type = SwipeType.NONE;

        }
        return type
    }

    override fun toString(): String {
        return "$downX $downY $upX $upY"
    }
}