package com.example.wfmarket.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.wfmarket.R
import com.example.wfmarket.models.responses.tradableItems.ItemDetailsItem
import com.example.wfmarket.models.responses.tradableItems.ItemOrders
import com.example.wfmarket.models.responses.tradableItems.Items
import com.example.wfmarket.models.responses.tradableItems.ItemsInSet
import com.example.wfmarket.pageLogic.TAG
import com.example.wfmarket.pageLogic.fragments.AllItemsFragment
import com.example.wfmarket.pageLogic.tradableItems
import com.squareup.picasso.Picasso


//RecyclerView.Adapter<AllItemsViewAdapter.ViewHolder>()

class SellerListViewAdapter(val context: Context, private val item: Items) : BaseAdapter() {
    override fun getCount(): Int {
        return item.orders.count()
        return 1
    }

    override fun getItem(position: Int): Any {
        return item.orders[position]
    }

    override fun getItemId(position: Int): Long {
        TODO("Not yet implemented")
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.cardview_item_in_set, parent, false)
        Log.i(TAG, "Total orders found = $count")
        return view
    }


}