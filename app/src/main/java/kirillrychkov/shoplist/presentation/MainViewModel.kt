package kirillrychkov.shoplist.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kirillrychkov.shoplist.data.ShopListRepositoryImpl
import kirillrychkov.shoplist.domain.*

class MainViewModel : ViewModel() {

    private val repository = ShopListRepositoryImpl

    private val getShopListUseCase = GetShopListUseCase(repository)
    private val removeShopItemUseCase = RemoveShopItemUseCase(repository)
    private val addShopItemUseCase = AddShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)

    val shopList = getShopListUseCase.getShopList()

    fun removeShopItem(shopItem: ShopItem){
        removeShopItemUseCase.removeShopItem(shopItem)
    }

    fun changeEnabled(shopItem: ShopItem){
        val isEnabled = !shopItem.enabled
        val changedItem = ShopItem(shopItem.name, shopItem.count, isEnabled, shopItem.id)
        editShopItemUseCase.editShopItem(changedItem)
    }
}