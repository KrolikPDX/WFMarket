package com.example.wfmarket.pageLogic
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.example.wfmarket.R
import com.example.wfmarket.models.responses.auth.AuthSigninResponse
import com.example.wfmarket.models.responses.auth.User
import com.example.wfmarket.models.responses.tradableItems.TradableItems
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson

var user: User? = null

class HomePageLogic: AppCompatActivity(){
    private lateinit var drawerLayout:DrawerLayout
    private lateinit var navigationView:NavigationView
    private lateinit var navigationHeader:LinearLayout
    private lateinit var navigationUserName:TextView
    private lateinit var navToggle:ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        setupParams()
        setupNavigation()
        //setupTradeData()
    }

    private fun setupParams() {
        var authSigningResponse:String?
        try {
            authSigningResponse = preferences.getString("AuthSigninResponse", "")
            user = Gson().fromJson(authSigningResponse, AuthSigninResponse::class.java).payload.user
        } catch(e:Exception) {
            Log.i("Print", "Could not find authSignin in preferences ${e.stackTrace}")
        }

        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)
        navToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)

        val navigationHeader: LinearLayout = navigationView.getHeaderView(0) as LinearLayout
        val navigationUserName: TextView = navigationHeader.findViewById(R.id.user_name)
        if (user != null && user?.ingame_name != null) {
            navigationUserName.text = user?.ingame_name
        } else {
            navigationUserName.text = "No Username Set!"
        }
    }

    private fun setupTradeData() {
        apiBuilder.setupGetRequest(getItemsUrl)
        var rawResponse = apiBuilder.executeRequest()
        tradableItems = Gson().fromJson(rawResponse, TradableItems::class.java)
    }

    private fun setupNavigation() {
        drawerLayout.addDrawerListener(navToggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navigationView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.navigation_buySell -> Log.i(TAG, "Clicked on Buy Sell!")
                R.id.navigation_contracts -> Toast.makeText(applicationContext, "Clicked Items from menu!", Toast.LENGTH_SHORT)
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (navToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}