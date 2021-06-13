package com.lucasginard.dolarpy.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lucasginard.dolarpy.R
import com.lucasginard.dolarpy.databinding.ActivityHomeBinding
import com.lucasginard.dolarpy.ui.cotizacionFragment
import com.lucasginard.dolarpy.ui.home.fragments.infoFragment
import com.lucasginard.dolarpy.ui.home.fragments.ubicacionFragment


class Home : AppCompatActivity() {

    private lateinit var bindding:ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(bindding.root)


        bindding.navView.setOnNavigationItemSelectedListener()
        bindding.navView.selectedItemId = R.id.nav_coti

        val fm: FragmentManager = supportFragmentManager
        val ft: FragmentTransaction = fm.beginTransaction()
        ft.replace(bindding.fragmentHome.id, cotizacionFragment.newInstance())
        ft.commit()
    }

    override fun onBackPressed() {

    }
    private fun BottomNavigationView.setOnNavigationItemSelectedListener() {
        setOnNavigationItemSelectedListener {
            val fm: FragmentManager = supportFragmentManager
            val ft: FragmentTransaction = fm.beginTransaction()

            when(it.itemId){
                R.id.nav_ubi ->{
                    ft.replace(bindding.fragmentHome.id, ubicacionFragment.newInstance())
                    ft.commit()
                }
                R.id.nav_coti ->{
                    ft.replace(bindding.fragmentHome.id, cotizacionFragment.newInstance())
                    ft.commit()
                }
                R.id.nav_info ->{
                    ft.replace(bindding.fragmentHome.id, infoFragment.newInstance())
                    ft.commit()
                }
            }
            return@setOnNavigationItemSelectedListener true
        }
    }
}


