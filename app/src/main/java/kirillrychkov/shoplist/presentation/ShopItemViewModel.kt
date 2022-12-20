package kirillrychkov.shoplist.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kirillrychkov.shoplist.data.ShopListRepositoryImpl
import kirillrychkov.shoplist.domain.AddShopItemUseCase
import kirillrychkov.shoplist.domain.EditShopItemUseCase
import kirillrychkov.shoplist.domain.GetShopItemByIdUseCase
import kirillrychkov.shoplist.domain.ShopItem

class ShopItemViewModel : ViewModel() {

    private val repository = ShopListRepositoryImpl
    private val getShopItemById = GetShopItemByIdUseCase(repository)
    private val addShopItemUseCase = AddShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)
    private val _errorInputName = MutableLiveData<Boolean>()
    val errorInputName : LiveData<Boolean>
    get() = _errorInputName

    private val _errorInputCount = MutableLiveData<Boolean>()
    val errorInputCount : LiveData<Boolean>
        get() = _errorInputCount

    private val _shopItem = MutableLiveData<ShopItem>()
    val shopItem : LiveData<ShopItem>
    get() = _shopItem

    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen : LiveData<Unit> //<Boolean>
        get() = _shouldCloseScreen

    fun getShopItem(shopItemId: Int){
        val item = getShopItemById.getShopItemById(shopItemId)
        _shopItem.postValue(item)
    }

    fun addShopItem(inputName: String?, inputCount: String?){
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val fieldsValid = validateInput(name, count)
        if(fieldsValid)
            addShopItemUseCase.addShopItem(ShopItem(name, count, true))
            _shouldCloseScreen.postValue(Unit) //true
    }

    fun editShopItem(inputName: String?, inputCount: String?){
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val fieldsValid = validateInput(name, count)
        if(fieldsValid){
            _shopItem.value?.let {
                val item = it.copy(name = name, count = count)
                editShopItemUseCase.editShopItem(item)
                _shouldCloseScreen.postValue(Unit) //true
            }
        }
    }

    private fun parseName(inputName: String?): String{
        return inputName?.trim() ?: ""
    }

    private fun parseCount(inputCount: String?): Int{
        return try {
            inputCount?.trim()?.toInt() ?: 0
        } catch (e: Exception){
            0
        }
    }

    private fun validateInput(name: String, count: Int): Boolean{
        var result = true
        if(name.isBlank()){
            _errorInputName.postValue(true)
            result = false
        }
        if(count <= 0){
            _errorInputCount.postValue(true)
            result = false
        }
        return result
    }

    public fun resetErrorInputName(){
        _errorInputName.postValue(false)
    }
    public fun resetErrorCountName(){
        _errorInputName.postValue(false)
    }
}