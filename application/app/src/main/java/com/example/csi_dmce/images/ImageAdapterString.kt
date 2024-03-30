import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.csi_dmce.R

class ImageAdapterString(private val imageList: ArrayList<String>, private val context: Context) :
    RecyclerView.Adapter<ImageAdapterString.ViewHolder>() {

    private val selectedItems = HashSet<Int>()


    // Method to remove an item from the imageList
    fun removeItem(position: Int) {
        imageList.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // loading the images from the position
        Glide.with(holder.itemView.context).load(imageList[position]).into(holder.imageView)
        holder.itemView.isSelected = selectedItems.contains(position)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView

        init {
            imageView = itemView.findViewById<ImageView>(R.id.item)
            // Set a click listener to handle item removal
            if (adapterPosition != RecyclerView.NO_POSITION) {
                toggleSelection(adapterPosition)
            }
        }

        private fun toggleSelection(position: Int) {
            if (selectedItems.contains(position)) {
                selectedItems.remove(position)
            } else {
                selectedItems.add(position)
            }
            notifyItemChanged(position)
        }
    }

    fun getSelectedItems(): Set<Int> {
        return selectedItems
    }
}
