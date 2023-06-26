package com.thavin.giftcardshop.domain.local.shopping_cart

import com.thavin.giftcardshop.data.local.shopping_cart.database.GiftCardEntity
import com.thavin.giftcardshop.domain.remote.gift_cards.GiftCardData

fun ShoppingCartData.toGiftCardEntity() =
    GiftCardEntity(
        id = id,
        brand = brand,
        amount = amount,
        discountedAmount = discountedAmount,
        imageUrl = imageUrl,
    )

fun GiftCardEntity.toShoppingCartData() =
    ShoppingCartData(
        id = id,
        brand = brand,
        amount = amount,
        discountedAmount = discountedAmount,
        imageUrl = imageUrl,
    )

fun GiftCardData.toShoppingCartData(amount: Double, discountedAmount: Double) =
    ShoppingCartData(
        brand = brand,
        amount = amount,
        discountedAmount = discountedAmount,
        imageUrl = imageUrl
    )
