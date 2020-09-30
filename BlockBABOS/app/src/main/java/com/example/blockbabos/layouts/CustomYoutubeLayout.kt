package com.example.blockbabos.layouts

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.RelativeLayout
import com.example.blockbabos.VideoActivity
import com.example.blockbabos.listeners.Swipe

class CustomYoutubeLayout : RelativeLayout {
    var swipe: Swipe = Swipe()
    var intercept = false

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
            if(!tobeReleased){
                swipe.setDown(ev.x, ev.y)
                tobeReleased = true
            }
            if (ev.action == MotionEvent.ACTION_UP) {
                swipe.setUp(ev.x, ev.y)
                type = swipe.getSwypeType()
                val activity = context as VideoActivity
                activity.onSwipe(type)
                tobeReleased = false
            }
            if (!tobeReleased  && type == Swipe.SwipeType.NONE) {
                return super.dispatchTouchEvent(ev)
            }
            return this.intercept
        } else {

            return super.dispatchTouchEvent(ev)
        }
    }


}
