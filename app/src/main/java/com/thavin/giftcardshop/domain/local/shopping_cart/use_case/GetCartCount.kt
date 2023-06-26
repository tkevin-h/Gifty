package com.thavin.giftcardshop.domain.local.shopping_cart.use_case

import com.thavin.giftcardshop.domain.local.shopping_cart.ShoppingCartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCartCount @Inject constructor(
    private val repository: ShoppingCartRepository
) {

    operator fun invoke(): Flow<Int> =
        repository.getCartCount()
}