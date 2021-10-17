package com.lucasginard.dolarpy.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.lucasginard.dolarpy.data.apiService
import com.lucasginard.dolarpy.databinding.ActivityMainBinding
import com.lucasginard.dolarpy.domain.MainRepository
import com.lucasginard.dolarpy.utils.*
import com.lucasginard.dolarpy.view.home.HomeActivity
import com.lucasginard.dolarpy.view.viewModel.MainViewModel
import com.lucasginard.dolarpy.view.viewModel.MyViewModelFactory
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var bindding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    private var SPLASH_DISPLAY_LENGTH = 7000
    private val retrofitService = apiService.getInstance()


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
        viewModel = ViewModelProvider(this, MyViewModelFactory(MainRepository(retrofitService))).get(MainViewModel::class.java)
        bindding.contraintBase.setBackground()
        val config = this.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if (viewModel.getFlatModeTheme()){
            val saveMode = viewModel.getMode()
            if (saveMode != 3){
                if (Tools.flatTheme){
                    when(saveMode){
                        1 ->{
                            if (config != AppCompatDelegate.MODE_NIGHT_YES){
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                            }
                        }
                        0 ->{
                            if (config != AppCompatDelegate.MODE_NIGHT_NO){
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getApi(){
        viewModel.getDolarList.observe(this, Observer {
            it.dolarpy.amambay.name = "AMANBAY"
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
            it.dolarpy.gnbfusion.name = "GNB FUSIÓN"
            it.dolarpy.eurocambios.name = "Euro Cambios"
            Tools.listBase.clear()
            Tools.listBase.add(it.dolarpy.amambay)
            Tools.listBase.add(it.dolarpy.bcp)
            Tools.listBase.add(it.dolarpy.bonanza)
            Tools.listBase.add(it.dolarpy.cambiosalberdi)
            Tools.listBase.add(it.dolarpy.cambioschaco)
            Tools.listBase.add(it.dolarpy.interfisa)
            Tools.listBase.add(it.dolarpy.gnbfusion)
            Tools.listBase.add(it.dolarpy.lamoneda)
            Tools.listBase.add(it.dolarpy.maxicambios)
            Tools.listBase.add(it.dolarpy.mundialcambios)
            Tools.listBase.add(it.dolarpy.mydcambios)
            Tools.listBase.add(it.dolarpy.set)
            Tools.listBase.add(it.dolarpy.vision)
            Tools.listBase.add(it.dolarpy.eurocambios)
            Tools.lastUpdate = it.update
            SPLASH_DISPLAY_LENGTH = 0
        })

        viewModel.errorMessage.observe(this, Observer {
            Tools.flatCheck = true
        })
        viewModel.getAllDolar()
    }

    override fun onResume() {
        super.onResume()
        configureUI()
    }
}