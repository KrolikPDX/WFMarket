package com.example.wfmarket.pageLogic.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.wfmarket.R
import com.example.wfmarket.models.responses.auth.AuthSigninResponse
import com.example.wfmarket.pageLogic.preferences
import com.example.wfmarket.pageLogic.tradableItems
import com.example.wfmarket.pageLogic.user
import com.google.gson.Gson


class BuySellFragment : Fragment() {
    private lateinit var buySellView: TextView
    private lateinit var createdView:View
    private lateinit var testButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ):View? {
        setupParams(inflater, container)
        //Populate tiles
        buySellView.text = tradableItems.payload.items[1].item_name
        return createdView
    }

    private fun setupParams(inflater:LayoutInflater, container:ViewGroup?){
        createdView = inflater.inflate(R.layout.fragment_buy_sell, container, false)
        buySellView = createdView.findViewById(R.id.buySellView)
        testButton = createdView.findViewById(R.id.testButton)
        testButton.setOnClickListener(testClick())

    }

    private fun testClick():View.OnClickListener {
        return View.OnClickListener {
            buySellView.text = "Test Button clicked!!"
        }
    }
}

