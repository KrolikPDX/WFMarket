package com.example.wfmarket.models.responses

import android.graphics.Bitmap
import android.util.Log
import com.example.wfmarket.pageLogic.TAG

class TradableItemImage {
    private var itemImages = mutableMapOf<String, Bitmap>()

    fun getImage(itemName:String):Bitmap? {
        return itemImages[itemName]
    }

    fun getAllItemNames(): MutableSet<String> {
        return itemImages.keys
    }

    fun getSize():Int {
        return itemImages.size
    }

    fun addImage(itemName:String, image:Bitmap) {
        Log.i(TAG, "Added image $itemName")
        itemImages[itemName] = image
    }
}