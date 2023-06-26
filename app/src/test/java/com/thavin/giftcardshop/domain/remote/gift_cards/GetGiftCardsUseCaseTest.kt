package com.thavin.giftcardshop.domain.remote.gift_cards

import com.google.common.truth.Truth.assertThat
import com.thavin.giftcardshop.data.remote.gift_cards.api.Denominations
import com.thavin.giftcardshop.data.remote.gift_cards.api.GiftCardDto
import com.thavin.giftcardshop.domain.resource.DataResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetGiftCardsUseCaseTest {

    private val dispatcher = StandardTestDispatcher()

    private lateinit var getGiftCardsUseCase: GetGiftCardsUseCase
    private val giftCardsRepository = mockk<GiftCardsRepository>(relaxed = true)

    private val denominations = listOf(
        Denominations(11.0, "AUD", "IN_STOCK"),
        Denominations(23.1, "AUD", "IN_STOCK")
    )

    private val denominationsSecond = listOf(
        Denominations(32.0, "AUD", "IN_STOCK"),
        Denominations(63.1, "AUD", "IN_STOCK")
    )


    private val giftCardsData = listOf(
        GiftCardDto(
            brand = "test",
            id = "testId",
            denominations = denominations,
            discount = 20.0,
            image = "testImage",
            terms = "testTerms",
            vendor = "testVendor",
            position = "testPosition",
            importantContent = "testContent",
            cardTypeStatus = "testStatus",
            disclaimer = "testDisclaimer"
        ),
        GiftCardDto(
            brand = "test2",
            id = "testId2",
            denominations = denominationsSecond,
            discount = 15.0,
            image = "testImage2",
            terms = "testTerms2",
            vendor = "testVendor2",
            position = "testPosition2",
            importantContent = "testContent2",
            cardTypeStatus = "testStatus2",
            disclaimer = "testDisclaimer2"
        )
    )

    @Before
    fun beforeTest() {
        Dispatchers.setMain(dispatcher)

        getGiftCardsUseCase = GetGiftCardsUseCase(giftCardsRepository)
    }

    @After
    fun afterTest() {
        Dispatchers.resetMain()
    }

    @Test
    fun `It receives a list of gift card data`() = runTest {
        coEvery { giftCardsRepository.getGiftCards() } returns DataResult.Success(giftCardsData)

        val expectedGiftCardsData = listOf(
            GiftCardData(
                brand = "test",
                amounts = listOf(11.0, 23.1),
                discountOffTotal = listOf(2.2, 4.62),
                discount = "20.0%",
                imageUrl = "testImage",
                terms = "testTerms",
                vendor = "testVendor"
            ),
            GiftCardData(
                brand = "test2",
                amounts = listOf(32.0, 63.1),
                discountOffTotal = listOf(4.8, 9.465),
                discount = "15.0%",
                imageUrl = "testImage2",
                terms = "testTerms2",
                vendor = "testVendor2"
            )
        )

        val giftCardItems = getGiftCardsUseCase.getGiftCards()
        assertThat(giftCardItems).isEqualTo(DataResult.Success(expectedGiftCardsData))
    }
}