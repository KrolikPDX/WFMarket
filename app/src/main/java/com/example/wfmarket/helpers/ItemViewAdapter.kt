import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.ui.graphics.ImageBitmap
import androidx.recyclerview.widget.RecyclerView
import com.example.wfmarket.R
import com.example.wfmarket.models.responses.TradableItemImage
import com.example.wfmarket.models.responses.tradableItems.Items
import com.example.wfmarket.pageLogic.*
import com.google.gson.Gson
import java.net.URL


class ItemViewAdapter(private val context: Context?) : RecyclerView.Adapter<ItemViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewAdapter.ViewHolder {
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
    override fun onBindViewHolder(holder: ItemViewAdapter.ViewHolder, position: Int) {
        val itemName = tradableItems.payload.items[position].item_name
        holder.itemTitleView.text = itemName
        if (tradableItemImages.getImage(itemName) == null) {
            val bitmapImage:Bitmap? = getImageFor(itemName)
            if (bitmapImage != null) {
                tradableItemImages.addImage(itemName, bitmapImage)
                val jsonObject = Gson().toJson(tradableItemImages)
                Log.i(TAG, "Adding image to preferences: $itemName")
                Log.i(TAG, "Total image items = ${tradableItemImages.getSize()}")
                prefEditor.putString("TradableItemImages", jsonObject).commit()
                holder.imageView.setImageBitmap(bitmapImage)
            }
        } else {
            holder.imageView.setImageBitmap(tradableItemImages.getImage(itemName))
        }
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