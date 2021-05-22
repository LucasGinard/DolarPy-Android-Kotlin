package com.lucasginard.dolarpy.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.lucasginard.dolarpy.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var bindding: ActivityMainBinding
    private val SPLASH_DISPLAY_LENGTH = 20000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            val i = Intent(this@MainActivity, Home::class.java)
            startActivity(i)
        }, SPLASH_DISPLAY_LENGTH.toLong())

    }
}