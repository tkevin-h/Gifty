package com.thavin.giftcardshop.domain.remote.gift_cards

data class GiftCardData(
    val brand: String,
    val amounts: List<Double>,
    val discountOffTotal: List<Double>,
    val discount: String,
    val imageUrl: String,
    val terms: String,
    val vendor: String,
)