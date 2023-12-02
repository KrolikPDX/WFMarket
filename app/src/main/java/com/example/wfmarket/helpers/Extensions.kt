package com.example.wfmarket.helpers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.example.wfmarket.models.responses.tradableItems.Items
import com.example.wfmarket.pageLogic.SetItemPrice
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream

//Encode bitmap to base64 string
fun Bitmap.encodeToBase64():String {
    val outputStream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    val byte = outputStream.toByteArray()
    val imageEncoded = Base64.encodeToString(byte, Base64.DEFAULT);
    return imageEncoded
}

//Decode string to bitmap
fun decodeFromBase64(input:String):Bitmap {
    val decodedByte = Base64.decode(input, 0);
    return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.size)
}

fun View.hideKeyboard() {
    val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(windowToken, 0)
}

fun TextView.setItemPrice(item: Items, progressBar: ProgressBar): SetItemPrice { //TextView extension
    var getItemPrice = SetItemPrice()
    getItemPrice.currentView = this
    getItemPrice.progressBar = progressBar
    getItemPrice.execute(item)
    return getItemPrice
}

fun ImageView.addPicassoImage(item: Items) {
    val fullImageUrl = "https://warframe.market/static/assets/${item.thumb.replace(".128x128", "")
        .replace("/thumbs", "")}"
    Picasso.get().load(fullImageUrl).into(this)
}