package com.lucasginard.dolarpy.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.lucasginard.dolarpy.DolarApp
import com.lucasginard.dolarpy.R
import com.lucasginard.dolarpy.data.apiService
import com.lucasginard.dolarpy.databinding.ActivityMainBinding
import com.lucasginard.dolarpy.domain.MainRepository
import com.lucasginard.dolarpy.utils.*
import com.lucasginard.dolarpy.view.home.HomeActivity
import com.lucasginard.dolarpy.view.viewModel.MainViewModel
import com.lucasginard.dolarpy.view.viewModel.MyViewModelFactory
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var bindding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    private var SPLASH_DISPLAY_LENGTH = 7000
    private val retrofitService = apiService.getInstance()
    private lateinit var preferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindding.root)
        configureUI()
        getApi()
        Handler(Looper.getMainLooper()).postDelayed({
            val i = Intent(this@MainActivity, HomeActivity::class.java)
            startActivity(i)
        }, SPLASH_DISPLAY_LENGTH.toLong())
    }

    private fun configureUI(){
        bindding.contraintBase.setBackground()
        preferences = this.getSharedPreferences("saveSettings", Context.MODE_PRIVATE)
        if (preferences.getBoolean("flatSaveLenguaje",false)){
            val lenguaje = preferences.getString("saveLenguaje","")
            if (Locale.getDefault().language != lenguaje){
                when(lenguaje){
                    "es" ->{
                        this.setAppLocale("es")
                        this.recreate()
                    }
                    else ->{
                        this.setAppLocale("en")
                        this.recreate()
                    }
                }
            }
        }
        if (preferences.getBoolean("flatSaveMode",false)){
            val saveMode = preferences.getInt("saveMode",3)
            if (saveMode != 3){
                when(saveMode){
                    1 ->{
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    }
                    0 ->{
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }
                }
            }
        }
    }

    private fun getApi(){
        viewModel = ViewModelProvider(this, MyViewModelFactory(MainRepository(retrofitService))).get(MainViewModel::class.java)
        viewModel.getDolarList.observe(this, Observer {
            it.dolarpy.amambay.name = "AMAMBAY"
            it.dolarpy.bbva.name = "BBVA"
            it.dolarpy.bcp.name = "BCP"
            it.dolarpy.bonanza.name = "BONANZA"
            it.dolarpy.cambiosalberdi.name = "CAMBIOS ALBERDI"
            it.dolarpy.cambioschaco.name = "CAMBIOS CHACO"
            it.dolarpy.interfisa.name = "INTERFISA"
            it.dolarpy.lamoneda.name = "LA MONEDA"
            it.dolarpy.maxicambios.name = "MAXICAMBIOS"
            it.dolarpy.mundialcambios.name = "MUNDIAL CAMBIOS"
            it.dolarpy.mydcambios.name = "MYD CAMBIOS"
            it.dolarpy.set.name = "SET"
            it.dolarpy.vision.name = "Visión Banco"
            it.dolarpy.eurocambios.name = "Euro Cambios"
            Tools.listBase.clear()
            Tools.listBase.add(it.dolarpy.amambay)
            Tools.listBase.add(it.dolarpy.bbva)
            Tools.listBase.add(it.dolarpy.bcp)
            Tools.listBase.add(it.dolarpy.bonanza)
            Tools.listBase.add(it.dolarpy.cambiosalberdi)
            Tools.listBase.add(it.dolarpy.cambioschaco)
            Tools.listBase.add(it.dolarpy.interfisa)
            Tools.listBase.add(it.dolarpy.lamoneda)
            Tools.listBase.add(it.dolarpy.maxicambios)
            Tools.listBase.add(it.dolarpy.mundialcambios)
            Tools.listBase.add(it.dolarpy.mydcambios)
            Tools.listBase.add(it.dolarpy.set)
            Tools.listBase.add(it.dolarpy.vision)
            Tools.listBase.add(it.dolarpy.eurocambios)
            Tools.lastUpdate = it.update
            deleteDolarList()
            SPLASH_DISPLAY_LENGTH = 0
        })

        viewModel.errorMessage.observe(this, Observer {
            val tintRed = ContextCompat.getColor(this, R.color.danger)
            bindding.progressBar.setTint(tintRed)
            Tools.flatCheck = true
        })
        viewModel.getAllDolar()
    }

    private fun deleteDolarList(){
        GlobalScope.launch {
            DolarApp.database.dolarDao().deleteDates()
        }
    }

    override fun onResume() {
        super.onResume()
        configureUI()
    }
}