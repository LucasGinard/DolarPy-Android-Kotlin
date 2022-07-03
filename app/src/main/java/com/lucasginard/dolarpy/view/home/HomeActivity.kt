package com.lucasginard.dolarpy.view.home

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lucasginard.dolarpy.R
import com.lucasginard.dolarpy.data.apiService
import com.lucasginard.dolarpy.utils.setBackground
import com.lucasginard.dolarpy.databinding.ActivityHomeBinding
import com.lucasginard.dolarpy.domain.MainRepository
import com.lucasginard.dolarpy.utils.OnHorizontalSwipeListener
import com.lucasginard.dolarpy.utils.Tools
import com.lucasginard.dolarpy.utils.setAppLocale
import com.lucasginard.dolarpy.view.home.fragments.CotizacionFragment
import com.lucasginard.dolarpy.view.home.fragments.InfoFragment
import com.lucasginard.dolarpy.view.home.fragments.UbicacionFragment
import com.lucasginard.dolarpy.view.viewModel.MainViewModel
import com.lucasginard.dolarpy.view.viewModel.MyViewModelFactory
import java.util.*


class HomeActivity : AppCompatActivity() {

    private lateinit var preferences: SharedPreferences
    private lateinit var bindding:ActivityHomeBinding
    private lateinit var viewModel: MainViewModel

    private val retrofitService = apiService.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        bindding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(bindding.root)
        configureNav()
        configureUI()
    }

    private fun configureUI() {
        bindding.contraintBase.setBackground()
        preferences = this.getSharedPreferences("saveSettings", Context.MODE_PRIVATE)
        if (preferences.getBoolean("flatSaveLenguaje",false)){
            val lenguaje = preferences.getString("saveLenguaje",Locale.getDefault().language)
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
        viewModel = ViewModelProvider(this, MyViewModelFactory(MainRepository(retrofitService))).get(
            MainViewModel::class.java)
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

    override fun onBackPressed() {
        moveTaskToBack(true)
    }

    override fun onResume() {
        super.onResume()
        configureUI()
    }


    private fun configureNav() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentHome) as NavHostFragment
        bindding.navView.setupWithNavController(navHostFragment.navController)
    }

}


