package com.thavin.giftcardshop.domain.remote.purchase

import com.thavin.giftcardshop.data.remote.purchase.api.PurchaseDto
import com.thavin.giftcardshop.domain.resource.DataResult

interface PurchaseRepository {
    suspend fun purchase(purchaseDto: PurchaseDto): DataResult<PurchaseDto>
}