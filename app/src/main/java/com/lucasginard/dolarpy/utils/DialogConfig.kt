package com.lucasginard.dolarpy.utils

import android.app.Activity
import android.app.Dialog
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.switchmaterial.SwitchMaterial
import com.lucasginard.dolarpy.R
import java.util.*

class DialogConfig {
    companion object{
        fun showDialogConfigure(activity: Activity?) {
            val dialog = Dialog(activity!!)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_layout)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val change = dialog.findViewById<AutoCompleteTextView>(R.id.lenguaje)
            configureLenguaje(activity,change)

            val switchMode = dialog.findViewById<SwitchMaterial>(R.id.switchMode)
            configureMode(switchMode,activity)
            dialog.show()
        }

        fun configureLenguaje(activity: Activity?,change:AutoCompleteTextView){
            val arrayLenguaje = listOf("English ","Español")
            val adapter = ArrayAdapter(activity!!, R.layout.support_simple_spinner_dropdown_item,arrayLenguaje);
            change.setAdapter(adapter)
            when(Locale.getDefault().language){
                "es" ->{
                    change.setText(arrayLenguaje[1],false);
                }
                else ->{
                    change.setText(arrayLenguaje[0],false);
                }
            }
            change.setOnItemClickListener { parent, view, position, id ->
                when (position){
                    0 ->{
                        activity.setAppLocale("en")
                        activity.recreate()
                    }
                    1 ->{
                        activity.setAppLocale("es")
                        activity.recreate()
                    }
                }
            }
        }

        private fun configureMode(switchMode: SwitchMaterial?,activity: Activity?) {
            val config = activity!!.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            if ( config == Configuration.UI_MODE_NIGHT_YES){
                switchMode!!.isChecked = true
            }
            switchMode?.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        }
    }
}