package com.thavin.giftcardshop.domain.remote.purchase

import com.thavin.giftcardshop.domain.resource.DataResult
import javax.inject.Inject

class PurchaseUseCase @Inject constructor(
    private val purchaseRepository: PurchaseRepository
) {
    suspend fun purchase(purchaseData: PurchaseData): DataResult<PurchaseData> =
        when (val result = purchaseRepository.purchase(purchaseData.toPurchaseDto())) {
            is DataResult.Success -> DataResult.Success(result.data.toPurchaseData())
            is DataResult.Error -> DataResult.Error(message = result.message)
        }
}