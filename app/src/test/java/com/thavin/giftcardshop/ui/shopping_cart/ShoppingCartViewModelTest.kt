package com.thavin.giftcardshop.ui.shopping_cart

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.thavin.giftcardshop.domain.local.shopping_cart.ShoppingCartData
import com.thavin.giftcardshop.domain.local.shopping_cart.use_case.ShoppingCartUseCases
import com.thavin.giftcardshop.domain.remote.purchase.PurchaseData
import com.thavin.giftcardshop.domain.remote.purchase.PurchaseUseCase
import com.thavin.giftcardshop.domain.resource.DataResult
import com.thavin.giftcardshop.ui.navigation.Routes
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ShoppingCartViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    private lateinit var shoppingCartViewModel: ShoppingCartViewModel
    private val shoppingCartUseCases = mockk<ShoppingCartUseCases>(relaxed = true)
    private val purchaseUseCase = mockk<PurchaseUseCase>(relaxed = true)

    private val purchaseData = PurchaseData(
        amount = 100.00
    )
    private val shoppingCartListData = listOf(
        ShoppingCartData(
            brand = "test",
            amount = 100.00,
            discountedAmount = 10.00,
            imageUrl = "testUrl"
        ),
        ShoppingCartData(
            brand = "test2",
            amount = 12.00,
            discountedAmount = 10.00,
            imageUrl = "testUrl"
        )
    )
    private val shoppingCartData = ShoppingCartData(
        brand = "test",
        amount = 100.00,
        discountedAmount = 10.00,
        imageUrl = "testUrl"
    )
    private val cartCount = 1

    @Before
    fun beforeTest() {
        Dispatchers.setMain(dispatcher)

        shoppingCartViewModel = ShoppingCartViewModel(shoppingCartUseCases, purchaseUseCase)

        coEvery { shoppingCartUseCases.getCartCount() } returns flowOf(cartCount)
    }

    @After
    fun afterTest() {
        Dispatchers.resetMain()
    }

    @Test
    fun `The shopping cart screen is initialised`() = runTest {
        coEvery { shoppingCartUseCases.getShoppingCart() } returns flowOf(shoppingCartListData)

        val expectedShoppingCartData = listOf(
            ShoppingCartData(
                brand = "test",
                amount = 100.00,
                discountedAmount = 10.00,
                imageUrl = "testUrl"
            ),
            ShoppingCartData(
                brand = "test2",
                amount = 12.00,
                discountedAmount = 10.00,
                imageUrl = "testUrl"
            )
        )

        // Pre State
        assertThat(shoppingCartViewModel.cartState.value.isCartLoading).isTrue()

        dispatcher.scheduler.advanceUntilIdle()

        // Post State
        with(shoppingCartViewModel.cartState.value) {
            assertThat(isCartLoading).isFalse()
            assertThat(amount).isEqualTo(20.0)
            assertThat(isPurchaseCartLoading).isFalse()
            assertThat(isError).isFalse()
            assertThat(shoppingCartData).isEqualTo(expectedShoppingCartData)
            assertThat(isBackButtonEnabled).isTrue()
        }
    }

    @Test
    fun `The purchase cart button is clicked and it is successful`() = runTest {
        coEvery { purchaseUseCase.purchase(any()) } returns DataResult.Success(purchaseData)

        shoppingCartViewModel.onEvent(ShoppingCartViewModel.CartEvent.PurchaseCartOnClick)

        // Pre State
        with(shoppingCartViewModel.cartState.value) {
            assertThat(isCartLoading).isTrue()
            assertThat(isPurchaseButtonEnabled).isFalse()
            assertThat(isBackButtonEnabled).isFalse()
        }

        shoppingCartViewModel.uiEvent.test {
            listOf(
                ShoppingCartViewModel.UiEvent.SetCartCount(count = 1),
                ShoppingCartViewModel.UiEvent.Navigate(Routes.GIFT_CARD_RECEIPT.name),
                ShoppingCartViewModel.UiEvent.SetCartCount(count = 1)
            ).forEach { uiEvent ->
                val emission = awaitItem()
                assertThat(emission).isEqualTo(uiEvent)
            }
        }
    }

    @Test
    fun `The purchase cart button is clicked and it is not successful`() = runTest {
        coEvery { purchaseUseCase.purchase(any()) } returns DataResult.Error()

        shoppingCartViewModel.onEvent(ShoppingCartViewModel.CartEvent.PurchaseCartOnClick)

        // Pre State
        with(shoppingCartViewModel.cartState.value) {
            assertThat(isCartLoading).isTrue()
            assertThat(isPurchaseButtonEnabled).isFalse()
            assertThat(isBackButtonEnabled).isFalse()
        }

        shoppingCartViewModel.uiEvent.test {
            val emission = awaitItem()
            assertThat(emission).isEqualTo(
                ShoppingCartViewModel.UiEvent.ShowSnackBar(
                    "Purchase failed",
                    "Try again"
                )
            )
        }

        dispatcher.scheduler.advanceUntilIdle()

        // Post State
        with(shoppingCartViewModel.cartState.value) {
            assertThat(isPurchaseCartLoading).isFalse()
            assertThat(isPurchaseButtonEnabled).isTrue()
            assertThat(isBackButtonEnabled).isTrue()
        }
    }

    @Test
    fun `The delete cart item button is clicked`() = runTest {
        shoppingCartViewModel.onEvent(
            ShoppingCartViewModel.CartEvent.DeleteCartItemOnClick(
                shoppingCartData
            )
        )

        shoppingCartViewModel.uiEvent.test {
            listOf(
                ShoppingCartViewModel.UiEvent.SetCartCount(count = 1),
                ShoppingCartViewModel.UiEvent.ShowSnackBar("Gift card removed")
            ).forEach { uiEvent ->
                val emission = awaitItem()
                assertThat(emission).isEqualTo(uiEvent)
            }
        }
    }
}