import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.denzcoskun.imageslider.models.SlideModel
import com.example.csi_dmce.R

class ImageAdapter(context1: ArrayList<String>, private val context: Context) :
    RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    private var imageList: ArrayList<SlideModel> = ArrayList()

    fun setImageList(images: ArrayList<SlideModel>) {
        imageList = images
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // loading the images from the position
        Glide.with(holder.itemView.context).load(imageList[position].imageUrl).into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView

        init {
            imageView = itemView.findViewById<ImageView>(R.id.item)
        }
    }
}
