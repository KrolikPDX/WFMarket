package com.example.wfmarket.pageLogic.fragments

import ItemViewAdapter
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wfmarket.R
import com.example.wfmarket.models.responses.TradableItemImage
import com.example.wfmarket.models.responses.tradableItems.Items
import com.example.wfmarket.pageLogic.*
import com.google.gson.Gson
import java.net.URL

/* For item icon:
1. Get item info https://api.warframe.market/v1/items/mirage_prime_systems
2. Pull item "icon": "icons/en/axi_a1_relic.a74c06f0cae21bdb8933685c867385f8.png"
3. Trim out icons/en/
4. Pull icon from https://warframe.market/static/assets/items/images/en/axi_a1_relic.a74c06f0cae21bdb8933685c867385f8.png
*/

/* For item thumbnail:
1. Get item info
2. Pull item "thumb" : "/icons/en/thumbs/axi_a1_relic.a74c06f0cae21bdb8933685c867385f8.128x128.png"
3. Trim out /icons/en/thumbs/
4. Pull thumbnail from https://warframe.market/static/assets/items/images/en/thumbs/axi_a1_relic.a74c06f0cae21bdb8933685c867385f8.128x128.png
 */

class ItemInfoFragment : Fragment() {
    private lateinit var rootView:View
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_item_info, container, false)
        setupParams()
        return rootView
    }

    private fun setupParams() {
        recyclerView = rootView.findViewById(R.id.recyclerView)
        layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = ItemViewAdapter(this.context)
        try {
            val tradableItemImages = Gson().fromJson(preferences.getString("TradableItemImages", ""), TradableItemImage::class.java)
            Log.i(TAG, "Found total images = " + tradableItemImages.getSize())
            Log.i(TAG, "Found images in preferences = " + tradableItemImages.getAllItemNames().toString())
        } catch (e: Exception) {
            Log.i(TAG, "Could not find existing tradableItemImages in preferences")
        }
    }
}

