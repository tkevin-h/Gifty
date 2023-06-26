package com.thavin.giftcardshop.domain.local.shopping_cart

import com.thavin.giftcardshop.data.local.shopping_cart.database.GiftCardEntity
import kotlinx.coroutines.flow.Flow

interface ShoppingCartRepository {
    suspend fun insertGiftCard(giftCardEntity: GiftCardEntity)

    suspend fun deleteGiftCard(giftCardEntity: GiftCardEntity)

    suspend fun deleteCart(giftCardEntities: List<GiftCardEntity>)

    fun getCartCount(): Flow<Int>

    fun getCart(): Flow<List<GiftCardEntity>>
}