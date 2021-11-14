package com.lucasginard.dolarpy.utils

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.view.animation.RotateAnimation
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lucasginard.dolarpy.R
import com.lucasginard.dolarpy.data.model.com_ven
import kotlin.collections.ArrayList


open class Tools {
    companion object{
        val listBase = ArrayList<com_ven>()
        var flatCheck = false
        var flatSave = true
        var flatTheme = true
        var flatRecyclerSave = false
        var lastUpdate =""

        fun setMargin(view: View, left: Int =0, right: Int  =0, top: Int  =0, bottom: Int  =0) {
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
            animation.duration = 500
            view.startAnimation(animation)
        }

        fun animationY(view: View){
            val iconAnim = ObjectAnimator.ofPropertyValuesHolder(view,
                PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, view.height.toFloat(), 0f),
                PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f))
            iconAnim.duration = 400
            iconAnim.start()
        }

        fun dialogCustom(activity: Activity, text: String, btnAccept: () -> Unit, btnCancel: () -> Unit ,exitOnTap:Boolean = true) {
            val builder = MaterialAlertDialogBuilder(activity)
            builder.setMessage(text)
                .setPositiveButton(R.string.btnAccept){dialog ,it ->
                    btnAccept()
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.btnCancel){dialog ,it ->
                    btnCancel()
                    dialog.dismiss()
                }
                .setCancelable(exitOnTap)
            builder.show()
        }

        fun dialogCustom(activity: Activity, text: String, btnAccept: () -> Unit) {
            val builder = MaterialAlertDialogBuilder(activity)
            builder.setMessage(text)
                    .setPositiveButton(R.string.btnAccept){dialog ,it ->
                        btnAccept()
                        dialog.dismiss()
                    }
            builder.show()
        }

        fun goToURL(url:String,context:Context){
            val uri: Uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(intent)
        }
    }
}