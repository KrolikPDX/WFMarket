package com.example.wfmarket.pageLogic.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wfmarket.R
import com.example.wfmarket.adapters.AllItemsViewAdapter
import com.example.wfmarket.pageLogic.TAG
import okhttp3.internal.notify

class AllItemsFragment : Fragment() {
    private lateinit var rootView:View
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_all_items, container, false)
        setupParams()
        val onScroll = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) { //Dy = rate at which we scroll
                if (recyclerView.layoutManager?.findViewByPosition(0)?.display != null) {
                    //Item 0 is displayed
                }
            }
        }
        recyclerView.addOnScrollListener(onScroll)

        return rootView
    }

    private fun setupParams() {
        recyclerView = rootView.findViewById(R.id.recyclerView)
        progressBar = rootView.findViewById(R.id.progressBar)
        layoutManager = LinearLayoutManager(this.requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = AllItemsViewAdapter(this.requireContext(), this)
    }

    fun refreshRecyclerView() {
        recyclerView.adapter!!.notifyDataSetChanged()
    }

    fun displayProgressBar(display: Int) {
        progressBar.visibility = display
    }
}

