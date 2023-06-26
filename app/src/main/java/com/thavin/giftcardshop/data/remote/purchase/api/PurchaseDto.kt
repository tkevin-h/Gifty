package com.thavin.giftcardshop.data.remote.purchase.api

import kotlinx.serialization.Serializable

@Serializable
data class PurchaseDto(
    val amount: Double
)