package com.thavin.giftcardshop.ui

import com.google.common.truth.Truth.assertThat
import com.thavin.giftcardshop.domain.remote.gift_cards.GiftCardData
import org.junit.Before
import org.junit.Test

class SharedViewModelTest {

    private lateinit var sharedViewModel: SharedViewModel

    private val giftCards = listOf(
        GiftCardData(
            brand = "testBrand",
            amounts = listOf(50.5, 12.2),
            discountOffTotal = listOf(19.0, 44.1),
            discount = "50%",
            imageUrl = "testImage",
            terms = "testTerms",
            vendor = "testVendor"
        ),
        GiftCardData(
            brand = "testBrand2",
            amounts = listOf(70.1, 66.2),
            discountOffTotal = listOf(19.0, 44.1),
            discount = "55%",
            imageUrl = "testImage2",
            terms = "testTerms2",
            vendor = "testVendor2"
        )
    )

    private val giftCardData = GiftCardData(
        brand = "testBrand2",
        amounts = listOf(70.1, 66.2),
        discountOffTotal = listOf(19.0, 44.1),
        discount = "55%",
        imageUrl = "testImage2",
        terms = "testTerms2",
        vendor = "testVendor2"
    )

    private val receiptAmount = "$300.40"

    private val cartCount = 1

    @Before
    fun beforeTest() {
        sharedViewModel = SharedViewModel()
    }

    @Test
    fun `Setting the gift cards list`() {
        sharedViewModel.onEvent(SharedViewModel.SharedEvent.SetGiftCards(giftCards))

        val expectedGiftCards = listOf(
            GiftCardData(
                brand = "testBrand",
                amounts = listOf(50.5, 12.2),
                discountOffTotal = listOf(19.0, 44.1),
                discount = "50%",
                imageUrl = "testImage",
                terms = "testTerms",
                vendor = "testVendor"
            ),
            GiftCardData(
                brand = "testBrand2",
                amounts = listOf(70.1, 66.2),
                discountOffTotal = listOf(19.0, 44.1),
                discount = "55%",
                imageUrl = "testImage2",
                terms = "testTerms2",
                vendor = "testVendor2"
            )
        )

        assertThat(sharedViewModel.giftCards.value).isEqualTo(expectedGiftCards)
    }

    @Test
    fun `Setting the selected gift card`() {
        sharedViewModel.onEvent(SharedViewModel.SharedEvent.SetSelectedGiftCard(giftCardData))

        val expectedGiftCardData = GiftCardData(
            brand = "testBrand2",
            amounts = listOf(70.1, 66.2),
            discountOffTotal = listOf(19.0, 44.1),
            discount = "55%",
            imageUrl = "testImage2",
            terms = "testTerms2",
            vendor = "testVendor2"
        )

        assertThat(sharedViewModel.selectedGiftCard.value).isEqualTo(expectedGiftCardData)
    }

    @Test
    fun `Setting the receipt amount`() {
        sharedViewModel.onEvent(SharedViewModel.SharedEvent.SetReceiptAmount(receiptAmount))

        val expectedReceiptAmount = "$300.40"

        assertThat(sharedViewModel.receiptAmount.value).isEqualTo(expectedReceiptAmount)
    }

    @Test
    fun `Setting the cart count`() {
        sharedViewModel.onEvent(SharedViewModel.SharedEvent.SetCartCount(cartCount))

        val expectedCartCount = 1
        assertThat(sharedViewModel.cartCount.value).isEqualTo(expectedCartCount)
    }

}