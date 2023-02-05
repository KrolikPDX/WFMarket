package com.example.wfmarket.pageLogic.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.wfmarket.R
import com.example.wfmarket.pageLogic.TAG
import com.example.wfmarket.pageLogic.tradableItems
import com.squareup.picasso.Picasso

class ItemDetailsFragment(private val itemName: String) : Fragment() {
    private lateinit var rootView: View
    private lateinit var itemTitle: TextView
    private lateinit var itemImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_item_details, container, false)
        setupParams()
        return rootView
    }

    private fun setupParams() {
        itemTitle = rootView.findViewById(R.id.itemTextView)
        itemImage = rootView.findViewById(R.id.itemImage)
        itemTitle.text = itemName
        val item = tradableItems.payload.items.find {
            it.item_name == itemName
        }!!
        val itemUrl = "https://warframe.market/static/assets/${item.thumb}"
        val fullImageUrl = "https://warframe.market/static/assets/${item.thumb.replace(".128x128", "").replace("/thumbs", "")}"
        Log.i(TAG, "Displaying Url=$fullImageUrl")
        Picasso.get().load(fullImageUrl).into(itemImage)
    }
}