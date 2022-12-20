package kirillrychkov.shoplist.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kirillrychkov.shoplist.R
import kirillrychkov.shoplist.domain.ShopItem
import kirillrychkov.shoplist.presentation.ShopItemActivity.Companion.newIntentEditItem
import java.io.Console

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: ShopListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        setupRecyclerView()
        viewModel.shopList.observe(this){
            adapter.shopList = it
            //adapter.submitList(it) если DiffUtilItem
        }
        val buttonAddItem = findViewById<FloatingActionButton>(R.id.button_add_shop_item)
        buttonAddItem.setOnClickListener {
            val intent = ShopItemActivity.newIntentAddItem(this)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView(){
        val rvShopList = findViewById<RecyclerView>(R.id.rv_shop_list)
        adapter = ShopListAdapter()
        rvShopList.adapter = adapter
        rvShopList.recycledViewPool.setMaxRecycledViews(ShopListAdapter.VIEW_TYPE_DISABLED, ShopListAdapter.MAX_POOL_SIZE)
        rvShopList.recycledViewPool.setMaxRecycledViews(ShopListAdapter.VIEW_TYPE_ENABLED, ShopListAdapter.MAX_POOL_SIZE)
        adapter.onShopItemLongClickListener = {
            viewModel.changeEnabled(it)
        }
        adapter.onShopItemClickListener = {
            val intent = newIntentEditItem(this, it.id)
            startActivity(intent)
        }
    }
}