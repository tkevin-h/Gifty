package com.thavin.giftcardshop.domain.local.shopping_cart

data class ShoppingCartData(
    val id: Int? = null,
    val brand: String,
    val amount: Double,
    val discountedAmount: Double,
    val imageUrl: String
)