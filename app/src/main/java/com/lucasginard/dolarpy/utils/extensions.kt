package com.lucasginard.dolarpy.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.lucasginard.dolarpy.R
import java.util.*

fun View.setTint(color:Int = R.color.primaryColor){
    this.backgroundTintList = ContextCompat.getColorStateList(this.context , color)
}

fun ConstraintLayout.setBackground(){
    val nightModeFlags = context.resources.configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK
    when (nightModeFlags) {
        Configuration.UI_MODE_NIGHT_YES -> this.background = ContextCompat.getDrawable(this.context,R.drawable.background_night)
        Configuration.UI_MODE_NIGHT_NO ->  this.background = ContextCompat.getDrawable(this.context,R.drawable.background_light)
    }
}

fun Context.setAppLocale(language: String) {
    val locale = Locale(language)
    Locale.setDefault(locale)
    val config = Configuration()
    config.setLocale(locale)
    config.setLayoutDirection(locale)
    this.resources.updateConfiguration(
        config,
        this.resources.displayMetrics
    )
}

@SuppressLint("ClickableViewAccessibility")
fun View.setOnClick(clickEvent: () -> Unit) {
    this.setOnTouchListener { _, event ->
        if (event.action == MotionEvent.ACTION_UP) {
            clickEvent.invoke()
        }
        false
    }
}

fun Activity.isAttachedToActivity(): Boolean {
    return this.isAttachedToActivity()
}