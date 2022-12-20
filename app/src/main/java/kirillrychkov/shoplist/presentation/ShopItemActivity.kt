package kirillrychkov.shoplist.presentation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import kirillrychkov.shoplist.R
import kirillrychkov.shoplist.domain.ShopItem

class ShopItemActivity : AppCompatActivity() {

//    private lateinit var viewModel: ShopItemViewModel
//    private lateinit var tilName: TextInputLayout
//    private lateinit var tilCount: TextInputLayout
//    private lateinit var etName: EditText
//    private lateinit var etCount: EditText
//    private lateinit var btnSave: Button
    private var shopItemId = ShopItem.UNDEFINED_ID
    private var screenMode = UNKNOWN_MODE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)
        parseIntent()
        val fragment = when(screenMode){
            EDIT_MODE -> {
                ShopItemFragment.newInstanceEditItem(shopItemId)
            }
            ADD_MODE -> {
                ShopItemFragment.newInstanceAddItem()
            }
            else -> {
                throw RuntimeException("Unknown mode $screenMode")
            }
        }
        supportFragmentManager.beginTransaction()
            .add(R.id.shop_item_container, fragment)
            .commit()
    }


//
//    private fun launchEditMode(){
//        viewModel.getShopItem(shopItemId)
//        viewModel.shopItem.observe(this){
//            etName.setText(it.name)
//            etCount.setText(it.count.toString())
//        }
//        btnSave.setOnClickListener {
//            viewModel.editShopItem(etName.text?.toString(), etCount.text?.toString())
//        }
//    }
//
//    private fun launchAddMode(){
//        btnSave.setOnClickListener {
//            viewModel.addShopItem(etName.text?.toString(), etCount.text?.toString())
//        }
//    }
//

    private fun parseIntent(){
        if(!intent.hasExtra(EXTRA_SCREEN_MODE)){
            throw RuntimeException("Param screen mode is absent")
        }
        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        if(mode != ADD_MODE && mode != EDIT_MODE){
            throw RuntimeException("Unknown screen mode $mode")
        }
        screenMode = mode
        if(screenMode == EDIT_MODE){
            if(!intent.hasExtra(EXTRA_SHOP_ITEM_ID)){
                throw RuntimeException("Param shop item id is absent")
            }
            shopItemId = intent.getIntExtra(EXTRA_SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }

    companion object{
        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val ADD_MODE = "add_mode"
        private const val EDIT_MODE = "edit_mode"
        private const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"
        private const val UNKNOWN_MODE = ""

        fun newIntentAddItem(context: Context) : Intent{
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, ADD_MODE)
            return intent
        }
        fun newIntentEditItem(context: Context, shopItemId: Int) : Intent{
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, EDIT_MODE)
            intent.putExtra(EXTRA_SHOP_ITEM_ID, shopItemId)
            return intent
        }
    }
}
