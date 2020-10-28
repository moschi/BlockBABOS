package com.example.blockbabos.presentation.layouts

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.RelativeLayout
import com.example.blockbabos.presentation.MainActivity
import com.example.blockbabos.domain.listeners.helper.Motion
import com.example.blockbabos.domain.listeners.helper.Swipe

class CustomYoutubeLayout : RelativeLayout {
    private var swipe: Swipe = Swipe(150)
    private var intercept = false
    private val motion = Motion()

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context) : super(context)

    var moved = false
    var tobeReleased = false
    private var type = Swipe.SwipeType.NONE



    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return false
    }

    /*
    Kind of a hacky solution for the problem, that the YoutubeActivity consumes all the MotionEvents and doesn't dispatch them back to the customView
    But hey.. It works

    https://stackoverflow.com/questions/7449799/how-are-android-touch-events-delivered

    When there are multiple motionevents this method checks, whether it's a swipe or not
        if swipe    -> call onswipe function in videoactivity and intercept touchevents (e.g. the youtube view)
        else        -> dispatch event
    */

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> moved = false
            MotionEvent.ACTION_MOVE -> moved = true
        }

        if (moved) {
            this.setPadding(motion.getRight(), motion.getBottom(), motion.getLeft(), motion.getTop())
            if(!tobeReleased){
                swipe.setDown(ev.x, ev.y)
                tobeReleased = true
            }

            if (ev.action == MotionEvent.ACTION_UP) {
                swipe.setUp(ev.x, ev.y)
                type = swipe.getSwipeType()
                val activity  = context as MainActivity
                motion.reset()
                this.setPadding(0,0,0,0)
                activity.onSwipe(type)
                tobeReleased = false
            }
            if (!tobeReleased  && type == Swipe.SwipeType.NONE) {
                return super.dispatchTouchEvent(ev)
            }
            val deltaX = swipe.downX - ev.x
            val deltaY = swipe.downY - ev.y
            motion.setLeft(deltaX.toInt())
            motion.setRight(-deltaX.toInt())
            motion.setBottom(-deltaY.toInt())
            motion.setTop(deltaY.toInt())

            return this.intercept
        } else {
            return super.dispatchTouchEvent(ev)
        }
    }


}
