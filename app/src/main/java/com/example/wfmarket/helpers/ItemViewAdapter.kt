import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wfmarket.R
import com.example.wfmarket.models.responses.tradableItems.TradableItems
import com.example.wfmarket.pageLogic.tradableItems

class ItemViewAdapter(private val context: Context?) : RecyclerView.Adapter<ItemViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewAdapter.ViewHolder {
        // to inflate the layout for each item of recycler view.
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.cardview_item, parent, false)
        return ViewHolder(view)
    }

    //Add data to card view elements
    override fun onBindViewHolder(holder: ItemViewAdapter.ViewHolder, position: Int) {
        holder.itemTitleView.text = tradableItems.payload.items[position].item_name
        // to set data to textview and imageview of each card layout
        //val model: CourseModel = courseModelArrayList[position]
        //holder.courseNameTV.setText(model.getCourse_name())
        //holder.courseRatingTV.setText("" + model.getCourse_rating())
        //holder.courseIV.setImageResource(model.getCourse_image())
    }

    //this method is used for showing number of card items in recycler view.
    override fun getItemCount(): Int {
        return tradableItems.payload.items.size
    }

    //Initialize card view elements by ID
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemTitleView: TextView
        //private val courseIV: ImageView
        //private val courseNameTV: TextView
        //private val courseRatingTV: TextView
        init {
            itemTitleView = itemView.findViewById(R.id.itemTitle)
            //courseIV = itemView.findViewById(R.id.idIVCourseImage)
            //courseNameTV = itemView.findViewById(R.id.idTVCourseName)
            //courseRatingTV = itemView.findViewById(R.id.idTVCourseRating)
        }
    }
}