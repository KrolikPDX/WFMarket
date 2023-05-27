package com.example.wfmarket.pageLogic
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.example.wfmarket.helpers.hideKeyboard
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
    lateinit var searchButton: Toolbar
    lateinit var searchTextbox: EditText
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
        searchTextbox.addTextChangedListener(searchTextWatcher)

        searchButton.setOnClickListener { //If we click on search icon display search bar, change icon to close search bar
            if (searchTextbox.visibility == View.INVISIBLE) {
                searchTextbox.visibility = View.VISIBLE
                searchButton.setNavigationIcon(R.drawable.ic_close_icon)
            } else {
                searchTextbox.visibility = View.INVISIBLE
                searchButton.setNavigationIcon(R.drawable.ic_search_icon)
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
                        replace(fragmentView.id, buySellFragment, "BuySellFragment")
                        commit()
                    }
                }
                R.id.navigation_items -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(fragmentView.id, allItemsFragment, "AllItemsFragment")
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
            replace(fragmentView.id, buySellFragment, "BuySellFragment")
            commit()
        }
    }

    private fun setupGlobalTradeData() {
        apiBuilder.setupGetRequest(getItemsUrl)
        val rawResponse = apiBuilder.executeRequest()
        tradableItems = Gson().fromJson(rawResponse, TradableItems::class.java)
        tradableItems.payload.items = tradableItems.payload.items.shuffled() //Randomize item order
    }

    private val searchTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            tradableItems.payload.items = tradableItems.payload.items.sortedByDescending {
                it.item_name.compareTo(s.toString(), true)
            }
            if (supportFragmentManager.fragments[0].javaClass == AllItemsFragment::class.java) {
                (supportFragmentManager.findFragmentByTag("AllItemsFragment") as AllItemsFragment).refreshRecyclerView()
            } else if (supportFragmentManager.fragments[0].javaClass == BuySellFragment::class.java) {
                (supportFragmentManager.findFragmentByTag("BuySellFragment") as BuySellFragment).refreshRecyclerView()
            }

            //If user pressed enter, remove whitespace and close search
            if (s.toString().contains("\n")) {
                searchTextbox.visibility = View.INVISIBLE
                searchButton.setNavigationIcon(R.drawable.ic_search_icon)
                searchTextbox.setText(s.toString().replace(Regex("\n"), ""))
                searchTextbox.hideKeyboard()
            }
        }

        override fun afterTextChanged(s: Editable?) {}
    }
}