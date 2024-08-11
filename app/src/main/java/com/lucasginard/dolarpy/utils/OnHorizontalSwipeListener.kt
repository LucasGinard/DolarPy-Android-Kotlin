package com.lucasginard.dolarpy.utils

import android.content.Context
import android.os.Build
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

abstract class OnHorizontalSwipeListener(val context: Context) : View.OnTouchListener {

    companion object {
        const val SWIPE_MIN = 50
        const val SWIPE_VELOCITY_MIN = 100
    }

    private val detector = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
        GestureDetector(context, GestureListener())
    } else {
        TODO("VERSION.SDK_INT < CUPCAKE")
    }

    override fun onTouch(view: View, event: MotionEvent) = detector.onTouchEvent(event)

    abstract fun onRightSwipe()

    abstract fun onLeftSwipe()

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onDown(e: MotionEvent) = true

        override fun onFling(p0: MotionEvent?, e1: MotionEvent, velocityX: Float, velocityY: Float)
                : Boolean {

            val deltaY = (p0?.y ?: 0f) - e1.y
            val deltaX = (p0?.x ?: 0f) - e1.x
            if(deltaY != null && deltaY >0){
                if (Math.abs(deltaX) < Math.abs(deltaY)) return false

                if (Math.abs(deltaX) < SWIPE_MIN
                    && Math.abs(velocityX) < SWIPE_VELOCITY_MIN) return false

                if (deltaX > 0) onLeftSwipe() else onRightSwipe()
            }

            return true
        }
    }
}