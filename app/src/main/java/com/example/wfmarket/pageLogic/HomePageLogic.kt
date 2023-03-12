package com.example.wfmarket.pageLogic
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.wfmarket.R
import com.example.wfmarket.models.responses.auth.AuthSigninResponse
import com.example.wfmarket.models.responses.auth.User
import com.example.wfmarket.models.responses.tradableItems.TradableItems
import com.example.wfmarket.pageLogic.fragments.AllItemsFragment
import com.example.wfmarket.pageLogic.fragments.BuySellFragment
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson


var user: User? = null

/*
TODO:
- Overall -
    -> Update theme / UI

- AllItemsFragment -
    -> Update CardView to include cropped picture with title underneath
        -> Fav item icon if user is logged in?
    -> Add sort by tag -> Get list of all tags
    -> Add search by name -> List relevant items mid type
    -> Item name in all caps

- ItemDetailsFragment -
    -> Back button to go to previous fragment
    -> Setup basic details
    -> List other items in set if any (https://www.geeksforgeeks.org/gridview-in-android-with-example/)
        -> Setup gridview to display a grid of items with 2 columns
        -> Create itemsInSet adapter to add values to each card view
            -> Pass list of items in set to adapter
            -> Gridview.setAdapter(adapter)

 */

class HomePageLogic: AppCompatActivity(){
    private lateinit var toolbar: Toolbar
    private lateinit var toggle:ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    private lateinit var fragmentView: FrameLayout
    private val buySellFragment = BuySellFragment()
    private val allItemsFragment = AllItemsFragment()

    private lateinit var navigationUserName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        setupParams()
        setupSignin()
        setupGlobalTradeData()
        setupNavigation()
        setupFragments()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupParams() {
        toolbar = findViewById(R.id.navigation_toolbar)

        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)
        fragmentView = findViewById(R.id.fragmentFrame)

        navigationUserName = navigationView.getHeaderView(0).findViewById(R.id.navigation_user_name)
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
    }

    private fun setupSignin() {
        try {
            val authSigningResponse = preferences.getString("AuthSigninResponse", "")
            user = Gson().fromJson(authSigningResponse, AuthSigninResponse::class.java).payload.user
        } catch(e:Exception) {
            Log.i(TAG, "Could not find authSignin in preferences ${e.stackTrace}")
        }
    }

    private fun setupNavigation() {
        setSupportActionBar(toolbar)
        drawerLayout.addDrawerListener(toggle)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) //Enables click-ability

        //Setup navigation header text
        if (user != null && user?.ingame_name != null) {
            navigationUserName.text = user?.ingame_name
        } else if (user != null) {
            navigationUserName.text = "Please verify account at warframe.market"
        } else {
            navigationUserName.text = "Not signed in."
        }

        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_buySell -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(fragmentView.id, buySellFragment)
                        commit()
                    }
                }
                R.id.navigation_items -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(fragmentView.id, allItemsFragment)
                        commit()
                    }
                }
                R.id.navigation_contracts -> Toast.makeText(applicationContext, "Clicked on Contracts", Toast.LENGTH_SHORT).show()
                R.id.navigation_lich -> Toast.makeText(applicationContext, "Clicked on Lich", Toast.LENGTH_SHORT).show()

            }
            drawerLayout.close()
            true
        }
    }

    //Setup default fragment upon initial load
    private fun setupFragments() {
        supportFragmentManager.beginTransaction().apply {
            replace(fragmentView.id, allItemsFragment)
            commit()
        }
    }

    private fun setupGlobalTradeData() {
        apiBuilder.setupGetRequest(getItemsUrl)
        val rawResponse = apiBuilder.executeRequest()
        tradableItems = Gson().fromJson(rawResponse, TradableItems::class.java)
       // tradableItems.payload.items = tradableItems.payload.items.shuffled() //Randomize item order
        tradableItems.payload.items = tradableItems.payload.items.sortedBy {
            it.item_name
        }
    }
}