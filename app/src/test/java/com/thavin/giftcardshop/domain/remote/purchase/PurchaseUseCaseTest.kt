package com.thavin.giftcardshop.domain.remote.purchase

import com.google.common.truth.Truth.assertThat
import com.thavin.giftcardshop.data.remote.purchase.api.PurchaseDto
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

@OptIn(ExperimentalCoroutinesApi::class)
class PurchaseUseCaseTest {

    private val dispatcher = StandardTestDispatcher()

    private lateinit var purchaseUseCase: PurchaseUseCase
    private val purchaseRepository = mockk<PurchaseRepository>(relaxed = true)

    private val purchaseDto = PurchaseDto(
        amount = 100.00
    )

    private val purchaseData = PurchaseData(
        amount = 100.00
    )

    @Before
    fun beforeTest() {
        Dispatchers.setMain(dispatcher)

        purchaseUseCase = PurchaseUseCase(purchaseRepository)
    }

    @After
    fun afterTest() {
        Dispatchers.resetMain()
    }

    @Test
    fun `It receives a purchase dto`() = runTest {
        coEvery { purchaseRepository.purchase(any()) } returns DataResult.Success(purchaseDto)

        val expectedPurchaseItem = purchaseUseCase.purchase(purchaseData)
        assertThat(expectedPurchaseItem).isEqualTo(DataResult.Success(purchaseData))
    }
}