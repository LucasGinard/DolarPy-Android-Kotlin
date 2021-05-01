package com.lucasginard.dolarpy.Utils

import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation

open class Tools {
    companion object{

        fun setMargin(view: View, left: Int, right: Int, top: Int, bottom: Int) {
            val params = view.layoutParams as ViewGroup.MarginLayoutParams
            params.setMargins(left, top, right, bottom)
            view.layoutParams = params
        }

         fun rotarImagen(view: View) {
            val animation = RotateAnimation(
                0F, 360F,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f
            )
            animation.duration = 2000
            animation.repeatCount = Animation.INFINITE
            animation.repeatMode = Animation.REVERSE
            view.startAnimation(animation)
        }
    }
}