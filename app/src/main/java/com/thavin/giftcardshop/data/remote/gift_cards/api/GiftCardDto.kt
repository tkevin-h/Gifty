package com.thavin.giftcardshop.data.remote.gift_cards.api

import kotlinx.serialization.Serializable

@Serializable
data class GiftCardDto(
    val vendor: String,
    val id: String,
    val brand: String,
    val image: String,
    val denominations: List<Denominations>,
    val position: String,
    val discount: Double,
    val terms: String,
    val importantContent: String,
    val cardTypeStatus: String,
    val disclaimer: String
)

@Serializable
data class Denominations(
    val price: Double,
    val currency: String,
    val stock: String
)
