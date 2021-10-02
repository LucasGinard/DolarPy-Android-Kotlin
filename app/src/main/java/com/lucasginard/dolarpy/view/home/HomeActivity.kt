package com.lucasginard.dolarpy.view.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lucasginard.dolarpy.R
import com.lucasginard.dolarpy.utils.setBackground
import com.lucasginard.dolarpy.databinding.ActivityHomeBinding
import com.lucasginard.dolarpy.utils.OnHorizontalSwipeListener
import com.lucasginard.dolarpy.utils.setAppLocale
import com.lucasginard.dolarpy.view.home.fragments.CotizacionFragment
import com.lucasginard.dolarpy.view.home.fragments.InfoFragment
import com.lucasginard.dolarpy.view.home.fragments.UbicacionFragment
import java.util.*


class HomeActivity : AppCompatActivity() {

    private lateinit var preferences: SharedPreferences
    private lateinit var bindding:ActivityHomeBinding
    private var idSave:Int = R.id.nav_coti

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(bindding.root)
        configureUI()

        bindding.navView.setOnNavigationItemSelectedListener()
        bindding.navView.selectedItemId = R.id.nav_coti

        val fm: FragmentManager = supportFragmentManager
        val ft: FragmentTransaction = fm.beginTransaction()
        ft.replace(bindding.fragmentHome.id, CotizacionFragment.newInstance())
        ft.commit()

        if (savedInstanceState != null) {
            idSave = savedInstanceState.getInt("idSave")
        }
        listenHorizontalSwipe(bindding.fragmentHome)
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

    fun listenHorizontalSwipe(view: View) {
        view.setOnTouchListener(object : OnHorizontalSwipeListener(view.context) {

            override fun onRightSwipe() {
                when(bindding.navView.selectedItemId ){
                    R.id.nav_coti ->{
                        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
                        ft.replace(bindding.fragmentHome.id, InfoFragment.newInstance())
                        bindding.navView.selectedItemId = R.id.nav_info
                        ft.commit()
                    }
                    R.id.nav_ubi ->{
                        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
                        ft.replace(bindding.fragmentHome.id, CotizacionFragment.newInstance())
                        bindding.navView.selectedItemId = R.id.nav_coti
                        ft.commit()
                    }
                }

            }

            override fun onLeftSwipe() {
                when(bindding.navView.selectedItemId){
                    R.id.nav_info ->{
                        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
                        ft.replace(bindding.fragmentHome.id, CotizacionFragment.newInstance())
                        bindding.navView.selectedItemId = R.id.nav_coti
                        ft.commit()
                    }
                    R.id.nav_coti ->{
                        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
                        ft.replace(bindding.fragmentHome.id, UbicacionFragment.newInstance())
                        bindding.navView.selectedItemId = R.id.nav_ubi
                        ft.commit()
                    }
                }
            }
        })
    }


    private fun BottomNavigationView.setOnNavigationItemSelectedListener() {
        setOnNavigationItemSelectedListener {
            val fm: FragmentManager = supportFragmentManager
            val ft: FragmentTransaction = fm.beginTransaction()

            when(it.itemId){
                R.id.nav_ubi ->{
                    ft.replace(bindding.fragmentHome.id, UbicacionFragment.newInstance())
                    ft.commit()
                    idSave = it.itemId
                }
                R.id.nav_coti ->{
                    ft.replace(bindding.fragmentHome.id, CotizacionFragment.newInstance())
                    ft.commit()
                    idSave = it.itemId
                }
                R.id.nav_info ->{
                    ft.replace(bindding.fragmentHome.id, InfoFragment.newInstance())
                    ft.commit()
                    idSave = it.itemId
                }
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

}


