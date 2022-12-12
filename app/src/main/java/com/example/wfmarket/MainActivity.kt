package com.example.wfmarket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.loginButton).setOnClickListener(loginButtonClicked())
        findViewById<Button>(R.id.skipLoginButton).setOnClickListener(skipButtonClicked())
    }

    private fun loginButtonClicked(): View.OnClickListener {
        return View.OnClickListener {
            var text:String = findViewById<EditText>(R.id.emailTextBox).text.toString()
            var password:String = findViewById<EditText>(R.id.passwordTextBox).text.toString()
            //Attempt to login to warframe marketplace
            //Log.i("Print", "Email: $text")
        }
    }

    private fun skipButtonClicked(): View.OnClickListener {
        return View.OnClickListener {
            //Move on to next screen and remember option
        }
    }
}