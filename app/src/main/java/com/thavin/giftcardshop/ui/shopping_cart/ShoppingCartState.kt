package com.thavin.giftcardshop.ui.shopping_cart

import com.thavin.giftcardshop.domain.local.shopping_cart.ShoppingCartData

data class ShoppingCartState(
    var isCartLoading: Boolean = false,
    var isPurchaseCartLoading: Boolean = false,
    var isError: Boolean = false,
    var shoppingCartData: List<ShoppingCartData> = emptyList(),
    var amount: Double = 0.0,
    var isEmptyCart: Boolean = true,
    var isPurchaseButtonEnabled: Boolean = false,
    var isBackButtonEnabled: Boolean = true
)