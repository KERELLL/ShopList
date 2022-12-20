package kirillrychkov.shoplist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import kirillrychkov.shoplist.R
import kirillrychkov.shoplist.domain.ShopItem

class ShopItemFragment(
    private val shopItemId: Int = ShopItem.UNDEFINED_ID,
    private val screenMode: String = UNKNOWN_MODE
)
    : Fragment() {

    private lateinit var viewModel: ShopItemViewModel
    private lateinit var tilName: TextInputLayout
    private lateinit var tilCount: TextInputLayout
    private lateinit var etName: EditText
    private lateinit var etCount: EditText
    private lateinit var btnSave: Button


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shop_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        parseParams()
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        initViews(view)
        subscribeError()
        setOnTextChange()
        when (screenMode) {
            EDIT_MODE -> {
                launchEditMode()
            }
            ADD_MODE -> {
                launchAddMode()
            }
        }
    }

    private fun subscribeError() {
        viewModel.errorInputCount.observe(viewLifecycleOwner) {
            val errorMessage = if (it) {
                getString(R.string.error_input_count)
            } else {
                null
            }
            tilCount.error = errorMessage
        }

        viewModel.errorInputName.observe(viewLifecycleOwner) {
            val errorMessage = if (it) {
                getString(R.string.error_input_name)
            } else {
                null
            }
            tilName.error = errorMessage
        }

        viewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
            activity?.onBackPressed()
        }
    }

    private fun launchEditMode() {
        viewModel.getShopItem(shopItemId)
        viewModel.shopItem.observe(viewLifecycleOwner) {
            etName.setText(it.name)
            etCount.setText(it.count.toString())
        }
        btnSave.setOnClickListener {
            viewModel.editShopItem(etName.text?.toString(), etCount.text?.toString())
        }
    }

    private fun launchAddMode() {
        btnSave.setOnClickListener {
            viewModel.addShopItem(etName.text?.toString(), etCount.text?.toString())
        }
    }

    private fun setOnTextChange() {
        etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        etCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorCountName()
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }

    private fun parseParams() {
        if(screenMode != ADD_MODE && screenMode != EDIT_MODE){
            throw RuntimeException("Param shop item id is absent")
        }
        if(screenMode == EDIT_MODE && shopItemId == ShopItem.UNDEFINED_ID){
            throw RuntimeException("Param shop item id is absent")
        }
//        if (!requireActivity().intent.hasExtra(EXTRA_SCREEN_MODE)) {
//            throw RuntimeException("Param screen mode is absent")
//        }
//        val mode = requireActivity().intent.getStringExtra(EXTRA_SCREEN_MODE)
//        if (mode != ADD_MODE && mode != EDIT_MODE) {
//            throw RuntimeException("Unknown screen mode $mode")
//        }
//        screenMode = mode
//        if (screenMode == EDIT_MODE) {
//            if (!requireActivity().intent.hasExtra(EXTRA_SHOP_ITEM_ID)) {
//                throw RuntimeException("Param shop item id is absent")
//            }
//            shopItemId = requireActivity().intent.getIntExtra(EXTRA_SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
//        }
    }

    private fun initViews(view: View) {
        tilName = view.findViewById(R.id.til_name)
        tilCount = view.findViewById(R.id.til_count)
        etName = view.findViewById(R.id.et_name)
        etCount = view.findViewById(R.id.et_count)
        btnSave = view.findViewById(R.id.save_button)
    }

    companion object {
        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val ADD_MODE = "add_mode"
        private const val EDIT_MODE = "edit_mode"
        private const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"
        private const val UNKNOWN_MODE = ""

        fun newIntentAddItem(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, ADD_MODE)
            return intent
        }

        fun newIntentEditItem(context: Context, shopItemId: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, EDIT_MODE)
            intent.putExtra(EXTRA_SHOP_ITEM_ID, shopItemId)
            return intent
        }

        fun newInstanceAddItem() : ShopItemFragment{
            return ShopItemFragment(screenMode = ADD_MODE)
        }
        fun newInstanceEditItem(shopItemId: Int) : ShopItemFragment{
            return ShopItemFragment(screenMode = EDIT_MODE, shopItemId = shopItemId)
        }
    }
}