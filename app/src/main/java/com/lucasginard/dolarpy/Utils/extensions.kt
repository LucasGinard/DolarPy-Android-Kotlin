package com.lucasginard.dolarpy.Utils

import android.widget.RadioButton
import androidx.core.content.ContextCompat
import com.lucasginard.dolarpy.R

fun RadioButton.setTint(color:Int = R.color.primaryColor){
    this.backgroundTintList = ContextCompat.getColorStateList(this.context , color)
}