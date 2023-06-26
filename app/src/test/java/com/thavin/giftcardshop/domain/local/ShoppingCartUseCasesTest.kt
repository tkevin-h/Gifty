package com.thavin.giftcardshop.domain.local

import com.google.common.truth.Truth.assertThat
import com.thavin.giftcardshop.data.local.shopping_cart.database.GiftCardEntity
import com.thavin.giftcardshop.domain.local.shopping_cart.ShoppingCartData
import com.thavin.giftcardshop.domain.local.shopping_cart.ShoppingCartRepository
import com.thavin.giftcardshop.domain.local.shopping_cart.use_case.*
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ShoppingCartUseCasesTest {

    private val dispatcher = StandardTestDispatcher()

    private lateinit var shoppingCartUseCases: ShoppingCartUseCases
    private val shoppingCartRepository = mockk<ShoppingCartRepository>(relaxed = true)

    private val giftCardEntities = listOf(
        GiftCardEntity(
            brand = "test",
            amount = 20.0,
            discountedAmount = 11.0,
            imageUrl = "testUrl"
        )
    )

    private val expectedShoppingCartData = listOf(
        ShoppingCartData(
            brand = "test",
            amount = 20.0,
            discountedAmount = 11.0,
            imageUrl = "testUrl"
        )
    )

    private val cartCount = 1

    @Before
    fun beforeTest() {
        Dispatchers.setMain(dispatcher)

        shoppingCartUseCases = ShoppingCartUseCases(
            insertGiftCard = InsertGiftCard(shoppingCartRepository),
            deleteGiftCard = DeleteGiftCard(shoppingCartRepository),
            deleteCart = DeleteCart(shoppingCartRepository),
            getCartCount = GetCartCount(shoppingCartRepository),
            getShoppingCart = GetShoppingCart(shoppingCartRepository)
        )
    }

    @After
    fun afterTest() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Retrieving all gift cards`() = runTest {
        coEvery { shoppingCartRepository.getCart() } returns flowOf(giftCardEntities)

        val shoppingCartItems = shoppingCartUseCases.getShoppingCart().first()

        assertThat(shoppingCartItems).isEqualTo(expectedShoppingCartData)
    }

    @Test
    fun `Getting the cart count`() = runTest {
        coEvery { shoppingCartRepository.getCartCount() } returns flowOf(cartCount)

        val shoppingCartItems = shoppingCartUseCases.getCartCount().first()
        val expectedCartCount = 1

        assertThat(shoppingCartItems).isEqualTo(expectedCartCount)
    }
}