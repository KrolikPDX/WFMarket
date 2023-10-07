@file:Suppress("DEPRECATION")

package com.example.wfmarket.adapters

import android.app.Activity
import android.content.Context
import android.os.AsyncTask
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.wfmarket.R
import com.example.wfmarket.helpers.hideKeyboard
import com.example.wfmarket.models.responses.tradableItems.ItemOrders
import com.example.wfmarket.models.responses.tradableItems.Items
import com.example.wfmarket.models.responses.tradableItems.Order
import com.example.wfmarket.pageLogic.HomePageLogic
import com.example.wfmarket.pageLogic.TAG
import com.example.wfmarket.pageLogic.apiBuilder
import com.example.wfmarket.pageLogic.fragments.BuySellFragment
import com.example.wfmarket.pageLogic.fragments.ItemDetailsFragment
import com.example.wfmarket.pageLogic.tradableItems
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import java.util.*


class BuySellViewAdapter(private val context: Context?, private val hostFragment: BuySellFragment) : RecyclerView.Adapter<BuySellViewAdapter.ViewHolder>() {

    lateinit var parentFragment: BuySellFragment

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // to inflate the layout for each item of recycler view.
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.cardview_buy_item, parent, false)
        parentFragment = (hostFragment as BuySellFragment)
        return ViewHolder(view)
    }

    //Initialize card view elements by ID
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val buyItemPrice: TextView
        val itemTitleView: TextView
        val imageView:ImageView
        val cardView: CardView
        val progressBar: ProgressBar
        init {
            buyItemPrice = itemView.findViewById(R.id.buyItemPrice)
            itemTitleView = itemView.findViewById(R.id.buyItemTitle)
            imageView = itemView.findViewById(R.id.buyItemImage)
            cardView = itemView.findViewById(R.id.buyItemCardView)
            progressBar = itemView.findViewById(R.id.buyItemPriceProgressBar)
        }
    }

    //this method is used for showing number of card items in recycler view.
    override fun getItemCount(): Int {
        return tradableItems.payload.items.size
    }

    //OnCardView create: Add data to card view elements
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //Set cardview itemName
        val item: Items = tradableItems.payload.items[position]
        holder.itemTitleView.text = item.item_name
        holder.progressBar.visibility = View.VISIBLE
        holder.buyItemPrice.visibility = View.GONE
        //Set cardview image
        val fullImageUrl = "https://warframe.market/static/assets/${item.thumb.replace(".128x128", "")
            .replace("/thumbs", "")}"
        Picasso.get().load(fullImageUrl).into(holder.imageView)


        //Set cardview price
        if (item.orders.isNullOrEmpty()) { //If we do not have stored orders for current item
            Log.i(TAG, "Enable progress bar for item ${item.item_name}")
            holder.progressBar.visibility = View.VISIBLE
            holder.buyItemPrice.visibility = View.GONE
            var test = holder.buyItemPrice.setItemPrice(item, holder.progressBar)
            var isDisplayed = parentFragment.isItemVisible(holder.itemView)
            Log.i(TAG, "Item '${item.item_name}' is displayed = $isDisplayed")
            //If current viewholder no longer displayed cancel get item details
            //test.cancel(true)
        } else { //Else we do have orders and display first (lowest) order price
            Log.i(TAG, "Existing | Disable progress bar for item ${item.item_name}")
            holder.progressBar.visibility = View.GONE
            holder.buyItemPrice.visibility = View.VISIBLE
            holder.buyItemPrice.text = "${item.orders[0].platinum}" //If there are NA price it will display incorrect value
        }

        //Setup onclickListener
        holder.cardView.setOnClickListener {
            changeFragmentItem(item, true)
        }
    }

    private fun changeFragmentItem(item: Items, displayProgressBar: Boolean = false) {
        if (displayProgressBar) //Display loading icon
            hostFragment.displayProgressBar(View.VISIBLE)

        val itemDetailsFragment = ItemDetailsFragment(item) //Replace with BuySellDetailsFragment(item)
        var supportFragManager = (context as AppCompatActivity).supportFragmentManager
        (context as HomePageLogic).searchTextbox.hideKeyboard() //Hide keyboard just in case

        //Header changes
        context.searchButton.setNavigationIcon(R.drawable.ic_search_icon)
        context.searchTextbox.visibility = View.INVISIBLE

        //Change fragment to detail fragment
        supportFragManager.beginTransaction().apply {
            replace(R.id.fragmentFrame, itemDetailsFragment, "ItemDetailsFragment")
                .addToBackStack(null) //Add to backstack to recognize supportFragManager.findFragmentByTag
                .commit()
        }

        //Change navigation icons
        supportFragManager.addOnBackStackChangedListener {
            var toolBar: Toolbar = context.findViewById(R.id.navigation_toolbar)
            var searchBar:Toolbar = context.findViewById(R.id.search_toolbar)

            val isDetailsFragmentDisplayed = supportFragManager.findFragmentByTag("ItemDetailsFragment")?.isVisible
            if (isDetailsFragmentDisplayed != null) { //Change to back button
                toolBar.apply {
                    navigationIcon = ContextCompat.getDrawable(context, R.drawable.ic_back_arrow)
                    setNavigationOnClickListener {
                        for (i in 1..supportFragManager.backStackEntryCount) {
                            supportFragManager.popBackStack()
                        }
                    }
                }
                searchBar.visibility = View.GONE
            } else { //Else revert changes to navigation icon
                toolBar.apply {
                    navigationIcon = ContextCompat.getDrawable(context, R.drawable.ic_menu_icon)
                    setOnClickListener(null)
                }
                searchBar.visibility = View.VISIBLE
                context.setSupportActionBar(toolBar)
            }
        }
    }

    private fun TextView.setItemPrice(item: Items, progressBar: ProgressBar): SetItemPrice { //TextView extension
        var getItemPrice = SetItemPrice()
        getItemPrice.currentView = this
        getItemPrice.progressBar = progressBar
        getItemPrice.execute(item)
        return getItemPrice
    }

    //Set item price in currentView asyncly
    class SetItemPrice : AsyncTask<Items, Int?, Items>() {
        public lateinit var currentView: TextView
        public lateinit var progressBar: ProgressBar

        override fun doInBackground(vararg items: Items): Items { //Get price for item
            apiBuilder.setupGetRequest("https://api.warframe.market/v1/items/${items[0].url_name}/orders")
            val response = apiBuilder.executeRequest()
            var itemOrders: List<Order> = Gson().fromJson(response, ItemOrders::class.java).payload.orders
            items[0].orders = itemOrders
            return items[0]
        }

        override fun onProgressUpdate(vararg progress: Int?) {} //Required method

        override fun onPostExecute(item: Items) { //Once we get orders for items filter and display price

            val orders = item.orders
            val filteredOrders = orders.filter { it.user.status != "offline" && it.order_type == "sell" }
            if (!filteredOrders.isNullOrEmpty()) {
                val sortedOrders = filteredOrders.sortedBy { it.platinum }
                item.orders = sortedOrders
                val cheapestOrder = sortedOrders[0].platinum
                currentView.text = "$cheapestOrder"
            } else {
                currentView.text = "N/A"
            }
            currentView.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }
    }
}


