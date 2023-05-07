package com.example.wfmarket.helpers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.View
import android.view.inputmethod.InputMethodManager
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