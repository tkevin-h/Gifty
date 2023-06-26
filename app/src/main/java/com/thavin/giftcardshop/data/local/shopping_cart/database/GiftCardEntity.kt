package com.thavin.giftcardshop.data.local.shopping_cart.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shoppingcart")
data class GiftCardEntity(
    @PrimaryKey val id: Int? = null,
    val brand: String,
    val amount: Double,
    val discountedAmount: Double,
    val imageUrl: String
)