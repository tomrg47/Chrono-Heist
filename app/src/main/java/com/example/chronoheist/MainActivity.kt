package com.example.chronoheist

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        val playButtonFrame = findViewById<FrameLayout>(R.id.PlayBttnFrame)
        val playButtonText = findViewById<TextView>(R.id.pause_options_text)
        playButtonFrame.visibility = View.VISIBLE
        playButtonText.visibility = View.VISIBLE
        Log.d("Debug", "Play Button Frame visibility: ${playButtonFrame.visibility}")
        Log.d("Debug", "Play Button Text visibility: ${playButtonText.visibility}")


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}