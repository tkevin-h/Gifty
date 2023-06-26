package com.thavin.giftcardshop.ui.gift_card_details

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.thavin.giftcardshop.domain.local.shopping_cart.use_case.ShoppingCartUseCases
import com.thavin.giftcardshop.domain.remote.gift_cards.GiftCardData
import com.thavin.giftcardshop.domain.remote.purchase.PurchaseData
import com.thavin.giftcardshop.domain.remote.purchase.PurchaseUseCase
import com.thavin.giftcardshop.domain.resource.DataResult
import com.thavin.giftcardshop.ui.navigation.Routes
import com.thavin.giftcardshop.ui.toCurrencyFormat
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
class GiftCardDetailsViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    private lateinit var giftCardDetailsViewModel: GiftCardDetailsViewModel
    private val purchaseUseCase = mockk<PurchaseUseCase>(relaxed = true)
    private val shoppingCartUseCases = mockk<ShoppingCartUseCases>(relaxed = true)

    private val amount = 20.00
    private val discountAmount = 10.00
    private val purchaseData = PurchaseData(amount = 50.5)
    private val giftCardData = GiftCardData(
        brand = "test",
        amounts = listOf(11.0, 23.1),
        discountOffTotal = listOf(19.0, 44.1),
        discount = "20%",
        imageUrl = "testImage",
        terms = "testTerms",
        vendor = "testVendor"
    )
    private val cartCount = 1

    @Before
    fun beforeTest() {
        Dispatchers.setMain(dispatcher)

        giftCardDetailsViewModel = GiftCardDetailsViewModel(purchaseUseCase, shoppingCartUseCases)

        coEvery { shoppingCartUseCases.getCartCount() } returns flowOf(cartCount)
    }

    @After
    fun afterTest() {
        Dispatchers.resetMain()
    }

    @Test
    fun `The gift card screen is initialised`() {
        with(giftCardDetailsViewModel.giftCardsDetailsState.value) {
            assertThat(amount).isEqualTo(0.0)
            assertThat(discountedAmount).isEqualTo(0.0)
            assertThat(isPurchaseLoading).isFalse()
            assertThat(isBuyNowButtonEnabled).isFalse()
            assertThat(isAddToCartButtonEnabled).isFalse()
            assertThat(isBackButtonEnabled).isTrue()
            assertThat(isCartButtonEnabled).isTrue()
        }
    }

    @Test
    fun `An amount is selected`() {
        giftCardDetailsViewModel.onEvent(
            GiftCardDetailsViewModel.ClickEvent.AmountOnClick(
                amount,
                discountAmount
            )
        )

        val expectedAmount = 20.00
        val expectedDiscountedAmount = 10.00

        with(giftCardDetailsViewModel.giftCardsDetailsState.value) {
            assertThat(amount).isEqualTo(expectedAmount)
            assertThat(discountedAmount).isEqualTo(expectedDiscountedAmount)
            assertThat(isBuyNowButtonEnabled).isEqualTo(true)
            assertThat(isAddToCartButtonEnabled).isEqualTo(true)
        }
    }

    @Test
    fun `The cart button is clicked`() = runTest {
        giftCardDetailsViewModel.onEvent(GiftCardDetailsViewModel.ClickEvent.CartOnClick)

        giftCardDetailsViewModel.uiEvent.test {
            val emission = awaitItem()
            assertThat(emission).isEqualTo(GiftCardDetailsViewModel.UiEvent.Navigate(Routes.SHOPPING_CART.name))
        }
    }

    @Test
    fun `The back button is clicked`() = runTest {
        giftCardDetailsViewModel.onEvent(GiftCardDetailsViewModel.ClickEvent.BackOnClick)

        giftCardDetailsViewModel.uiEvent.test {
            val emission = awaitItem()
            assertThat(emission).isEqualTo(GiftCardDetailsViewModel.UiEvent.NavigateBack)
        }
    }

    @Test
    fun `The buy now button is clicked and is successful`() = runTest {
        coEvery { purchaseUseCase.purchase(any()) } returns DataResult.Success(purchaseData)

        giftCardDetailsViewModel.giftCardsDetailsState.value =
            giftCardDetailsViewModel.giftCardsDetailsState.value.copy(
                amount = 50.5
            )

        giftCardDetailsViewModel.onEvent(GiftCardDetailsViewModel.ClickEvent.BuyNowOnClick)

        // Pre State
        with(giftCardDetailsViewModel.giftCardsDetailsState.value) {
            assertThat(isPurchaseLoading).isTrue()
            assertThat(isBuyNowButtonEnabled).isFalse()
            assertThat(isAddToCartButtonEnabled).isFalse()
            assertThat(isBackButtonEnabled).isFalse()
            assertThat(isCartButtonEnabled).isFalse()
        }

        giftCardDetailsViewModel.uiEvent.test {
            listOf(
                GiftCardDetailsViewModel.UiEvent.SetReceiptAmount(50.5.toCurrencyFormat()),
                GiftCardDetailsViewModel.UiEvent.Navigate(Routes.GIFT_CARD_RECEIPT.name)
            ).forEach { uiEvent ->
                val emission = awaitItem()
                assertThat(emission).isEqualTo(uiEvent)
            }
        }

        dispatcher.scheduler.advanceUntilIdle()
        // Post State
        assertThat(giftCardDetailsViewModel.giftCardsDetailsState.value.isPurchaseLoading).isFalse()
    }

    @Test
    fun `The buy now button is clicked and is not successful`() = runTest {
        coEvery { purchaseUseCase.purchase(any()) } returns DataResult.Error()

        giftCardDetailsViewModel.onEvent(GiftCardDetailsViewModel.ClickEvent.BuyNowOnClick)

        // Pre State
        with(giftCardDetailsViewModel.giftCardsDetailsState.value) {
            assertThat(isPurchaseLoading).isTrue()
            assertThat(isBuyNowButtonEnabled).isFalse()
            assertThat(isAddToCartButtonEnabled).isFalse()
            assertThat(isBackButtonEnabled).isFalse()
            assertThat(isCartButtonEnabled).isFalse()
        }

        giftCardDetailsViewModel.uiEvent.test {
            val emission = awaitItem()
            assertThat(emission).isEqualTo(
                GiftCardDetailsViewModel.UiEvent.ShowSnackBar(
                    "Purchase failed",
                    "Try again"
                )
            )
        }

        dispatcher.scheduler.advanceUntilIdle()

        // Post State
        with(giftCardDetailsViewModel.giftCardsDetailsState.value) {
            assertThat(isPurchaseLoading).isFalse()
            assertThat(isBuyNowButtonEnabled).isTrue()
            assertThat(isAddToCartButtonEnabled).isTrue()
            assertThat(isBackButtonEnabled).isTrue()
            assertThat(isCartButtonEnabled).isTrue()
        }
    }

    @Test
    fun `The add to cart button is clicked`() = runTest {
        giftCardDetailsViewModel.onEvent(
            GiftCardDetailsViewModel.ClickEvent.AddToCartOnClick(
                giftCardData
            )
        )

        giftCardDetailsViewModel.uiEvent.test {
            listOf(
                GiftCardDetailsViewModel.UiEvent.SetCartCount(count = 1),
                GiftCardDetailsViewModel.UiEvent.ShowSnackBar("Added to cart"),
            ).forEach { uiEvent ->
                val emission = awaitItem()
                assertThat(emission).isEqualTo(uiEvent)
            }
        }
    }
}