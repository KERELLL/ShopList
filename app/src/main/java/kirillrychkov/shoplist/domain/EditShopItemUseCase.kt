package kirillrychkov.shoplist.domain

class EditShopItemUseCase(private val shopListRepository: ShopListRepository) {
    fun editShopItem(shopItem: ShopItem) {
        return shopListRepository.editShopItem(shopItem)
    }
}