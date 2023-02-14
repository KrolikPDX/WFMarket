package com.example.wfmarket.adapters

import com.example.wfmarket.pageLogic.fragments.ItemDetailsFragment
import android.content.Context
import android.database.DataSetObserver
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListAdapter
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.wfmarket.R
import com.example.wfmarket.models.responses.tradableItems.ItemDetailsItem
import com.example.wfmarket.models.responses.tradableItems.Items
import com.example.wfmarket.models.responses.tradableItems.ItemsInSet
import com.example.wfmarket.pageLogic.TAG
import com.example.wfmarket.pageLogic.tradableItems
import com.squareup.picasso.Picasso

//RecyclerView.Adapter<AllItemsViewAdapter.ViewHolder>()
class ItemsInSetAdapter(val context: Context, val currentItem: Items, val itemsInSet: ItemDetailsItem) : BaseAdapter() {
    lateinit var itemTitleView: TextView
    lateinit var  imageView: ImageView
    lateinit var  cardView: CardView

    override fun getCount(): Int {
        return itemsInSet.items_in_set.size
    }

    override fun getItem(position: Int): ItemsInSet {
        return itemsInSet.items_in_set[position]
    }

    override fun getItemId(position: Int): Long {
        return itemsInSet.items_in_set[position].id.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        val currentItemInSet = itemsInSet.items_in_set[position]
        val item = currentItem
        var view = convertView //If using a conditional
        if (currentItem.item_name != currentItemInSet.en.item_name) {
            view =
                LayoutInflater.from(context).inflate(R.layout.cardview_item_in_set, parent, false)
            itemTitleView = view.findViewById(R.id.itemTitle)
            imageView = view.findViewById(R.id.itemImage)
            cardView = view.findViewById(R.id.cardView)
            cardView.setOnClickListener {
                Log.i("Print", "Clicked on item ${currentItemInSet.en.item_name}")
            }

            itemTitleView.text = currentItemInSet.en.item_name
            val fullImageUrl = "https://warframe.market/static/assets/${currentItemInSet.sub_icon}"
            Log.i("Print", "${currentItemInSet.en.item_name} = $fullImageUrl")
            Picasso.get().load(fullImageUrl).into(imageView) //Add url to imageview
        }

        return view
    }

    //override fun getView(position: Int, convertView: View?, parent: ViewGroup): View?

}