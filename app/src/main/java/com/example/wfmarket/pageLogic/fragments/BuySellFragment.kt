package com.example.wfmarket.pageLogic.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.wfmarket.R
import com.example.wfmarket.pageLogic.TAG
import com.example.wfmarket.pageLogic.tradableItems

class BuySellFragment : Fragment() {
    var itemIndex:Int = 0
    private lateinit var rootView:View
    private lateinit var titleTextView:TextView
    private lateinit var nextButton:Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_buy_sell, container, false)
        setupParams()


        titleTextView.text = tradableItems.payload.items[itemIndex].item_name
        nextButton.setOnClickListener(nextButtonPress())
        // Inflate the layout for this fragment
        return rootView
    }

    private fun setupParams() {
        titleTextView = rootView.findViewById(R.id.titleTextView)
        nextButton = rootView.findViewById(R.id.nextButton)
    }

    private fun nextButtonPress():OnClickListener {
        return OnClickListener {
            itemIndex++
            var itemName = tradableItems.payload.items[itemIndex].item_name
            titleTextView.text = itemName
        }
    }
}
