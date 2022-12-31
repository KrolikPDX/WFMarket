package com.example.wfmarket.helpers

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody


public class ApiBuilder {
    private val client:OkHttpClient = OkHttpClient()
    private var request:Request.Builder = Request.Builder()
    private val json = "application/json; charset=utf-8".toMediaTypeOrNull()

    fun setupPostRequest(url: String, body: String) {
        var requestBody = FormBody.Builder()

        addHeader("Content-Type", "application/json")
        addHeader("Accept", "application/json")
        request.post(requestBody.build())
            .url(url)
            .post(body.toRequestBody(json))
            .build()
        val body = body
    }

    fun executeRequest():String {
        val response = client.newCall(request.build()).execute()
        return response.body?.string() ?: ""
    }

    fun addHeader(name:String, value:String) {
        request.addHeader(name, value)
    }
}