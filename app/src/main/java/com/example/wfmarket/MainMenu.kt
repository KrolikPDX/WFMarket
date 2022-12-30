package com.example.wfmarket
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.wfmarket.models.response.AuthSigninResponse
import com.example.wfmarket.models.response.User
import com.google.gson.Gson

private lateinit var backButton: Button
private lateinit var displayView: TextView

var user:User? = null


class MainMenu: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        setupParams()
    }

    private fun backButtonClicked(): View.OnClickListener {
        return View.OnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            finish()
        }
    }

    private fun setupParams() {
        var authSigningResponse:String? = null
        try {
            authSigningResponse = preferences.getString("AuthSigninResponse", "")
            user = Gson().fromJson(authSigningResponse, AuthSigninResponse::class.java).payload.user
        } catch(e:Exception) {
            Log.i("Print", "Could not find authSignin in preferences ${e.stackTrace}")
        }
        backButton = findViewById(R.id.backButton)
        backButton.setOnClickListener(backButtonClicked())
        displayView = findViewById(R.id.displayView)
        if (user != null) {
            displayView.text = "User exists!"
        } else {
            displayView.text = "User not found!"
        }
    }
}