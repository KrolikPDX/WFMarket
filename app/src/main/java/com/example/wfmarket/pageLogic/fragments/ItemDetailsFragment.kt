package com.example.wfmarket.pageLogic.fragments

import com.example.wfmarket.models.responses.tradableItems.ItemDetails
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.wfmarket.R
import com.example.wfmarket.models.responses.tradableItems.ItemDetailsItem
import com.example.wfmarket.models.responses.tradableItems.Items
import com.example.wfmarket.models.responses.tradableItems.ItemsInSet
import com.example.wfmarket.pageLogic.TAG
import com.example.wfmarket.pageLogic.apiBuilder
import com.google.gson.Gson
import com.squareup.picasso.Picasso

class ItemDetailsFragment(private val item: Items) : Fragment() {
    private lateinit var rootView: View
    private lateinit var itemTitle: TextView
    private lateinit var itemImage: ImageView
    private lateinit var itemDescription: TextView

    private lateinit var fullItemDetails: ItemDetailsItem
    private lateinit var itemDetails: ItemsInSet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_item_details, container, false)


        getItemDetails()
        setupParams()
        setupViewData()
        return rootView
    }

    private fun getItemDetails(): Unit {
        apiBuilder.setupGetRequest("https://api.warframe.market/v1/items/${item.url_name}")
        val response = apiBuilder.executeRequest()
        fullItemDetails = Gson().fromJson(response, ItemDetails::class.java).payload.item
        itemDetails = fullItemDetails.items_in_set.find {
            it.url_name == item.url_name
        }!!
    }

    private fun setupParams() {
        itemTitle = rootView.findViewById(R.id.itemTitle)
        itemImage = rootView.findViewById(R.id.itemImage)
        itemDescription = rootView.findViewById(R.id.itemDescription)
        //mod_max_rank
        //rarity
        //wiki_link
        //Other items in set if any
    }

    private fun setupViewData() {
        itemTitle.text = item.item_name

        val fullImageUrl = "https://warframe.market/static/assets/${item.thumb.replace(".128x128", "")
            .replace("/thumbs", "")}"
        Picasso.get().load(fullImageUrl).into(itemImage) //Add url to imageview

        itemDescription.text = itemDetails.en.description
    }
}