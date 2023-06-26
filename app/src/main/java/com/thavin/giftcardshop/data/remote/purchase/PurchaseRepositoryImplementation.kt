package com.thavin.giftcardshop.data.remote.purchase

import com.thavin.giftcardshop.data.remote.purchase.api.PurchaseApi
import com.thavin.giftcardshop.data.remote.purchase.api.PurchaseDto
import com.thavin.giftcardshop.domain.remote.purchase.PurchaseRepository
import com.thavin.giftcardshop.domain.resource.DataResult
import javax.inject.Inject

class PurchaseRepositoryImplementation @Inject constructor(
    private val purchaseApi: PurchaseApi
) : PurchaseRepository {
    override suspend fun purchase(purchaseDto: PurchaseDto): DataResult<PurchaseDto> =
        try {
            val result = purchaseApi.purchaseGiftCard(purchaseDto)
            DataResult.Success(data = result)
        } catch (e: Exception) {
            DataResult.Error(message = e.toString())
        }
}