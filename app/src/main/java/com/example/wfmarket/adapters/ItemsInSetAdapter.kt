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
import com.example.wfmarket.models.responses.tradableItems.Items
import com.example.wfmarket.models.responses.tradableItems.ItemsInSet
import com.squareup.picasso.Picasso

//RecyclerView.Adapter<AllItemsViewAdapter.ViewHolder>()

class ItemsInSetAdapter(private val context: Context, private val currentItem: Items, private val itemsInSet: ItemDetailsItem) : BaseAdapter() {
    lateinit var itemTextView: TextView
    lateinit var  itemImageView: ImageView
    lateinit var  cardView: CardView

    init {
        //itemsInSet.items_in_set = itemsInSet.items_in_set.distinct()
        itemsInSet.items_in_set = itemsInSet.items_in_set.sortedBy { it.en.item_name }
    }

    override fun getCount(): Int {
        return itemsInSet.items_in_set.size
    }

    override fun getItem(position: Int): ItemsInSet {
        return itemsInSet.items_in_set[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        val currentItemInSet = itemsInSet.items_in_set[position]
        val view = LayoutInflater.from(context).inflate(R.layout.cardview_item_in_set, parent, false)

        if (itemsInSet.items_in_set.size > 1) {
            itemTextView = view.findViewById(R.id.itemTitle)
            itemImageView = view.findViewById(R.id.itemImage)
            cardView = view.findViewById(R.id.cardView)

            if (currentItem.item_name != currentItemInSet.en.item_name) {
                cardView.setOnClickListener {
                    //Replace current fragment with new item fragment
                    Log.i("Print", "Clicked on item ${currentItemInSet.en.item_name}")
                }
            }
            itemTextView.text = currentItemInSet.en.item_name

            val append = currentItemInSet.sub_icon ?: currentItemInSet.icon
            val fullImageUrl = "https://warframe.market/static/assets/${append}"
            Log.i("Print", "${currentItemInSet.en.item_name} = $fullImageUrl")
            Picasso.get().load(fullImageUrl).into(itemImageView) //Add url to imageview

            return view
        }
        view.visibility = View.INVISIBLE
        return view
    }

    //override fun getView(position: Int, convertView: View?, parent: ViewGroup): View?

}