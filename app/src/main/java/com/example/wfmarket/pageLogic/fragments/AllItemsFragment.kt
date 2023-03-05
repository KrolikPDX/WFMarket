package com.example.wfmarket.pageLogic.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wfmarket.R
import com.example.wfmarket.adapters.AllItemsViewAdapter

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
        return rootView
    }

    private fun setupParams() {
        recyclerView = rootView.findViewById(R.id.recyclerView)
        progressBar = rootView.findViewById(R.id.progressBar)
        layoutManager = LinearLayoutManager(this.requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = AllItemsViewAdapter(this.requireContext(), this)
    }

    fun displayProgressBar(display: Int) {
        progressBar.visibility = display
    }
}

