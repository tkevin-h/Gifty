package com.thavin.giftcardshop.domain.remote.purchase

import com.thavin.giftcardshop.data.remote.purchase.api.PurchaseDto

fun PurchaseData.toPurchaseDto() =
    PurchaseDto(
        amount = amount
    )

fun PurchaseDto.toPurchaseData() =
    PurchaseData(
        amount = amount
    )