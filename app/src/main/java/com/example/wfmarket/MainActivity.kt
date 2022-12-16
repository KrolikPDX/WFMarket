package com.example.wfmarket
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.wfmarket.helpers.ApiBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

const val TAG:String = "Print"

private lateinit var emailTextView:TextView
private lateinit var passwordTextView:TextView
private lateinit var loginButton:Button
private lateinit var skipButton: Button
private lateinit var apiView: TextView
private lateinit var mainMenu:Intent


class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        StrictMode.setThreadPolicy(ThreadPolicy.Builder().permitAll().build())
        setupParams()
    }

    private fun loginButtonClicked(): View.OnClickListener {
        return View.OnClickListener {
            //var email:String = emailTextView.text.toString()

            //Coroutine: Update button, send request, open main menu, update button
            GlobalScope.launch(Dispatchers.IO) {
                this@MainActivity.runOnUiThread(java.lang.Runnable { loginButton.text = "Loading" })
                val response = ApiBuilder().executeRequest("https://api.warframe.market/v1/items")
                this@MainActivity.runOnUiThread(java.lang.Runnable { apiView.text = response })
                Thread.sleep(3000)
                startActivity(mainMenu)
                Thread.sleep(1000)
                this@MainActivity.runOnUiThread(java.lang.Runnable { apiView.text = "" })
                this@MainActivity.runOnUiThread(java.lang.Runnable { loginButton.text = "Login?" })
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


