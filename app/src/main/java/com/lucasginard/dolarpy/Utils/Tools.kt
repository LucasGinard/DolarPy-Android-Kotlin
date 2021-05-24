package com.lucasginard.dolarpy.Utils

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import com.lucasginard.dolarpy.R
import com.lucasginard.dolarpy.com_ven

open class Tools {
    companion object{
        val listBase = ArrayList<com_ven>()
        var lastUpdate =""

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

        fun dialogCustom(activity:Activity,Dialog:String){
            val builder = AlertDialog.Builder(activity)
            builder.setMessage(Dialog)
                    .setPositiveButton(R.string.btnAceppt,
                            DialogInterface.OnClickListener { dialog, id ->
                                // FIRE ZE MISSILES!
                            })
            // Create the AlertDialog object and return it
            builder.create()
            builder.show()
        }
    }
}