package com.example.wfmarket.helpers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wfmarket.R
import com.example.wfmarket.pageLogic.prefEditor
import com.example.wfmarket.pageLogic.tradableItemImages
import com.example.wfmarket.pageLogic.tradableItems
import com.google.gson.Gson
import java.net.URL

class ItemViewAdapter(private val context: Context?) : RecyclerView.Adapter<ItemViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // to inflate the layout for each item of recycler view.
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.cardview_item, parent, false)
        return ViewHolder(view)
    }

    //Initialize card view elements by ID
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemTitleView: TextView
        val imageView:ImageView
        init {
            itemTitleView = itemView.findViewById(R.id.itemTitle)
            imageView = itemView.findViewById(R.id.imageView)
        }
    }

    //this method is used for showing number of card items in recycler view.
    override fun getItemCount(): Int {
        return tradableItems.payload.items.size
    }

    //OnCardView create: Add data to card view elements
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemName = tradableItems.payload.items[position].item_name
        holder.itemTitleView.text = itemName


        val existingImage = tradableItemImages.getImage(itemName)
        if (existingImage == null) { //Get image from internet since its not stored
            val bitmapImage:Bitmap? = getImageFor(itemName)

            if (bitmapImage != null) { //If an internet image was found
                saveImageToPrefs(itemName, bitmapImage)
                holder.imageView.setImageBitmap(bitmapImage)
            }
        } else { //Display existing image
            holder.imageView.setImageBitmap(existingImage)
        }
    }

    private fun saveImageToPrefs(itemName:String, image:Bitmap) {
        tradableItemImages.addImage(itemName, image.encodeToBase64())
        val jsonObject = Gson().toJson(tradableItemImages)
        prefEditor.putString("TradableItemImages", jsonObject).commit()
    }

    private fun getImageFor(itemName: String): Bitmap? {
        val itemThumb = tradableItems.payload.items.find {
            it.item_name == itemName
        }!!.thumb
        val itemUrl = "https://warframe.market/static/assets/${itemThumb}"
        val url = URL(itemUrl)
        try {
            return BitmapFactory.decodeStream(url.openConnection().getInputStream())
        } catch (e: Exception) {
            Log.i("ERROR", "Could not find image for $itemName with $itemUrl")
        }
        return null
    }
}