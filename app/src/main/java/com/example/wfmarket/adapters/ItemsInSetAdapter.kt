package com.example.wfmarket.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.wfmarket.R
import com.example.wfmarket.models.responses.tradableItems.ItemDetailsItem
import com.example.wfmarket.models.responses.tradableItems.Items
import com.example.wfmarket.models.responses.tradableItems.ItemsInSet
import com.example.wfmarket.pageLogic.fragments.AllItemsFragment
import com.example.wfmarket.pageLogic.tradableItems
import com.squareup.picasso.Picasso


//RecyclerView.Adapter<AllItemsViewAdapter.ViewHolder>()

class ItemsInSetAdapter(private val context: Context, private val currentItem: Items, private val itemsInSet: ItemDetailsItem) : BaseAdapter() {
    lateinit var itemTextView: TextView
    lateinit var  itemImageView: ImageView
    lateinit var  cardView: CardView

    init {
        itemsInSet.items_in_set = itemsInSet.items_in_set.sortedBy { it.en.item_name }
    }

    //Gets run for each card for gridview
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        val currentItemInSet = itemsInSet.items_in_set[position]
        val view = LayoutInflater.from(context).inflate(R.layout.cardview_item_in_set, parent, false)

        //Display items in set
        if (itemsInSet.items_in_set.size > 1) {
            setupViews(view)

            if (currentItem.item_name != currentItemInSet.en.item_name) { //Prevent clicking on self
                cardView.setOnClickListener {
                    val item = tradableItems.payload.items.find {
                        it.item_name == currentItemInSet.en.item_name
                    }!!
                    AllItemsViewAdapter(context, AllItemsFragment()).changeFragmentItem(item)
                }
            }
            itemTextView.text = currentItemInSet.en.item_name

            val append = currentItemInSet.sub_icon ?: currentItemInSet.icon
            val fullImageUrl = "https://warframe.market/static/assets/${append}"
            Picasso.get().load(fullImageUrl).into(itemImageView) //Add url to imageview
            //Add card click animation for the clicked item
            return view
        }
        //If there are no items to display
        view.visibility = View.INVISIBLE
        return view
    }

    private fun setupViews(view: View) {
        itemTextView = view.findViewById(R.id.itemTitle)
        itemImageView = view.findViewById(R.id.itemImage)
        cardView = view.findViewById(R.id.cardView)
    }

    //Required BaseAdapter methods
    override fun getCount(): Int {
        return itemsInSet.items_in_set.size
    }

    override fun getItem(position: Int): ItemsInSet {
        return itemsInSet.items_in_set[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }
}