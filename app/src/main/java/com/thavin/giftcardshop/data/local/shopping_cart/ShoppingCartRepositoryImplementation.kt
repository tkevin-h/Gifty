package com.thavin.giftcardshop.data.local.shopping_cart

import com.thavin.giftcardshop.data.local.shopping_cart.database.ShoppingCartDao
import com.thavin.giftcardshop.data.local.shopping_cart.database.GiftCardEntity
import com.thavin.giftcardshop.domain.local.shopping_cart.ShoppingCartRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ShoppingCartRepositoryImplementation @Inject constructor(
    private val dao: ShoppingCartDao
) : ShoppingCartRepository {
    override suspend fun insertGiftCard(giftCardEntity: GiftCardEntity) =
        dao.insertGiftCard(giftCardEntity)

    override suspend fun deleteGiftCard(giftCardEntity: GiftCardEntity) =
        dao.deleteGiftCard(giftCardEntity)

    override suspend fun deleteCart(giftCardEntities: List<GiftCardEntity>, dispatcher: CoroutineDispatcher) =
        withContext(dispatcher) {
            giftCardEntities.forEach { giftCardEntity ->
                dao.deleteGiftCard(giftCardEntity)
            }
        }

    override fun getCartCount(): Flow<Int> =
        dao.getCartCount()

    override fun getCart(): Flow<List<GiftCardEntity>> =
        dao.getCart()
}