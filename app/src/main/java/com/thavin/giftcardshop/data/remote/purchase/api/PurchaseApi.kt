package com.thavin.giftcardshop.data.remote.purchase.api

import retrofit2.http.Body
import retrofit2.http.POST

interface PurchaseApi {
    companion object {
        const val BASE_URL = "https://64005b8263e89b0913ace599.mockapi.io/"
    }

    @POST("purchase")
    suspend fun purchaseGiftCard(@Body purchaseDto: PurchaseDto): PurchaseDto
}
