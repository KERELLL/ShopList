package kirillrychkov.shoplist.presentation

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kirillrychkov.shoplist.R

class ShopItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val tvName: TextView = itemView.findViewById<TextView>(R.id.tv_name)
    val tvCount: TextView = itemView.findViewById<TextView>(R.id.tv_count)
}