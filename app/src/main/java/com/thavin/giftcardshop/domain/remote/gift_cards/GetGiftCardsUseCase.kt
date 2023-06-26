package com.thavin.giftcardshop.domain.remote.gift_cards

import com.thavin.giftcardshop.domain.resource.DataResult
import javax.inject.Inject

class GetGiftCardsUseCase @Inject constructor(
    private val giftCardsRepository: GiftCardsRepository
) {
    suspend fun getGiftCards(): DataResult<List<GiftCardData>> =
        when (val result = giftCardsRepository.getGiftCards()) {
            is DataResult.Success -> {
                val giftCardsData = result.data
                    .map { giftCardDto ->
                        giftCardDto.toGiftCardData()
                    }
                DataResult.Success(data = giftCardsData)
            }
            is DataResult.Error -> DataResult.Error(message = result.message)
        }
}