package com.thavin.giftcardshop.data.remote.gift_cards.api

import retrofit2.http.GET

interface GiftCardsApi {
    companion object {
        const val BASE_URL = "https://zip.co/giftcards/api/"
    }

    @GET("giftcards")
    suspend fun getGiftCards(): List<GiftCardDto>
}