package com.example.wfmarket
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.wfmarket.helpers.ApiBuilder
import com.example.wfmarket.models.payloads.AuthSigninPayload
import com.example.wfmarket.models.response.authSigninResponse
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

const val TAG:String = "Print"
const val authUrl:String = "https://api.warframe.market/v1/auth/signin"
const val jwtToken = "JWT eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzaWQiOiJmWTVNb2xLNjJ3REVQa0tJdkdySmdHWFpzVXpYOW5hVCIsImNzcmZfdG9rZW4iOiJkM2IzOWZmNzNlZTZkMDQ1MjExOGU0ZjI2NjlkNWEzYmNkNTVmZTMzIiwiZXhwIjoxNjc2NDQwNzQ1LCJpYXQiOjE2NzEyNTY3NDUsImlzcyI6Imp3dCIsImF1ZCI6Imp3dCIsImF1dGhfdHlwZSI6ImNvb2tpZSIsInNlY3VyZSI6dHJ1ZSwibG9naW5fdWEiOiJiJ01vemlsbGEvNS4wIChNYWNpbnRvc2g7IEludGVsIE1hYyBPUyBYIDEwXzE1XzcpIEFwcGxlV2ViS2l0LzUzNy4zNiAoS0hUTUwsIGxpa2UgR2Vja28pIENocm9tZS8xMDguMC4wLjAgU2FmYXJpLzUzNy4zNiciLCJsb2dpbl9pcCI6ImInMjYwMToxYzI6NTAwMjo1NDA6OGRlNDpiMDk0OjY3OTg6MjdlNiciLCJqd3RfaWRlbnRpdHkiOiIzMTd1U3lobXdTdFlxVFNiWlpoM2tRd1JkTDdrQ0J3eiJ9.d0rdvgwNX5pPRLjSpMJn28tLOuvCuIK3wZxa6VOWQtQ"


private lateinit var emailTextView:TextView
private lateinit var passwordTextView:TextView
private lateinit var loginButton:Button
private lateinit var skipButton: Button
private lateinit var apiView: TextView
private lateinit var mainMenu:Intent

var apiBuilder = ApiBuilder()



class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        StrictMode.setThreadPolicy(ThreadPolicy.Builder().permitAll().build())
        setupParams()
    }

    private fun loginButtonClicked(): View.OnClickListener {
        return View.OnClickListener {
            //Coroutine: Update button, send request, open main menu, update button
            GlobalScope.launch(Dispatchers.IO) {
                this@MainActivity.runOnUiThread(java.lang.Runnable { loginButton.text = "Loading" })
                //val loginBody = mutableMapOf("email" to "josephd8888@hotmail.com", "password" to "JosephD8888")
                val authSigninPayload = AuthSigninPayload("josephd8888@hotmail.com", "JosephD8888")
                val payload = Gson().toJson(authSigninPayload)
                apiBuilder.setupPostRequest(authUrl, payload)
                apiBuilder.addHeader("authorization", jwtToken)
                val response = apiBuilder.executeRequest()
                //val authSigninResponse = authSigninResponse.fromJson(response)
                Log.i(TAG, response)


                this@MainActivity.runOnUiThread(java.lang.Runnable { apiView.text = response })
                //this@MainActivity.runOnUiThread(java.lang.Runnable { apiView.text = authSigninResponse!!.payload.user.id })
                Thread.sleep(3000)
                //startActivity(mainMenu)
                Thread.sleep(1000)
                this@MainActivity.runOnUiThread(java.lang.Runnable { apiView.text = "" })
                this@MainActivity.runOnUiThread(java.lang.Runnable { loginButton.text = "Login" })
            }
        }
    }

    private fun skipLoginButtonClicked(): View.OnClickListener {
        return View.OnClickListener {
            startActivity(mainMenu)
        }
    }

    private fun setupParams() {
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


