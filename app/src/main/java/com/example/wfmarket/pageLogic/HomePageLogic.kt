package com.example.wfmarket.pageLogic
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
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
    private lateinit var toggle:ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        setupParams()
        //setupTradeData()
        setupNavigation()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.i("Print", "Checking if item match: $item")
        if (toggle.onOptionsItemSelected(item)) {
            Log.i("Print", "Clicked on item!")
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupParams() {
        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)

        var authSigningResponse:String?
        try {
            authSigningResponse = preferences.getString("AuthSigninResponse", "")
            user = Gson().fromJson(authSigningResponse, AuthSigninResponse::class.java).payload.user
        } catch(e:Exception) {
            Log.i("Print", "Could not find authSignin in preferences ${e.stackTrace}")
        }
    }

    private fun setupNavigation() {
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true) //Adds back arrow to close open menu
        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_buySell -> Log.i(TAG, "Clicked on buy Sell")
                R.id.navigation_items -> Toast.makeText(applicationContext, "Clicked on Items", Toast.LENGTH_SHORT).show()
                R.id.navigation_contracts -> Toast.makeText(applicationContext, "Clicked on Contracts", Toast.LENGTH_SHORT).show()
                R.id.navigation_lich -> Toast.makeText(applicationContext, "Clicked on Lich", Toast.LENGTH_SHORT).show()
            }
            true
        }
    }

    private fun setupTradeData() {
        apiBuilder.setupGetRequest(getItemsUrl)
        var rawResponse = apiBuilder.executeRequest()
        tradableItems = Gson().fromJson(rawResponse, TradableItems::class.java)
    }
}