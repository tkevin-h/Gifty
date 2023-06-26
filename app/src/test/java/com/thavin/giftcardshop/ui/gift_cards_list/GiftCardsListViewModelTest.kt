package com.thavin.giftcardshop.ui.gift_cards_list

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.thavin.giftcardshop.domain.local.shopping_cart.use_case.ShoppingCartUseCases
import com.thavin.giftcardshop.domain.remote.gift_cards.GetGiftCardsUseCase
import com.thavin.giftcardshop.domain.remote.gift_cards.GiftCardData
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
class GiftCardsListViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    private lateinit var giftCardListViewModel: GiftCardListViewModel
    private val getGiftCardsUseCase = mockk<GetGiftCardsUseCase>(relaxed = true)
    private val shoppingCartUseCases = mockk<ShoppingCartUseCases>(relaxed = true)

    private val giftCards = listOf(
        GiftCardData(
            brand = "test",
            amounts = listOf(11.0, 23.1),
            discountOffTotal = listOf(19.0, 44.1),
            discount = "20%",
            imageUrl = "testImage",
            terms = "testTerms",
            vendor = "testVendor"
        ),
        GiftCardData(
            brand = "test2",
            amounts = listOf(61.0, 13.1),
            discountOffTotal = listOf(19.0, 44.1),
            discount = "70%",
            imageUrl = "testImage2",
            terms = "testTerms2",
            vendor = "testVendor2"
        )
    )
    private val cartCount = 1

    @Before
    fun beforeTest() {
        Dispatchers.setMain(dispatcher)

        giftCardListViewModel = GiftCardListViewModel(
            getGiftCardsUseCase,
            shoppingCartUseCases
        )

        coEvery { shoppingCartUseCases.getCartCount() } returns flowOf(cartCount)
    }

    @After
    fun afterTest() {
        Dispatchers.resetMain()
    }

    @Test
    fun `The gift cards screen is initialised`() = runTest {
        coEvery { getGiftCardsUseCase.getGiftCards() } returns DataResult.Success(giftCards)

        val expectedGiftCards = listOf(
            GiftCardData(
                brand = "test",
                amounts = listOf(11.0, 23.1),
                discountOffTotal = listOf(19.0, 44.1),
                discount = "20%",
                imageUrl = "testImage",
                terms = "testTerms",
                vendor = "testVendor"
            ),
            GiftCardData(
                brand = "test2",
                amounts = listOf(61.0, 13.1),
                discountOffTotal = listOf(19.0, 44.1),
                discount = "70%",
                imageUrl = "testImage2",
                terms = "testTerms2",
                vendor = "testVendor2"
            )
        )

        // Pre State
        with(giftCardListViewModel.giftCardsListState.value) {
            assertThat(isLoading).isTrue()
            assertThat(isError).isFalse()
            assertThat(isCartButtonEnabled).isFalse()
        }

        giftCardListViewModel.uiEvent.test {
            listOf(
                GiftCardListViewModel.UiEvent.SetCartCount(count = 1),
                GiftCardListViewModel.UiEvent.GiftCardsLoaded(expectedGiftCards)
            ).forEach { uiEvent ->
                val emission = awaitItem()
                assertThat(emission).isEqualTo(uiEvent)
            }
        }

        dispatcher.scheduler.advanceUntilIdle()

        // Post State
        with(giftCardListViewModel.giftCardsListState.value) {
            assertThat(isLoading).isFalse()
            assertThat(isError).isFalse()
            assertThat(isCartButtonEnabled).isTrue()
        }
    }

    @Test
    fun `A gift card is clicked`() = runTest {
        giftCardListViewModel.onEvent(GiftCardListViewModel.ClickEvent.GiftCardOnClick)

        giftCardListViewModel.uiEvent.test {
            listOf(
                GiftCardListViewModel.UiEvent.Navigate(Routes.GIFT_CARD_DETAILS.name),
                GiftCardListViewModel.UiEvent.SetCartCount(count = 1)
            ).forEach { uiEvent ->
                val emission = awaitItem()
                assertThat(emission).isEqualTo(uiEvent)
            }
        }
    }

    @Test
    fun `The cart button is clicked`() = runTest {
        giftCardListViewModel.onEvent(GiftCardListViewModel.ClickEvent.CartOnClick)

        giftCardListViewModel.uiEvent.test {
            listOf(
                GiftCardListViewModel.UiEvent.Navigate(Routes.SHOPPING_CART.name),
                GiftCardListViewModel.UiEvent.SetCartCount(count = 1)
            ).forEach { uiEvent ->
                val emission = awaitItem()
                assertThat(emission).isEqualTo(uiEvent)
            }
        }
    }

    @Test
    fun `The try button is clicked and it is successful`() = runTest {
        coEvery { getGiftCardsUseCase.getGiftCards() } returns DataResult.Success(giftCards)

        val expectedGiftCards = listOf(
            GiftCardData(
                brand = "test",
                amounts = listOf(11.0, 23.1),
                discountOffTotal = listOf(19.0, 44.1),
                discount = "20%",
                imageUrl = "testImage",
                terms = "testTerms",
                vendor = "testVendor"
            ),
            GiftCardData(
                brand = "test2",
                amounts = listOf(61.0, 13.1),
                discountOffTotal = listOf(19.0, 44.1),
                discount = "70%",
                imageUrl = "testImage2",
                terms = "testTerms2",
                vendor = "testVendor2"
            )
        )

        giftCardListViewModel.onEvent(GiftCardListViewModel.ClickEvent.TryAgainOnClick)

        // Pre State
        with(giftCardListViewModel.giftCardsListState.value) {
            assertThat(isLoading).isTrue()
            assertThat(isError).isFalse()
            assertThat(isCartButtonEnabled).isFalse()
        }

        giftCardListViewModel.uiEvent.test {
            listOf(
                GiftCardListViewModel.UiEvent.SetCartCount(count = 1),
                GiftCardListViewModel.UiEvent.GiftCardsLoaded(expectedGiftCards),
                GiftCardListViewModel.UiEvent.GiftCardsLoaded(expectedGiftCards)
            ).forEach { uiEvent ->
                val emission = awaitItem()
                assertThat(emission).isEqualTo(uiEvent)
            }
        }

        dispatcher.scheduler.advanceUntilIdle()

        // Post State
        with(giftCardListViewModel.giftCardsListState.value) {
            assertThat(isLoading).isFalse()
            assertThat(isCartButtonEnabled).isTrue()
        }
    }

    @Test
    fun `The try button is clicked and it is not successful`() = runTest {
        coEvery { getGiftCardsUseCase.getGiftCards() } returns DataResult.Error()

        // Pre State
        with(giftCardListViewModel.giftCardsListState.value) {
            assertThat(isLoading).isTrue()
            assertThat(isError).isFalse()
            assertThat(isCartButtonEnabled).isFalse()
        }

        giftCardListViewModel.onEvent(GiftCardListViewModel.ClickEvent.TryAgainOnClick)

        dispatcher.scheduler.advanceUntilIdle()

        // Pre State
        with(giftCardListViewModel.giftCardsListState.value) {
            assertThat(isLoading).isFalse()
            assertThat(isError).isTrue()
            assertThat(isCartButtonEnabled).isTrue()
        }
    }
}