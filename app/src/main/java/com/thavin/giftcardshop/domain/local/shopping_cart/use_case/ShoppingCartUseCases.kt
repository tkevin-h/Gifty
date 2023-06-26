package com.thavin.giftcardshop.domain.local.shopping_cart.use_case

import javax.inject.Inject

data class ShoppingCartUseCases @Inject constructor(
    val insertGiftCard: InsertGiftCard,
    val deleteGiftCard: DeleteGiftCard,
    val deleteCart: DeleteCart,
    val getCartCount: GetCartCount,
    val getShoppingCart: GetShoppingCart
)