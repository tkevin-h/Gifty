package com.thavin.giftcardshop.data.local.shopping_cart.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingCartDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertGiftCard(giftCardEntity: GiftCardEntity)

    @Delete
    suspend fun deleteGiftCard(giftCardEntity: GiftCardEntity)

    @Query("SELECT * FROM shoppingcart")
    fun getCart(): Flow<List<GiftCardEntity>>

    @Query("SELECT COUNT(*) FROM shoppingcart")
    fun getCartCount(): Flow<Int>
}