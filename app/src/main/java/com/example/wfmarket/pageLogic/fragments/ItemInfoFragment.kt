package com.example.wfmarket.pageLogic.fragments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wfmarket.R
import com.example.wfmarket.helpers.ItemViewAdapter
import com.example.wfmarket.models.responses.TradableItemImage
import com.example.wfmarket.pageLogic.TAG
import com.example.wfmarket.pageLogic.preferences
import com.example.wfmarket.pageLogic.tradableItemImages
import com.example.wfmarket.pageLogic.tradableItems
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_item_info, container, false)
        setupParams()
        checkIfImagesNotSaved()
        return rootView
    }

    private fun setupParams() {
        recyclerView = rootView.findViewById(R.id.recyclerView)
        layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = ItemViewAdapter(this.context)
        try {
            //prefEditor.remove("TradableItemImages").commit()
            tradableItemImages = TradableItemImage()
            tradableItemImages = Gson().fromJson(preferences.getString("TradableItemImages", ""), TradableItemImage::class.java)
        } catch (e: Exception) { }
    }

    private fun checkIfImagesNotSaved() {
        var foundImages = tradableItemImages.getAllItemNames()
        var nonFoundImages = tradableItems.payload.items.filter {
            !foundImages.contains(it.item_name)
        }
        //Run async to get images that are not found
        Log.i(TAG, "Total non found images = ${nonFoundImages.size}")
        GlobalScope.launch(Dispatchers.IO) {
            //Go through each nonfound image and add them to list, after each added item update list
            /*
            nonFoundImages.forEach { nonFoundImage ->
                val imageString:String = getImageFor(nonFoundImage.item_name)!!.encodeToBase64()
                tradableItemImages.addImage(nonFoundImage.item_name, imageString)
                val jsonObject = Gson().toJson(tradableItemImages)
                prefEditor.putString("TradableItemImages", jsonObject).commit()
                var test = tradableItems.payload.items.filter {
                    !foundImages.contains(it.item_name)
                }
                Log.i(TAG, "Total nonfound items left = " + test.size)
            } */
        }
    }

    private fun getImageFor(itemName: String): Bitmap? {
        val itemThumb = tradableItems.payload.items.find {
            it.item_name == itemName
        }!!.thumb
        val itemUrl = "https://warframe.market/static/assets/${itemThumb}"
        val url = URL(itemUrl)
        try {
            return BitmapFactory.decodeStream(url.openConnection().getInputStream())
        } catch (e: Exception) {
            Log.i("ERROR", "Could not find image for $itemName with $itemUrl")
        }
        return null
    }
}

