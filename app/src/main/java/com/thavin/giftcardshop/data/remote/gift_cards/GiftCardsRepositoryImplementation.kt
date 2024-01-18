package com.thavin.giftcardshop.data.remote.gift_cards

import com.thavin.giftcardshop.data.remote.gift_cards.api.GiftCardDto
import com.thavin.giftcardshop.data.remote.gift_cards.api.GiftCardsApi
import com.thavin.giftcardshop.domain.remote.gift_cards.GiftCardsRepository
import com.thavin.giftcardshop.domain.resource.DataResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GiftCardsRepositoryImplementation @Inject constructor(
    private val giftCardsApi: GiftCardsApi
) : GiftCardsRepository {

    override suspend fun getGiftCards(): DataResult<List<GiftCardDto>> =
        withContext(Dispatchers.IO) {
            try {
                val result = giftCardsApi.getGiftCards()
                DataResult.Success(data = result)
            } catch (e: Exception) {
                DataResult.Error(message = e.toString())
            }
        }
}