package com.example.chronoheist

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
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
        val optionsBttn = findViewById<ImageButton>(R.id.optionsBttn)
        val backBttn  = findViewById<ImageButton>(R.id.backBttn)
        val armouryBttn = findViewById<ImageButton>(R.id.armouryBttn)
        val battlepass = findViewById<ImageButton>(R.id.battlesPassBttn)
        val playFrame = findViewById<FrameLayout>(R.id.PlayBttnFrame)
        val optionsFrame = findViewById<FrameLayout>(R.id.OptionsBttnFrame)
        val armouryFrame = findViewById<FrameLayout>(R.id.ArmouryBttnFrame)
        val battlepassFrame = findViewById<FrameLayout>(R.id.BattlePassBttnFrame)
        val mapSelect = findViewById<TableLayout>(R.id.MapSelectTable)
        val lvl1Bttn = findViewById<ImageButton>(R.id.level1Bttn)
        val optionsLayout = findViewById<FrameLayout>(R.id.optionsMenu)
        val armouryBack = findViewById<ImageButton>(R.id.armouryBackBttn)
        val armouryMenu = findViewById<FrameLayout>(R.id.armouryMenu)
        val chronoHeist = findViewById<ImageView>(R.id.Chrono_Heist)
        val battlepassMenu = findViewById<FrameLayout>(R.id.battlepassMenu)
        val battlepassBack = findViewById<ImageButton>(R.id.battlepassBackBttn)
        val mapSelectBack = findViewById<ImageButton>(R.id.mapSelectBackBttn)

        playBttn.setOnClickListener {
            playFrame.visibility = View.GONE
            optionsFrame.visibility = View.GONE
            armouryFrame.visibility = View.GONE
            battlepassFrame.visibility = View.GONE
            mapSelect.visibility = View.VISIBLE
        }
        optionsBttn.setOnClickListener {
            playFrame.visibility = View.GONE
            optionsFrame.visibility = View.GONE
            armouryFrame.visibility = View.GONE
            battlepassFrame.visibility = View.GONE
            optionsLayout.visibility = View.VISIBLE
        }
        battlepass.setOnClickListener {
            playFrame.visibility = View.GONE
            optionsFrame.visibility = View.GONE
            armouryFrame.visibility = View.GONE
            battlepassFrame.visibility = View.GONE
            battlepassMenu.visibility = View.VISIBLE
            chronoHeist.visibility = View.GONE
        }
        armouryBttn.setOnClickListener {
            playFrame.visibility = View.GONE
            optionsFrame.visibility = View.GONE
            armouryFrame.visibility = View.GONE
            battlepassFrame.visibility = View.GONE
            armouryMenu.visibility = View.VISIBLE
            chronoHeist.visibility = View.GONE
        }
        backBttn.setOnClickListener {
            playFrame.visibility = View.VISIBLE
            optionsFrame.visibility = View.VISIBLE
            armouryFrame.visibility = View.VISIBLE
            battlepassFrame.visibility = View.VISIBLE
            optionsLayout.visibility = View.GONE
        }
        armouryBack.setOnClickListener {
            playFrame.visibility = View.VISIBLE
            optionsFrame.visibility = View.VISIBLE
            armouryFrame.visibility = View.VISIBLE
            battlepassFrame.visibility = View.VISIBLE
            armouryMenu.visibility = View.GONE
            chronoHeist.visibility= View.VISIBLE
        }
        battlepassBack.setOnClickListener {
            playFrame.visibility = View.VISIBLE
            optionsFrame.visibility = View.VISIBLE
            armouryFrame.visibility = View.VISIBLE
            battlepassFrame.visibility = View.VISIBLE
            battlepassMenu.visibility = View.GONE
            chronoHeist.visibility= View.VISIBLE
        }
        mapSelectBack.setOnClickListener {
            playFrame.visibility = View.VISIBLE
            optionsFrame.visibility = View.VISIBLE
            armouryFrame.visibility = View.VISIBLE
            battlepassFrame.visibility = View.VISIBLE
            mapSelect.visibility = View.GONE

        }


        lvl1Bttn.setOnClickListener{
            val intent = Intent (this, Level1Activity::class.java)
            startActivity(intent)
        }
    }
}