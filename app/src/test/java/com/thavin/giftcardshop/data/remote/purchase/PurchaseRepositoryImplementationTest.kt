package com.thavin.giftcardshop.data.remote.purchase

import com.google.common.truth.Truth.assertThat
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.thavin.giftcardshop.data.remote.purchase.api.PurchaseApi
import com.thavin.giftcardshop.data.remote.purchase.api.PurchaseDto
import com.thavin.giftcardshop.domain.resource.DataResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

@ExperimentalCoroutinesApi
@ExperimentalSerializationApi
class PurchaseRepositoryImplementationTest {

    private val dispatcher = StandardTestDispatcher()
    private val webServer = MockWebServer()

    private val client = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.SECONDS)
        .readTimeout(1, TimeUnit.SECONDS)
        .writeTimeout(1, TimeUnit.SECONDS)
        .build()

    private val json = Json {
        isLenient = true
        ignoreUnknownKeys = true
    }

    private val api = Retrofit.Builder()
        .baseUrl(webServer.url("/"))
        .client(client)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(PurchaseApi::class.java)

    private val purchaseRepositoryImplementation = PurchaseRepositoryImplementation(api)

    @Before
    fun beforeTest() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun afterTest() {
        webServer.shutdown()
        Dispatchers.resetMain()
    }

    @Test
    fun `It sends the purchase dto`() = runTest {
        val response = MockResponse()
            .setBody(
                "  {\n" +
                        "    \"amount\": 40\n" +
                        "  }"
            )
            .setResponseCode(200)

        webServer.enqueue(response)

        val expectedResponse = DataResult.Success(
            PurchaseDto(
                amount = 40.0
            )
        )

        val actualResponse = purchaseRepositoryImplementation.purchase(
            PurchaseDto(
                amount = 40.0
            )
        )

        assertThat(actualResponse).isEqualTo(expectedResponse)
    }
}