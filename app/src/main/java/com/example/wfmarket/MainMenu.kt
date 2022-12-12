package com.example.wfmarket

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MainMenu: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        findViewById<Button>(R.id.backButton).setOnClickListener(backButtonClicked())

    }

    private fun backButtonClicked(): View.OnClickListener {
        return View.OnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            finish()
        }
    }
}