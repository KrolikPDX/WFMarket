@file:Suppress("DEPRECATION")

package com.example.wfmarket.adapters

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // to inflate the layout for each item of recycler view.
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.cardview_buy_item, parent, false)
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

        //Set cardview image
        val fullImageUrl = "https://warframe.market/static/assets/${item.thumb.replace(".128x128", "")
            .replace("/thumbs", "")}"
        Picasso.get().load(fullImageUrl).into(holder.imageView)

        //Set cardview price
        //Only first 7 rows get unique buy price, row 8 and onward take initial value from row 1 and so on before updating
        if (item.orders.isNullOrEmpty()) {
            holder.buyItemPrice.setItemPrice(item, holder.progressBar) //pass holder instead?
        } else {
            holder.progressBar.visibility = View.GONE
            holder.buyItemPrice.visibility = View.VISIBLE
            holder.buyItemPrice.text = "${item.orders[0].platinum}"
        }

        //Setup onclickListener
        holder.cardView.setOnClickListener {
            changeFragmentItem(item, true)
        }
    }

    private fun changeFragmentItem(item: Items, displayProgressBar: Boolean = false) {
        if (displayProgressBar) {
            hostFragment.displayProgressBar(View.VISIBLE) //Display loading icon
        }

        //Replace current fragment with new fragment(item)
        val itemDetailsFragment = ItemDetailsFragment(item)
        var supportFragManager = (context as AppCompatActivity).supportFragmentManager
        (context as HomePageLogic).searchTextbox.hideKeyboard()
        context.searchButton.setNavigationIcon(R.drawable.ic_search_icon)
        context.searchTextbox.visibility = View.INVISIBLE
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

    private fun TextView.setItemPrice(item: Items, progressBar: ProgressBar) {
        var getItemPrice = SetItemPrice()
        getItemPrice.currentView = this
        getItemPrice.progressBar = progressBar
        getItemPrice.execute(item)
    }

    class SetItemPrice : AsyncTask<Items, Int?, Items>() {
        public lateinit var currentView: TextView
        public lateinit var progressBar: ProgressBar

        override fun doInBackground(vararg items: Items): Items {
            apiBuilder.setupGetRequest("https://api.warframe.market/v1/items/${items[0].url_name}/orders")
            val response = apiBuilder.executeRequest()
            var itemOrders: List<Order> = Gson().fromJson(response, ItemOrders::class.java).payload.orders
            items[0].orders = itemOrders
            return items[0]
        }

        override fun onProgressUpdate(vararg progress: Int?) {
            //Set to progress bar while we wait to retrieve price
            //setProgressPercent(progress[0])
        }

        override fun onPostExecute(item: Items) {
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


