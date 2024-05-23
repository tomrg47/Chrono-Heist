package com.example.chronoheist

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TableLayout
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


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val playBttn = findViewById<ImageButton>(R.id.playBttn)
        val playFrame = findViewById<FrameLayout>(R.id.PlayBttnFrame)
        val optionsFrame = findViewById<FrameLayout>(R.id.OptionsBttnFrame)
        val armouryFrame = findViewById<FrameLayout>(R.id.ArmouryBttnFrame)
        val battlepassFrame = findViewById<FrameLayout>(R.id.BattlePassBttnFrame)
        val mapSelect = findViewById<TableLayout>(R.id.MapSelectTable)
        val lvl1Bttn = findViewById<ImageButton>(R.id.level1Bttn)


        playBttn.setOnClickListener {
            playFrame.visibility = View.GONE
            optionsFrame.visibility = View.GONE
            armouryFrame.visibility = View.GONE
            battlepassFrame.visibility = View.GONE
            mapSelect.visibility = View.VISIBLE
        }
        lvl1Bttn.setOnClickListener{
            val intent = Intent (this, Level1Activity::class.java)
            startActivity(intent)
        }
    }
}