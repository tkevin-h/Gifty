package com.thavin.giftcardshop.domain.local.shopping_cart.use_case

import com.thavin.giftcardshop.domain.local.shopping_cart.ShoppingCartData
import com.thavin.giftcardshop.domain.local.shopping_cart.ShoppingCartRepository
import com.thavin.giftcardshop.domain.local.shopping_cart.toGiftCardEntity
import javax.inject.Inject

class DeleteGiftCard @Inject constructor(
    private val repository: ShoppingCartRepository
) {

    suspend operator fun invoke(shoppingCartData: ShoppingCartData) {
        repository.deleteGiftCard(shoppingCartData.toGiftCardEntity())
    }
}