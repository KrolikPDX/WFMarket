package com.example.wfmarket.pageLogic
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.wfmarket.R
import com.example.wfmarket.helpers.ApiBuilder
import com.example.wfmarket.models.payloads.AuthSigninPayload
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


const val TAG:String = "Print"
const val authUrl:String = "https://api.warframe.market/v1/auth/signin"
const val jwtToken = "JWT eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzaWQiOiJmWTVNb2xLNjJ3REVQa0tJdkdySmdHWFpzVXpYOW5hVCIsImNzcmZfdG9rZW4iOiJkM2IzOWZmNzNlZTZkMDQ1MjExOGU0ZjI2NjlkNWEzYmNkNTVmZTMzIiwiZXhwIjoxNjc2NDQwNzQ1LCJpYXQiOjE2NzEyNTY3NDUsImlzcyI6Imp3dCIsImF1ZCI6Imp3dCIsImF1dGhfdHlwZSI6ImNvb2tpZSIsInNlY3VyZSI6dHJ1ZSwibG9naW5fdWEiOiJiJ01vemlsbGEvNS4wIChNYWNpbnRvc2g7IEludGVsIE1hYyBPUyBYIDEwXzE1XzcpIEFwcGxlV2ViS2l0LzUzNy4zNiAoS0hUTUwsIGxpa2UgR2Vja28pIENocm9tZS8xMDguMC4wLjAgU2FmYXJpLzUzNy4zNiciLCJsb2dpbl9pcCI6ImInMjYwMToxYzI6NTAwMjo1NDA6OGRlNDpiMDk0OjY3OTg6MjdlNiciLCJqd3RfaWRlbnRpdHkiOiIzMTd1U3lobXdTdFlxVFNiWlpoM2tRd1JkTDdrQ0J3eiJ9.d0rdvgwNX5pPRLjSpMJn28tLOuvCuIK3wZxa6VOWQtQ"

var apiBuilder = ApiBuilder()
lateinit var preferences: SharedPreferences
lateinit var prefEditor:Editor

private lateinit var emailTextView:TextView
private lateinit var passwordTextView:TextView
private lateinit var loginButton:Button
private lateinit var skipButton: Button
private lateinit var apiView: TextView
private lateinit var mainMenu:Intent



class LoginLogic : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        StrictMode.setThreadPolicy(ThreadPolicy.Builder().permitAll().build()) //Enable internet usage
        setup()
        if (preferences.getString("AuthSigninResponse", "")?.isEmpty() != true) {
            startActivity(mainMenu)
        }
    }

    private fun loginButtonClicked(): View.OnClickListener {
        return View.OnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                changeButtonText(loginButton, "Loading")
                //Change to pull data from
                var authSigninPayload = AuthSigninPayload(emailTextView.text.toString(), passwordTextView.text.toString())
                var payload = Gson().toJson(authSigninPayload)
                apiBuilder.setupPostRequest(authUrl, payload)
                apiBuilder.addHeader("authorization", jwtToken)

                var rawResponse = apiBuilder.executeRequest()
                if (apiBuilder.requestExecutedSuccess()) {
                    changeViewText(apiView, rawResponse)
                    prefEditor.putString("AuthSigninResponse", rawResponse).commit()
                    startActivity(mainMenu)
                    Thread.sleep(1000)
                    changeViewText(apiView)
                } else {
                    Thread.sleep(1000)
                    changeViewText(apiView, "Login failed!")
                }
                Thread.sleep(1000)
                changeButtonText(loginButton, "Login")
                changeViewText(apiView)
            }
        }
    }

    private fun changeViewText (textView: TextView, text:String = "") {
        this@LoginLogic.runOnUiThread(java.lang.Runnable { textView.text = text})
    }

    private fun changeButtonText(button:Button, text:String = "") {
        this@LoginLogic.runOnUiThread(java.lang.Runnable { button.text = text})
    }

    private fun skipLoginButtonClicked(): View.OnClickListener {
        return View.OnClickListener {
            startActivity(mainMenu)
        }
    }

    private fun setup() {
        preferences = getPreferences(MODE_PRIVATE)
        prefEditor = preferences.edit()
        prefEditor.clear().commit() //Clear any saved data

        mainMenu = Intent(this, MainMenu::class.java)
        emailTextView = findViewById(R.id.emailTextBox)
        passwordTextView = findViewById(R.id.passwordTextBox)

        loginButton = findViewById(R.id.loginButton)
        loginButton.setOnClickListener(loginButtonClicked())

        skipButton = findViewById(R.id.skipLoginButton)
        skipButton.setOnClickListener(skipLoginButtonClicked())

        apiView = findViewById(R.id.apiView)
    }
}


