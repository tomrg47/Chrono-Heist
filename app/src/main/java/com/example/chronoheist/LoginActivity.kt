package com.example.chronoheist

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginActivity : AppCompatActivity() {

    private lateinit var usernameInput : EditText
    private lateinit var passwordInput : EditText
    private lateinit var loginBtn : Button

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        usernameInput = findViewById(R.id.username_input)
        passwordInput=  findViewById(R.id.password_input)
        loginBtn = findViewById(R.id.login_btn)
        val login = findViewById<LinearLayout>(R.id.LogInLayout)
        val signup = findViewById<LinearLayout>(R.id.SignUpLayout)
        val signUpBtn = findViewById<Button>(R.id.createAccount_btn)

        loginBtn.setOnClickListener {
            val username = usernameInput.text.toString()
            val password = passwordInput.text.toString()
            Log.i ("Test Credentials", "Username : $username and Password : $password ")
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("USERNAME", username)
            //intent.putExtra("PASSWORD", password)
            startActivity(intent)
        }
        signUpBtn.setOnClickListener{
            login.visibility =View.GONE
            signup.visibility= View.VISIBLE
        }
    }
}