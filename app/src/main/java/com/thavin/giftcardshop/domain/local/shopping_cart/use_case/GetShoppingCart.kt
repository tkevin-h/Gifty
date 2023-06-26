package com.thavin.giftcardshop.domain.local.shopping_cart.use_case

import com.thavin.giftcardshop.domain.local.shopping_cart.ShoppingCartData
import com.thavin.giftcardshop.domain.local.shopping_cart.ShoppingCartRepository
import com.thavin.giftcardshop.domain.local.shopping_cart.toShoppingCartData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetShoppingCart @Inject constructor(
    private val repository: ShoppingCartRepository
) {
    operator fun invoke(): Flow<List<ShoppingCartData>> =
        repository.getCart().map { items ->
            items.map { entities ->
                entities.toShoppingCartData()
            }
        }
}