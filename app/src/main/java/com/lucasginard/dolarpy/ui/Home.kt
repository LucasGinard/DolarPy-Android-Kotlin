package com.lucasginard.dolarpy.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.lucasginard.dolarpy.databinding.ActivityHomeBinding


class Home : AppCompatActivity() {

    private lateinit var bindding:ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(bindding.root)


        val fm: FragmentManager = supportFragmentManager
        val ft: FragmentTransaction = fm.beginTransaction()
        ft.replace(bindding.fragmentHome.id, fragment_inicio.newInstance())
        ft.commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()

    }
}