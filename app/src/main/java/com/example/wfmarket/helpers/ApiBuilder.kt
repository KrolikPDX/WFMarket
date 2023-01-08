package com.example.wfmarket.helpers

import android.util.Log
import com.example.wfmarket.pageLogic.TAG
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody


class ApiBuilder {
    private val json = "application/json; charset=utf-8".toMediaTypeOrNull()

    private var client:OkHttpClient = OkHttpClient()
    private lateinit var request:Request.Builder
    private lateinit var response:Response

    fun setupPostRequest(url: String, body: String) {
        request = Request.Builder() //Prevents duplication
        request.url(url)
            .post(body.toRequestBody(json))
            .build()
    }

    fun setupGetRequest(url: String) {
        request = Request.Builder()
        request.url(url)
            .get()
            .build()
    }

    fun executeRequest():String {
        response = client.newCall(request.build()).execute()
        return response.body?.string() ?: ""
    }

    fun requestExecutedSuccess(): Boolean {
        return response.isSuccessful
    }

    fun addHeader(name:String, value:String) {
        request.addHeader(name, value)
    }
}