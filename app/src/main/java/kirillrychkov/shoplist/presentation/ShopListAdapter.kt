package kirillrychkov.shoplist.presentation

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kirillrychkov.shoplist.R
import kirillrychkov.shoplist.domain.ShopItem

// androidx.recyclerview.widget.ListAdapter<ShopItem, ShopItemViewHolder>(ShopItemDiffCallback())
// Способ через DiffUtilItem

class ShopListAdapter() : RecyclerView.Adapter<ShopItemViewHolder>(){

    var shopList = listOf<ShopItem>()
    set(value){
        val callback = ShopListDiffCallback(shopList, value)
        val diffResult = DiffUtil.calculateDiff(callback)
        diffResult.dispatchUpdatesTo(this)
        field = value
    }

//    var onShopItemLongClickListener: OnShopItemLongClickListener? = null
    var onShopItemLongClickListener: ((ShopItem) -> Unit)? = null
    var onShopItemClickListener: ((ShopItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val layout = when(viewType){
            VIEW_TYPE_ENABLED -> R.layout.item_shop_enabled
            VIEW_TYPE_DISABLED -> R.layout.item_shop_desabled
            else -> {throw RuntimeException("Unknown view type: $viewType")}
        }
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ShopItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        val shopItem = shopList[position] //getItem(position)
        holder.tvName.text = shopItem.name
        holder.tvCount.text = shopItem.count.toString()
        holder.itemView.setOnLongClickListener {
            onShopItemLongClickListener?.invoke(shopItem)
            true
        }
        holder.itemView.setOnClickListener {
            onShopItemClickListener?.invoke(shopItem)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val itemView = shopList[position] //getItem(position)
        if(itemView.enabled){
            return VIEW_TYPE_ENABLED
        }else{
            return VIEW_TYPE_DISABLED
        }
    }

//    interface OnShopItemLongClickListener{
//        fun onShopItemLongClick(shopItem: ShopItem)
//    }

    companion object{
        const val VIEW_TYPE_ENABLED = 0
        const val VIEW_TYPE_DISABLED = 1
        const val MAX_POOL_SIZE = 5
    }

    override fun getItemCount(): Int {
        return shopList.size
    }
}