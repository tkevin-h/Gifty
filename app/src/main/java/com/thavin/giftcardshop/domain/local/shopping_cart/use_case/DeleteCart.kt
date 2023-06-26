package com.thavin.giftcardshop.domain.local.shopping_cart.use_case

import com.thavin.giftcardshop.domain.local.shopping_cart.ShoppingCartData
import com.thavin.giftcardshop.domain.local.shopping_cart.ShoppingCartRepository
import com.thavin.giftcardshop.domain.local.shopping_cart.toGiftCardEntity
import javax.inject.Inject

class DeleteCart @Inject constructor(
    private val repository: ShoppingCartRepository
) {
    suspend operator fun invoke(shoppingCartData: List<ShoppingCartData>) {
        repository.deleteCart(shoppingCartData
            .map { giftCardItem -> giftCardItem.toGiftCardEntity() })
    }
}