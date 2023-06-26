package com.thavin.giftcardshop.data.local.shopping_cart.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    exportSchema = true,
    entities = [GiftCardEntity::class],
    version = 1
)
abstract class ShoppingCartDatabase : RoomDatabase() {

    abstract val dao: ShoppingCartDao
}