package com.example.wfmarket.pageLogic

import android.os.AsyncTask
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.example.wfmarket.models.responses.tradableItems.ItemOrders
import com.example.wfmarket.models.responses.tradableItems.Items
import com.example.wfmarket.models.responses.tradableItems.Order
import com.google.gson.Gson

//Set item price in currentView asyncly
public class SetItemPrice : AsyncTask<Items, Int?, Items>() {
    lateinit var currentView: TextView
    lateinit var progressBar: ProgressBar

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