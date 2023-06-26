package com.thavin.giftcardshop.ui.receipt

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.thavin.giftcardshop.ui.navigation.Routes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ReceiptViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    private lateinit var receiptViewModel: ReceiptViewModel

    @Before
    fun beforeTest() {
        Dispatchers.setMain(dispatcher)

        receiptViewModel = ReceiptViewModel()
    }

    @After
    fun afterTest() {
        Dispatchers.resetMain()
    }

    @Test
    fun `The close button is clicked`() = runTest {
        receiptViewModel.onEvent(ReceiptViewModel.ClickEvent.CloseReceipt)

        receiptViewModel.uiEvent.test {
            val emission = awaitItem()
            assertThat(emission).isEqualTo(ReceiptViewModel.UiEvent.Navigate(Routes.GIFT_CARDS_LIST.name))
        }
    }
}