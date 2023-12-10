package com.example.wfmarket.pageLogic.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewManager
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.wfmarket.R
import com.example.wfmarket.adapters.ItemsInSetAdapter
import com.example.wfmarket.adapters.SellerListViewAdapter
import com.example.wfmarket.models.responses.tradableItems.*
import com.example.wfmarket.pageLogic.TAG
import com.example.wfmarket.pageLogic.apiBuilder
import com.google.gson.Gson
import com.squareup.picasso.Picasso


class ItemDetailsFragment(private val item: Items) : Fragment() {
    private lateinit var rootView: View
    private lateinit var itemTitle: TextView
    private lateinit var itemRarity: TextView
    private lateinit var itemImage: ImageView
    private lateinit var itemDescription: TextView
    private lateinit var itemsInSetGrid: GridView
    private lateinit var wikiLinkButton: Button
    private lateinit var sellerListView: ListView

    private lateinit var itemDetailsItem: ItemDetailsItem
    private lateinit var itemInSet: ItemsInSet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_item_details, container, false)
        getItemDetails()
        setupItemOrders()
        setupParams()
        setupViewData()
        return rootView
    }

    //Get itemDetails as tradableItems does not contain all item info
    private fun getItemDetails(){
        apiBuilder.setupGetRequest("https://api.warframe.market/v1/items/${item.url_name}")
        val response = apiBuilder.executeRequest()
        itemDetailsItem = Gson().fromJson(response, ItemDetails::class.java).payload.item
        itemInSet = itemDetailsItem.items_in_set.find {
            it.url_name == item.url_name
        }!!
    }

    private fun setupItemOrders() {
        apiBuilder.setupGetRequest("https://api.warframe.market/v1/items/${item.url_name}/orders")
        val response = apiBuilder.executeRequest()
        item.orders = Gson().fromJson(response, ItemOrders::class.java).payload.orders
    }

    private fun setupParams() {
        itemTitle = rootView.findViewById(R.id.itemTitle)
        itemRarity = rootView.findViewById(R.id.itemRarity)
        itemImage = rootView.findViewById(R.id.itemImage)
        itemDescription = rootView.findViewById(R.id.itemDescription)
        itemsInSetGrid = rootView.findViewById(R.id.itemInSetGridView)
        wikiLinkButton = rootView.findViewById(R.id.wikiLinkButton)
        sellerListView = rootView.findViewById(R.id.sellerListView)
    }

    private fun setupViewData() {
        //Item Title
        itemTitle.text = item.item_name

        //Item Image
        val fullImageUrl = "https://warframe.market/static/assets/${item.thumb.replace(".128x128", "")
            .replace("/thumbs", "")}"
        Picasso.get().load(fullImageUrl).into(itemImage)

        //Item description
        itemDescription.text = itemInSet.en.description

        //Item rarity
        if (itemInSet.rarity == null)
                (itemRarity.parent as ViewManager).removeView(itemRarity)
        else itemRarity.text = itemInSet.rarity!!.uppercase()

        //Item wiki link
        wikiLinkButton.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(itemInSet.en.wiki_link))
            startActivity(browserIntent)
        }

        if (itemDetailsItem.items_in_set.size > 1) {
            itemsInSetGrid.adapter = ItemsInSetAdapter(this.requireContext(), item, itemDetailsItem)
            //use recyclerview with a grid layout and set its height as wrap_content to get proper height
        } else {
            itemsInSetGrid.visibility = View.GONE
        }


        val arrayAdapter: ArrayAdapter<*>
        //arrayAdapter = SellerListViewAdapter(this.requireContext(), item)
        var array = arrayOf("Melbourne", "Vienna", "Vancouver", "Toronto", "Calgary", "Adelaide", "Perth", "Auckland", "Helsinki", "Hamburg", "Munich", "New York", "Sydney", "Paris", "Cape Town", "Barcelona", "London", "Bangkok")

        val adapter = ArrayAdapter(this.requireContext(), R.layout.listview_seller, array)
        sellerListView.adapter = adapter
    }
}