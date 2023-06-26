package com.thavin.giftcardshop.ui.gift_card_details

data class GiftCardsDetailState(
    var amount: Double = 0.0,
    var discountedAmount: Double = 0.0,
    var isPurchaseLoading: Boolean = false,
    var isBuyNowButtonEnabled: Boolean = false,
    var isAddToCartButtonEnabled: Boolean = false,
    var isBackButtonEnabled: Boolean = true,
    var isCartButtonEnabled: Boolean = true
)