package com.thavin.giftcardshop.domain.remote.gift_cards

import com.thavin.giftcardshop.data.remote.gift_cards.api.GiftCardDto
import com.thavin.giftcardshop.domain.resource.DataResult

interface GiftCardsRepository {
    suspend fun getGiftCards(): DataResult<List<GiftCardDto>>
}