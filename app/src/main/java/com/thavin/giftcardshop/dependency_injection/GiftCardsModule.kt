package com.thavin.giftcardshop.dependency_injection

import android.app.Application
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.thavin.giftcardshop.domain.local.shopping_cart.ShoppingCartRepository
import com.thavin.giftcardshop.data.local.shopping_cart.ShoppingCartRepositoryImplementation
import com.thavin.giftcardshop.data.local.shopping_cart.database.ShoppingCartDatabase
import com.thavin.giftcardshop.domain.remote.gift_cards.GiftCardsRepository
import com.thavin.giftcardshop.data.remote.gift_cards.api.GiftCardsApi
import com.thavin.giftcardshop.data.remote.gift_cards.GiftCardsRepositoryImplementation
import com.thavin.giftcardshop.domain.remote.purchase.PurchaseRepository
import com.thavin.giftcardshop.data.remote.purchase.api.PurchaseApi
import com.thavin.giftcardshop.data.remote.purchase.PurchaseRepositoryImplementation
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GiftCardsModule {
    private const val APPLICATION_JSON = "application/json"

    @Provides
    @Singleton
    fun providesGiftCardsRepository(retrofit: Retrofit): GiftCardsRepository =
        GiftCardsRepositoryImplementation(retrofit)

    @Provides
    @Singleton
    fun providesPurchaseRepository(purchaseApi: PurchaseApi): PurchaseRepository =
        PurchaseRepositoryImplementation(purchaseApi)

    @Provides
    @Singleton
    fun providesCartRepository(db: ShoppingCartDatabase): ShoppingCartRepository =
        ShoppingCartRepositoryImplementation(db.dao)

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun providesGiftCardsApi(json: Json, client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(GiftCardsApi.BASE_URL)
            .addConverterFactory(json.asConverterFactory(APPLICATION_JSON.toMediaType()))
            .client(client)
            .build()

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun providesPurchaseApi(json: Json, client: OkHttpClient): PurchaseApi =
        Retrofit.Builder()
            .baseUrl(PurchaseApi.BASE_URL)
            .addConverterFactory(json.asConverterFactory(APPLICATION_JSON.toMediaType()))
            .client(client)
            .build()
            .create(PurchaseApi::class.java)

    @Provides
    @Singleton
    fun providesCartDatabase(app: Application): ShoppingCartDatabase =
        Room.databaseBuilder(app, ShoppingCartDatabase::class.java, "cart_db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun providesOkHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder().apply {
            this
                .addInterceptor(interceptor = interceptor)
                .connectTimeout(20, TimeUnit.SECONDS)
        }.build()

    @Provides
    @Singleton
    fun providesHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }


    @Provides
    @Singleton
    fun providesJsonBuilder(): Json =
        Json {
            isLenient = true
            ignoreUnknownKeys = true
        }
}