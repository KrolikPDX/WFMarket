package com.example.wfmarket.pageLogic.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.wfmarket.R
import com.example.wfmarket.models.responses.auth.AuthSigninResponse
import com.example.wfmarket.pageLogic.preferences
import com.example.wfmarket.pageLogic.tradableItems
import com.example.wfmarket.pageLogic.user
import com.google.gson.Gson


class BuySellFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_buy_sell, container, false)
        var buySellView = view.findViewById<TextView>(R.id.buySellView)
        buySellView.text = tradableItems.payload.items[1].item_name
        setupParams()
        return view
    }

    private fun setupParams(){
        var authSigningResponse:String?
        try {
            authSigningResponse = preferences.getString("AuthSigninResponse", "")
            user = Gson().fromJson(authSigningResponse, AuthSigninResponse::class.java).payload.user
        } catch(e:Exception) {
            Log.i("Print", "Could not find authSignin in preferences ${e.stackTrace}")
        }
    }
}

