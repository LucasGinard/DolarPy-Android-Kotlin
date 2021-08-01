package com.lucasginard.dolarpy.view.home

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lucasginard.dolarpy.R
import com.lucasginard.dolarpy.utils.setBackground
import com.lucasginard.dolarpy.databinding.ActivityHomeBinding
import com.lucasginard.dolarpy.utils.setAppLocale
import com.lucasginard.dolarpy.view.cotizacionFragment
import com.lucasginard.dolarpy.view.home.fragments.infoFragment
import com.lucasginard.dolarpy.view.home.fragments.ubicacionFragment
import java.util.*


class Home : AppCompatActivity() {

    private lateinit var bindding:ActivityHomeBinding
    private lateinit var preferences: SharedPreferences
    private var idSave:Int = R.id.nav_coti

    private var flat = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(bindding.root)
        configureUI()

        bindding.navView.setOnNavigationItemSelectedListener()
        bindding.navView.selectedItemId = R.id.nav_coti

        val fm: FragmentManager = supportFragmentManager
        val ft: FragmentTransaction = fm.beginTransaction()
        ft.replace(bindding.fragmentHome.id, cotizacionFragment.newInstance())
        ft.commit()

        if (savedInstanceState != null) {
            idSave = savedInstanceState.getInt("idSave")
        }
    }

    private fun configureUI() {
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
            val mode = this.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            val saveMode = preferences.getInt("saveMode",3)
//            if (mode != saveMode && saveMode != 3){
//                when(saveMode){
//                    1 ->{
//                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//                    }
//
//                    0 ->{
//                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//                    }
//                }
//            }
        }
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }

    override fun onResume() {
        super.onResume()
        configureUI()
        bindding.navView.selectedItemId = idSave
        bindding.navView.setOnNavigationItemSelectedListener()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("idSave", idSave);
    }

    private fun BottomNavigationView.setOnNavigationItemSelectedListener() {
        setOnNavigationItemSelectedListener {
            val fm: FragmentManager = supportFragmentManager
            val ft: FragmentTransaction = fm.beginTransaction()

            when(it.itemId){
                R.id.nav_ubi ->{
                    ft.replace(bindding.fragmentHome.id, ubicacionFragment.newInstance())
                    ft.commit()
                    idSave = it.itemId
                }
                R.id.nav_coti ->{
                    ft.replace(bindding.fragmentHome.id, cotizacionFragment.newInstance())
                    ft.commit()
                    idSave = it.itemId
                }
                R.id.nav_info ->{
                    ft.replace(bindding.fragmentHome.id, infoFragment.newInstance())
                    ft.commit()
                    idSave = it.itemId
                }
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

}


