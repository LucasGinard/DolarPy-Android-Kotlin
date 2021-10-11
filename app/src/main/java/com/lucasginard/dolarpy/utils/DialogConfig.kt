package com.lucasginard.dolarpy.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.Intent.getIntent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat.startActivity
import com.google.android.material.switchmaterial.SwitchMaterial
import com.lucasginard.dolarpy.R
import com.lucasginard.dolarpy.view.home.HomeActivity
import java.util.*


class DialogConfig(context: Context,var activity: Activity?,var preferences: SharedPreferences): AlertDialog(context) {

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_layout)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        showDialogConfigure(activity,preferences)
    }


    private fun showDialogConfigure(activity: Activity?,preferences: SharedPreferences) {
        val change = findViewById<AutoCompleteTextView>(R.id.lenguaje)
        configureLenguaje(activity,change,preferences)

        val switchMode = findViewById<SwitchMaterial>(R.id.switchMode)
        configureMode(switchMode,activity,preferences)

        val btnAccept = findViewById<Button>(R.id.btnAceppt)
        btnAccept.setOnClickListener {
            dismiss()
        }
    }

    private fun configureLenguaje(activity: Activity?,change:AutoCompleteTextView,preferences: SharedPreferences){
        val arrayLenguaje = listOf("English ","Español")
        val adapter = ArrayAdapter(activity!!, R.layout.support_simple_spinner_dropdown_item,arrayLenguaje)
        change.setAdapter(adapter)
        saveLanguage(change,arrayLenguaje)
        change.setOnItemClickListener { parent, view, position, id ->
            Tools.dialogCustom(activity,activity.getString(R.string.dialogLanguage),{
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
            },{
                saveLanguage(change,arrayLenguaje)
                change.clearFocus()
            },false)
        }
    }

    private fun saveLanguage(change:AutoCompleteTextView,arrayLenguaje: List<String>){
        if (preferences.getBoolean("flatSaveLenguaje",false)){
            when(preferences.getString("saveLenguaje","")){
                "es" ->{
                    change.setText(arrayLenguaje[1],false)
                    dismiss()
                }
                else ->{
                    change.setText(arrayLenguaje[0],false)
                    dismiss()
                }
            }
        }else{
            when(Locale.getDefault().language){
                "es" ->{
                    change.setText(arrayLenguaje[1],false)
                    dismiss()
                }
                else ->{
                    change.setText(arrayLenguaje[0],false)
                    dismiss()
                }
            }
        }
    }

    private fun configureMode(switchMode: SwitchMaterial,activity: Activity?,preferences: SharedPreferences) {
        saveMode(preferences,switchMode)
        switchMode.setOnCheckedChangeListener { buttonView, isChecked ->
            Tools.dialogCustom(activity!!,activity.getString(R.string.dialogMode),{
                if (isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    saveConfigMode(preferences,1,true)
                    Tools.flatTheme = false
                    activity.finish()
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    saveConfigMode(preferences,0,true)
                    Tools.flatTheme = false
                    activity.finish()
                }
            },{
                saveMode(preferences,switchMode)
            },false)
        }
    }

    private fun saveConfigLenguaje(preferences: SharedPreferences,lenguaje:String,flatLenguaje:Boolean){
        val editor = preferences.edit()
        editor.putBoolean("flatSaveLenguaje",flatLenguaje)
        editor.putString("saveLenguaje",lenguaje)
        editor.apply()
    }

    private fun saveMode(preferences: SharedPreferences,switchMode: SwitchMaterial?){
        val config = activity!!.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when(preferences.getInt("saveMode",3)){
            1 -> switchMode!!.isChecked = true
            0 -> switchMode!!.isChecked = false
            else -> switchMode!!.isChecked = config == Configuration.UI_MODE_NIGHT_YES
        }
    }

    companion object{
        fun saveConfigMode(preferences: SharedPreferences,mode:Int,flatMode:Boolean){
            val editor = preferences.edit()
            editor.putBoolean("flatSaveMode",flatMode)
            editor.putInt("saveMode",mode)
            editor.apply()
        }
    }
}