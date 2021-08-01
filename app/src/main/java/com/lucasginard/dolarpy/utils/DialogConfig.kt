package com.lucasginard.dolarpy.utils

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.switchmaterial.SwitchMaterial
import com.lucasginard.dolarpy.R
import com.lucasginard.dolarpy.view.home.Home
import java.util.*

class DialogConfig {
    companion object{
        fun showDialogConfigure(activity: Activity?,preferences: SharedPreferences) {
            val dialog = Dialog(activity!!)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_layout)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val change = dialog.findViewById<AutoCompleteTextView>(R.id.lenguaje)
            configureLenguaje(activity,change,preferences)

            val switchMode = dialog.findViewById<SwitchMaterial>(R.id.switchMode)
            configureMode(switchMode,activity,preferences)
            dialog.show()
        }

        fun configureLenguaje(activity: Activity?,change:AutoCompleteTextView,preferences: SharedPreferences){
            val arrayLenguaje = listOf("English ","Español")
            val adapter = ArrayAdapter(activity!!, R.layout.support_simple_spinner_dropdown_item,arrayLenguaje);
            change.setAdapter(adapter)
            if (preferences.getBoolean("flatSaveLenguaje",false)){
                when(preferences.getString("saveLenguaje","")){
                    "es" ->{
                        change.setText(arrayLenguaje[1],false);
                    }
                    else ->{
                        change.setText(arrayLenguaje[0],false);
                    }
                }
            }else{
                when(Locale.getDefault().language){
                    "es" ->{
                        change.setText(arrayLenguaje[1],false);
                    }
                    else ->{
                        change.setText(arrayLenguaje[0],false);
                    }
                }
            }
            change.setOnItemClickListener { parent, view, position, id ->
                when (position){
                    0 ->{
                        activity.setAppLocale("en")
                        saveConfigLenguaje(preferences,"en",true)
                        activity.recreate()
                    }
                    1 ->{
                        activity.setAppLocale("es")
                        saveConfigLenguaje(preferences,"es",true)
                        activity.recreate()
                    }
                }
            }
        }

        private fun configureMode(switchMode: SwitchMaterial?,activity: Activity?,preferences: SharedPreferences) {
            val config = activity!!.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            when(preferences.getInt("saveMode",3)){
                1 -> switchMode!!.isChecked = true
                0 -> switchMode!!.isChecked = false
                else -> switchMode!!.isChecked = config == Configuration.UI_MODE_NIGHT_YES
            }
            switchMode.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    saveConfigMode(preferences,1,true)
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    saveConfigMode(preferences,0,true)
                }
            }
        }

        fun saveConfigLenguaje(preferences: SharedPreferences,lenguaje:String,flatLenguaje:Boolean){
            val editor = preferences.edit()
            editor.putBoolean("flatSaveLenguaje",flatLenguaje)
            editor.putString("saveLenguaje",lenguaje)
            editor.apply()
        }

        fun saveConfigMode(preferences: SharedPreferences,mode:Int,flatMode:Boolean){
            val editor = preferences.edit()
            editor.putBoolean("flatSaveMode",flatMode)
            editor.putInt("saveMode",mode)
            editor.apply()
        }
    }
}