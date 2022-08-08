import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapplication.R

data class Article(val title: String, val details: String, val link: String,val image:String)
class RecyclerAdapter(
    private val articles: ArrayList<Article?>,
    var listener: OnArticlieClickListener
) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>(),View.OnClickListener {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val itemTitle: TextView = itemView.findViewById(R.id.tv_title)
        val itemDetail: TextView = itemView.findViewById(R.id.tv_description)
        val itemPicture: ImageView = itemView.findViewById(R.id.iv_image)

        // clickListeners events
        init {
            itemPicture.setOnClickListener {
                //listener(titles[adapterPosition])
//                Toast.makeText(this,"this is my Pic",Toast.LENGTH_LONG).show()
            }
            itemView.setOnClickListener { v: View ->

//                val position: Int = adapterPosition // lamda ex
//
//                val intent = Intent(Intent.ACTION_VIEW)
//                intent.data = Uri.parse(links[position])
//                startActivity(itemView.context, intent,null)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return articles?.size
    }
        var mPosition = -1
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        mPosition = position
        val myArticle = articles[position]!!
        holder.itemView.setOnClickListener{
            listener.itemClick(myArticle)
        }
        holder.itemTitle.text = myArticle.title
        holder.itemDetail.text = myArticle.details
        holder.itemPicture.setOnClickListener {
            listener.imageClick(myArticle)
        }
        Glide.with(holder.itemPicture)
            .load("https://picsum.photos/100/200")
            .into(holder.itemPicture)

    }

    override fun onClick(myview: View?) {
       if(myview?.id == R.id.iv_image ) {
           articles[mPosition]?.let {
               listener.imageClick(it)
           }
       }
        if(myview?.id == R.id.tv_title ) {

        }

    }
}

interface OnArticlieClickListener {
 fun itemClick(myArticle: Article)
 fun imageClick(myArticle: Article)
}
