package com.thavin.giftcardshop.data.local.shopping_cart

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.thavin.giftcardshop.data.local.shopping_cart.database.GiftCardEntity
import com.thavin.giftcardshop.data.local.shopping_cart.database.ShoppingCartDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ShoppingCartRepositoryImplementationTest {

    private lateinit var shoppingCartRepositoryImplementation: ShoppingCartRepositoryImplementation
    private lateinit var shoppingCartDatabase: ShoppingCartDatabase

    private val giftCardEntity = GiftCardEntity(
        brand = "test",
        amount = 20.0,
        discountedAmount = 10.0,
        imageUrl = "testUrl"
    )

    @Before
    fun beforeTest() {
        shoppingCartDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ShoppingCartDatabase::class.java
        ).build()

        shoppingCartRepositoryImplementation = ShoppingCartRepositoryImplementation(shoppingCartDatabase.dao)
    }

    @After
    fun afterTests() {
        shoppingCartDatabase.close()
    }

    @Test
    fun insertGiftCard() = runBlocking {
        shoppingCartRepositoryImplementation.insertGiftCard(giftCardEntity)

        val expectedGiftCardEntity = GiftCardEntity(
            id = 1,
            brand = "test",
            amount = 20.0,
            discountedAmount = 10.0,
            imageUrl = "testUrl"
        )

        val giftCards = shoppingCartRepositoryImplementation.getCart()

        assertThat(giftCards.first()[0]).isEqualTo(expectedGiftCardEntity)
    }

    @Test
    fun deleteGiftCard() = runBlocking {
        shoppingCartRepositoryImplementation.insertGiftCard(giftCardEntity)

        val expectedGiftCardEntity = GiftCardEntity(
            id = 1,
            brand = "test",
            amount = 20.0,
            discountedAmount = 10.0,
            imageUrl = "testUrl"
        )

        shoppingCartRepositoryImplementation.deleteGiftCard(expectedGiftCardEntity)

        val giftCards = shoppingCartRepositoryImplementation.getCart()

        assertThat(giftCards.first()).isEqualTo(emptyList<GiftCardEntity>())
    }

    @Test
    fun getNumberOfItemsInCart() = runBlocking {
        shoppingCartRepositoryImplementation.insertGiftCard(giftCardEntity)

        val expectedCartCount = 1

        val cartCount = shoppingCartRepositoryImplementation.getCartCount()

        assertThat(cartCount.first()).isEqualTo(expectedCartCount)
    }
}