package com.example.wfmarket.pageLogic
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
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
    -> BUG: Sometimes text is set to small font though words fit

- ItemDetailsFragment -
    -> Setup on itemsInSet click replace current fragment with new fragment
    -> Update ItemsInSet titles to not cut off if too long
    -> Update ItemsInSet images to be relative
 */

class HomePageLogic: AppCompatActivity(){
    private lateinit var toolbar: Toolbar
    private lateinit var searchButton: Toolbar
    private lateinit var searchTextbox: EditText
    private lateinit var toggle:ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    private var isSearchTextboxOpen: Boolean = false

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
        setDefaultFragment()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupParams() {
        toolbar = findViewById(R.id.navigation_toolbar)
        searchButton = findViewById(R.id.search_toolbar)
        searchTextbox = findViewById(R.id.searchTextbox)
        searchTextbox.visibility = View.INVISIBLE

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

        searchButton.setOnClickListener {
            if (!isSearchTextboxOpen && searchTextbox.visibility == View.INVISIBLE) {
                searchTextbox.text.clear()
                searchTextbox.visibility = View.VISIBLE
                isSearchTextboxOpen = true
            } else if (isSearchTextboxOpen && searchTextbox.visibility == View.VISIBLE) {
                Log.i(TAG, "First Item = ${tradableItems.payload.items[0].item_name}")
                for(i in 0..10) {
                    var value = tradableItems.payload.items[i].item_name.compareTo(searchTextbox.text.toString(), true)
                    Log.i(TAG, "Compare ${searchTextbox.text.toString()} to ${tradableItems.payload.items[i].item_name} = $value")
                }
                Log.i(TAG, "New Item = ${tradableItems.payload.items[0].item_name}")

                //Reset recycle view with updated list
                searchTextbox.visibility = View.INVISIBLE
                isSearchTextboxOpen = false
            }
        }

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

    //Change to set fragment to whatever use left off on
    private fun setDefaultFragment() { //Setup default fragment upon initial load
        supportFragmentManager.beginTransaction().apply {
            replace(fragmentView.id, allItemsFragment)
            commit()
        }
    }

    private fun setupGlobalTradeData() {
        apiBuilder.setupGetRequest(getItemsUrl)
        val rawResponse = apiBuilder.executeRequest()
        tradableItems = Gson().fromJson(rawResponse, TradableItems::class.java)
        //tradableItems.payload.items = tradableItems.payload.items.shuffled() //Randomize item order
        tradableItems.payload.items = tradableItems.payload.items.sortedByDescending { it.item_name.length }
    }
}