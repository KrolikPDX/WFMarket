package com.example.wfmarket.helpers

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
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