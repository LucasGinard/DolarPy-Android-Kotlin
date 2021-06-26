package com.lucasginard.dolarpy.Utils

import android.content.res.Configuration
import android.widget.RadioButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.lucasginard.dolarpy.R

fun RadioButton.setTint(color:Int = R.color.primaryColor){
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