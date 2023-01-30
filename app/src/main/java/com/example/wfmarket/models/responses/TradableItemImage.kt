package com.example.wfmarket.models.responses

import android.graphics.Bitmap
import android.util.Log
import com.example.wfmarket.helpers.decodeFromBase64
import com.example.wfmarket.pageLogic.TAG

class TradableItemImage {
    //                               <ItemName, EncodedBitmap>
    private var itemImages:MutableMap<String, String> = mutableMapOf()

    fun getImage(itemName:String):Bitmap? {
        try {
            return decodeFromBase64(itemImages[itemName]!!)
        } catch (e: Exception) {
            Log.i(TAG, "Could not find $itemName in itemImages")
        }
        return null
    }

    fun getAllItemNames(): MutableSet<String> {
        return itemImages.keys
    }

    fun addImage(itemName:String, image:String) {
        itemImages[itemName] = image
    }
}