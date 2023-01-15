package com.example.wfmarket.pageLogic
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.wfmarket.R
import com.example.wfmarket.helpers.FragmentAdapter
import com.example.wfmarket.models.responses.auth.AuthSigninResponse
import com.example.wfmarket.models.responses.auth.User
import com.example.wfmarket.models.responses.tradableItems.TradableItems
import com.example.wfmarket.pageLogic.fragments.BuySellFragment
import com.example.wfmarket.pageLogic.fragments.ContractsFragment
import com.example.wfmarket.pageLogic.fragments.ItemInfoFragment
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import okhttp3.internal.notify
import okhttp3.internal.notifyAll

var user: User? = null
lateinit var viewPager:ViewPager
lateinit var tabLayout:TabLayout

class HomePageLogic: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        setupParams()
        setupTradeData()
        setupTabs()
    }

    private fun setupParams() {
        var authSigningResponse:String?
        try {
            authSigningResponse = preferences.getString("AuthSigninResponse", "")
            user = Gson().fromJson(authSigningResponse, AuthSigninResponse::class.java).payload.user
        } catch(e:Exception) {
            Log.i("Print", "Could not find authSignin in preferences ${e.stackTrace}")
        }
        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)
    }

    private fun setupTradeData() {
        apiBuilder.setupGetRequest(getItemsUrl)
        var rawResponse = apiBuilder.executeRequest()
        tradableItems = Gson().fromJson(rawResponse, TradableItems::class.java)
    }

    private fun setupTabs() {
        val adapter = FragmentAdapter(supportFragmentManager)
        adapter.addFragment(BuySellFragment())
        adapter.addFragment(ContractsFragment())
        adapter.addFragment(ItemInfoFragment())
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)

        tabLayout.getTabAt(0)!!.setIcon(R.drawable.trade_icon)
        tabLayout.getTabAt(1)!!.setIcon(R.drawable.contracts_icon)
        tabLayout.getTabAt(2)!!.setIcon(R.drawable.item_icon)

    }
}