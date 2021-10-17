package com.lucasginard.dolarpy.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.switchmaterial.SwitchMaterial
import com.lucasginard.dolarpy.R
import com.lucasginard.dolarpy.view.viewModel.MainViewModel
import java.util.*


class DialogConfig(context: Context,var activity: Activity?,var viewModel: MainViewModel): AlertDialog(context) {

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_layout)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        showDialogConfigure(activity)
    }


    private fun showDialogConfigure(activity: Activity?) {
        val change = findViewById<AutoCompleteTextView>(R.id.lenguaje)
        configureLenguaje(activity,change)

        val switchMode = findViewById<SwitchMaterial>(R.id.switchMode)
        configureMode(switchMode,activity)

        val btnAccept = findViewById<Button>(R.id.btnAceppt)
        btnAccept.setOnClickListener {
            dismiss()
        }
    }

    private fun configureLenguaje(activity: Activity?,change:AutoCompleteTextView){
        val arrayLenguaje = listOf("English ","Español")
        val adapter = ArrayAdapter(activity!!, R.layout.support_simple_spinner_dropdown_item,arrayLenguaje)
        change.setAdapter(adapter)
        saveLanguage(change,arrayLenguaje)
        change.setOnItemClickListener { parent, view, position, id ->
            Tools.dialogCustom(activity,activity.getString(R.string.dialogLanguage),{
                when (position){
                    0 ->{
                        activity.setAppLocale("en")
                        viewModel.setSaveLenguaje("en")
                        viewModel.setFlatLenguaje(true)
                        activity.recreate()
                    }
                    1 ->{
                        activity.setAppLocale("es")
                        viewModel.setSaveLenguaje("es")
                        viewModel.setFlatLenguaje(true)
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
        if (viewModel.getFlatLenguaje()){
            when(viewModel.getSaveLenguaje()){
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

    private fun configureMode(switchMode: SwitchMaterial,activity: Activity?) {
        saveMode(switchMode)
        switchMode.setOnCheckedChangeListener { buttonView, isChecked ->
            Tools.dialogCustom(activity!!,activity.getString(R.string.dialogMode),{
                if (isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    viewModel.saveMode(1)
                    viewModel.setFlatMode(true)
                    Tools.flatTheme = false
                    activity.finish()
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    viewModel.saveMode(0)
                    viewModel.setFlatMode(true)
                    Tools.flatTheme = false
                    activity.finish()
                }
            },{
                saveMode(switchMode)
            },false)
        }
    }

    private fun saveMode(switchMode: SwitchMaterial?){
        val config = activity!!.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when(viewModel.getMode()){
            1 -> switchMode!!.isChecked = true
            0 -> switchMode!!.isChecked = false
            else -> switchMode!!.isChecked = config == Configuration.UI_MODE_NIGHT_YES
        }
    }
}