package com.example.wfmarket.adapters

import com.example.wfmarket.pageLogic.fragments.ItemDetailsFragment
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.wfmarket.R
import com.example.wfmarket.pageLogic.HomePageLogic
import com.example.wfmarket.pageLogic.fragments.AllItemsFragment
import com.example.wfmarket.pageLogic.tradableItems
import com.squareup.picasso.Picasso


class AllItemsViewAdapter(private val context: Context?, private val hostFragment: AllItemsFragment) : RecyclerView.Adapter<AllItemsViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // to inflate the layout for each item of recycler view.
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.cardview_item, parent, false)
        return ViewHolder(view)
    }

    //Initialize card view elements by ID
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemTitleView: TextView
        val imageView:ImageView
        val cardView: CardView
        init {
            itemTitleView = itemView.findViewById(R.id.itemTitle)
            imageView = itemView.findViewById(R.id.imageView)
            cardView = itemView.findViewById(R.id.cardView)
        }
    }

    //this method is used for showing number of card items in recycler view.
    override fun getItemCount(): Int {
        return tradableItems.payload.items.size
    }

    //OnCardView create: Add data to card view elements
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = tradableItems.payload.items[position]
        holder.itemTitleView.text = item.item_name
        val fullImageUrl = "https://warframe.market/static/assets/${item.thumb.replace(".128x128", "")
            .replace("/thumbs", "")}"
        Picasso.get().load(fullImageUrl).into(holder.imageView)

        holder.cardView.setOnClickListener {
            hostFragment.displayProgressBar(View.VISIBLE)

            val itemDetailsFragment = ItemDetailsFragment(item)
            //Replace/overlay current fragment with new fragment
            (context as AppCompatActivity).supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentFrame, itemDetailsFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }
}