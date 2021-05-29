package com.lucasginard.dolarpy.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.lucasginard.dolarpy.Utils.Tools
import com.lucasginard.dolarpy.data.apiService
import com.lucasginard.dolarpy.databinding.ActivityMainBinding
import com.lucasginard.dolarpy.domain.MainRepository
import com.lucasginard.dolarpy.ui.home.Home
import com.lucasginard.dolarpy.ui.viewModel.MainViewModel
import com.lucasginard.dolarpy.ui.viewModel.MyViewModelFactory


class MainActivity : AppCompatActivity() {

    private lateinit var bindding: ActivityMainBinding
    private var SPLASH_DISPLAY_LENGTH = 7000
    lateinit var viewModel: MainViewModel
    private val retrofitService = apiService.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindding.root)
        getApi()
        Handler(Looper.getMainLooper()).postDelayed({
            val i = Intent(this@MainActivity, Home::class.java)
            startActivity(i)
        }, SPLASH_DISPLAY_LENGTH.toLong())

    }

    fun getApi(){
        viewModel = ViewModelProvider(this, MyViewModelFactory(MainRepository(retrofitService))).get(MainViewModel::class.java)
        viewModel.getDolarList.observe(this, Observer {
            it.dolarpy.amambay.name = "AMANBAY"
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
            Tools.lastUpdate = it.update
            SPLASH_DISPLAY_LENGTH = 0
        })

        viewModel.errorMessage.observe(this, Observer {
            Tools.dialogCustom(this, "Sin conexión compruebe su conexión")
        })
        viewModel.getAllDolar()
    }
}