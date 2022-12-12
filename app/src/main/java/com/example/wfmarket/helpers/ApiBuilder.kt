package com.example.wfmarket

import okhttp3.*


public class ApiBuilder {
    public fun executeRequest(url: String): String {
        val client = OkHttpClient()
        val request: Request = Request.Builder()
            .url(url)
            .build()
        return client.newCall(request).execute().body?.string() ?: ""
    }
}