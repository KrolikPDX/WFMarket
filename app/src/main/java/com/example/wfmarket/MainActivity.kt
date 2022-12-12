package com.example.wfmarket
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.util.concurrent.Executor


class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val foundEmail: String = findViewById<Button>(R.id.loginButton).text.toString()
        Log.i("Print", "Found email to be = $foundEmail")
        findViewById<Button>(R.id.loginButton).setOnClickListener(loginButtonClicked())
        val skipButton = findViewById<Button>(R.id.skipLoginButton)
        skipButton.setOnClickListener(skipLoginButtonClicked(skipButton))
    }

    private fun loginButtonClicked(): View.OnClickListener {
        return View.OnClickListener {
            var text:String = findViewById<EditText>(R.id.emailTextBox).text.toString()
            var password:String = findViewById<EditText>(R.id.passwordTextBox).text.toString()
            //Attempt to login to warframe marketplace
            Log.i("Print", "Email: $text")
        }
    }

    private fun skipLoginButtonClicked(skipButton: Button): View.OnClickListener {
        return View.OnClickListener {
            skipButton.text = "Loading..."
            val exec:Exec = Exec()
            exec.execute(executeRequest("Skip", skipButton))

        }
    }

    private fun executeRequest(text: String, button: Button): Runnable {
        return Runnable {
            //val response = ApiBuilder().executeRequest("https://api.warframe.market/v1/items")
            //Log.i("Print", "Response = $response")
            val intent = Intent(this, MainMenu::class.java)
            startActivity(intent)
            Thread.sleep(1_000)
            button.text = text
        }
    }



    class Exec : Executor {
        override fun execute(command: Runnable) {
            Thread(command).start()
        }
    }


}


