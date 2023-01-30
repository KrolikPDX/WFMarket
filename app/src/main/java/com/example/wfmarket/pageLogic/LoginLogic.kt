package com.example.wfmarket.pageLogic
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.graphics.Bitmap
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.wfmarket.R
import com.example.wfmarket.helpers.ApiBuilder
import com.example.wfmarket.models.payloads.AuthSigninPayload
import com.example.wfmarket.models.responses.TradableItemImage
import com.example.wfmarket.models.responses.tradableItems.TradableItems
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Dictionary

const val TAG:String = "Print"
const val authUrl:String = "https://api.warframe.market/v1/auth/signin"
const val getItemsUrl:String = "https://api.warframe.market/v1/items"
const val getItemOrders:String = "https://api.warframe.market/v1/items/{url_name}/orders"
//eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzaWQiOiJCbmhVQVRDNllVbVpKNElkaHlJTlNuNDF5QWJKSXdZUCIsImNzcmZfdG9rZW4iOiJhZDgzNGYxOGRjOGNmNzM0ZTFlNTI0ZTcxNmExNmQzZTRlODNkYzhkIiwiZXhwIjoxNjc3NzE5NDUxLCJpYXQiOjE2NzI1MzU0NTEsImlzcyI6Imp3dCIsImF1ZCI6Imp3dCIsImF1dGhfdHlwZSI6ImNvb2tpZSIsInNlY3VyZSI6dHJ1ZSwibG9naW5fdWEiOiJiJ01vemlsbGEvNS4wIChXaW5kb3dzIE5UIDEwLjA7IFdpbjY0OyB4NjQpIEFwcGxlV2ViS2l0LzUzNy4zNiAoS0hUTUwsIGxpa2UgR2Vja28pIENocm9tZS8xMDguMC4wLjAgU2FmYXJpLzUzNy4zNiciLCJsb2dpbl9pcCI6ImInMjYwMToxYzI6NTAwMTozNWYwOjMwNGQ6Zjg1Nzo2MzNiOjZkNWQnIiwiand0X2lkZW50aXR5IjoiV3FmbXE0dkc4a2cwSENYQnpVYTBGenpqTVhzNVlqQjUifQ.OAxVr5x4F0sm7ZjJBeNT7YGw9XoE5GCsm9AnuD1GYH0
const val jwtToken = ""

//Globals
var apiBuilder = ApiBuilder()
lateinit var preferences: SharedPreferences
lateinit var prefEditor:Editor
lateinit var tradableItems:TradableItems
lateinit var tradableItemImages: TradableItemImage //Setup in ItemInfoFragment

class LoginLogic : AppCompatActivity(){
    private lateinit var emailTextView:TextView
    private lateinit var passwordTextView:TextView
    private lateinit var loginButton:Button
    private lateinit var skipButton: Button
    private lateinit var apiView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var homePageIntent:Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        StrictMode.setThreadPolicy(ThreadPolicy.Builder().permitAll().build()) //Enable internet usage
        setup()
        if (preferences.getString("AuthSigninResponse", "")?.isEmpty() != true) {
            startActivity(homePageIntent)
        }
    }

    private fun loginButtonClicked(): View.OnClickListener {
        return View.OnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                enableProgressBar(true)
                //Change to pull data from
                var authSigninPayload = AuthSigninPayload(emailTextView.text.toString(), passwordTextView.text.toString())
                var payload = Gson().toJson(authSigninPayload)
                apiBuilder.setupPostRequest(authUrl, payload)
                apiBuilder.addHeader("authorization", jwtToken)
                var rawResponse = apiBuilder.executeRequest()
                enableProgressBar(false)
                if (apiBuilder.requestExecutedSuccess()) {
                    changeViewText(apiView, rawResponse)
                    prefEditor.putString("AuthSigninResponse", rawResponse).commit()
                    Thread.sleep(5000)
                    startActivity(homePageIntent)
                    changeViewText(apiView)
                } else {
                    changeViewText(apiView, "Login failed due to $rawResponse")
                    Thread.sleep(5000)
                }
                changeViewText(apiView)
            }
        }
    }



    private fun skipLoginButtonClicked(): View.OnClickListener {
        return View.OnClickListener {
            enableProgressBar(true)
            startActivity(homePageIntent)
        }
    }

    private fun setup() {
        preferences = getPreferences(MODE_PRIVATE)
        prefEditor = preferences.edit()
        //prefEditor.clear().commit() //Clear any saved data

        homePageIntent = Intent(this, HomePageLogic::class.java)
        emailTextView = findViewById(R.id.emailTextBox)
        passwordTextView = findViewById(R.id.passwordTextBox)

        loginButton = findViewById(R.id.loginButton)
        loginButton.setOnClickListener(loginButtonClicked())

        skipButton = findViewById(R.id.skipLoginButton)
        skipButton.setOnClickListener(skipLoginButtonClicked())

        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.INVISIBLE

        apiView = findViewById(R.id.apiView)
    }

    private fun changeViewText (textView: TextView, text:String = "") {
        this@LoginLogic.runOnUiThread { textView.text = text }
    }

    private fun changeButtonText(button:Button, text:String = "") {
        this@LoginLogic.runOnUiThread { button.text = text}
    }

    private fun enableProgressBar(enabled: Boolean) {
        this@LoginLogic.runOnUiThread {
            if (enabled)
                progressBar.visibility = View.VISIBLE
            else
                progressBar.visibility = View.INVISIBLE

        }
    }
}


