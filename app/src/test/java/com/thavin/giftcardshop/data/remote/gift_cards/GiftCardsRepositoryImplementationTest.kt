package com.thavin.giftcardshop.data.remote.gift_cards

import com.google.common.truth.Truth.assertThat
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.thavin.giftcardshop.data.remote.gift_cards.api.Denominations
import com.thavin.giftcardshop.data.remote.gift_cards.api.GiftCardDto
import com.thavin.giftcardshop.data.remote.gift_cards.api.GiftCardsApi
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
class GiftCardsRepositoryImplementationTest {

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
        .create(GiftCardsApi::class.java)

    private val giftCardsRepositoryImplementation = GiftCardsRepositoryImplementation(api)

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
    fun `Getting gift cards returns successfully`() = runTest {
        val response = MockResponse()
            .setBody(
                "[  {\n" +
                        "    \"vendor\": \"Prezzee\",\n" +
                        "    \"id\": \"KMART\",\n" +
                        "    \"brand\": \"Kmart\",\n" +
                        "    \"image\": \"https://files.prezzee.com.au/media/sku-theme-designs/your-kmart-gift-card-sept-2020-58f3936c-e807-4a8a-ba6b-b75f6c136d2c/your-kmart-gift-card-sept-2020.jpg\",\n" +
                        "    \"denominations\": [\n" +
                        "      {\n" +
                        "        \"price\": 5,\n" +
                        "        \"currency\": \"AUD\",\n" +
                        "        \"stock\": \"IN_STOCK\"\n" +
                        "      },\n" +
                        "      {\n" +
                        "        \"price\": 10,\n" +
                        "        \"currency\": \"AUD\",\n" +
                        "        \"stock\": \"IN_STOCK\"\n" +
                        "      },\n" +
                        "      {\n" +
                        "        \"price\": 15,\n" +
                        "        \"currency\": \"AUD\",\n" +
                        "        \"stock\": \"IN_STOCK\"\n" +
                        "      },\n" +
                        "      {\n" +
                        "        \"price\": 20,\n" +
                        "        \"currency\": \"AUD\",\n" +
                        "        \"stock\": \"IN_STOCK\"\n" +
                        "      },\n" +
                        "      {\n" +
                        "        \"price\": 25,\n" +
                        "        \"currency\": \"AUD\",\n" +
                        "        \"stock\": \"IN_STOCK\"\n" +
                        "      },\n" +
                        "      {\n" +
                        "        \"price\": 30,\n" +
                        "        \"currency\": \"AUD\",\n" +
                        "        \"stock\": \"IN_STOCK\"\n" +
                        "      },\n" +
                        "      {\n" +
                        "        \"price\": 50,\n" +
                        "        \"currency\": \"AUD\",\n" +
                        "        \"stock\": \"IN_STOCK\"\n" +
                        "      },\n" +
                        "      {\n" +
                        "        \"price\": 75,\n" +
                        "        \"currency\": \"AUD\",\n" +
                        "        \"stock\": \"IN_STOCK\"\n" +
                        "      },\n" +
                        "      {\n" +
                        "        \"price\": 100,\n" +
                        "        \"currency\": \"AUD\",\n" +
                        "        \"stock\": \"IN_STOCK\"\n" +
                        "      },\n" +
                        "      {\n" +
                        "        \"price\": 150,\n" +
                        "        \"currency\": \"AUD\",\n" +
                        "        \"stock\": \"IN_STOCK\"\n" +
                        "      },\n" +
                        "      {\n" +
                        "        \"price\": 200,\n" +
                        "        \"currency\": \"AUD\",\n" +
                        "        \"stock\": \"IN_STOCK\"\n" +
                        "      },\n" +
                        "      {\n" +
                        "        \"price\": 250,\n" +
                        "        \"currency\": \"AUD\",\n" +
                        "        \"stock\": \"IN_STOCK\"\n" +
                        "      },\n" +
                        "      {\n" +
                        "        \"price\": 500,\n" +
                        "        \"currency\": \"AUD\",\n" +
                        "        \"stock\": \"IN_STOCK\"\n" +
                        "      }\n" +
                        "    ],\n" +
                        "    \"position\": 10,\n" +
                        "    \"discount\": 95.5,\n" +
                        "    \"terms\": \"Key conditions of use: Treat this gift card like cash. Lost or stolen giftcards will not be replaced or refunded. To be used for the purchase of goods and services at Kmart stores excluding Kmart Tyre & Auto Service stations, Kmart New Zealand, PixiFoto and purchases initiated by phone, email, online or fax. Not redeemable for cash or payment of credit or store accounts. Gift cards cannot be used to purchase gift cards.  This card will expire 3 years from the issue date. For Full Terms of Use, expiry date or customer service visit giftcards.com.au or phone 1300 304 990.\",\n" +
                        "    \"importantContent\": \"Kmart Gift Cards are the perfect solution to shopping indecision. They make gift giving easy. With a massive product range in store, the Kmart Gift Card ensures that everyone finds the perfect gift.\",\n" +
                        "    \"cardTypeStatus\": \"AVAILABLE\",\n" +
                        "    \"customDenominations\": [],\n" +
                        "    \"disclaimer\": \"\"\n" +
                        "  }]"
            )
            .setResponseCode(200)

        webServer.enqueue(response)

        val expectedDenominations = listOf(
            Denominations(5.0, "AUD", "IN_STOCK"),
            Denominations(10.0, "AUD", "IN_STOCK"),
            Denominations(15.0, "AUD", "IN_STOCK"),
            Denominations(20.0, "AUD", "IN_STOCK"),
            Denominations(25.0, "AUD", "IN_STOCK"),
            Denominations(30.0, "AUD", "IN_STOCK"),
            Denominations(50.0, "AUD", "IN_STOCK"),
            Denominations(75.0, "AUD", "IN_STOCK"),
            Denominations(100.0, "AUD", "IN_STOCK"),
            Denominations(150.0, "AUD", "IN_STOCK"),
            Denominations(200.0, "AUD", "IN_STOCK"),
            Denominations(250.0, "AUD", "IN_STOCK"),
            Denominations(500.0, "AUD", "IN_STOCK"),
        )

        val expectedResponse = DataResult.Success(
            listOf(
                GiftCardDto(
                    brand = "Kmart",
                    id = "KMART",
                    denominations = expectedDenominations,
                    discount = 95.5,
                    position = "10",
                    importantContent = "Kmart Gift Cards are the perfect solution to shopping indecision. They make gift giving easy. With a massive product range in store, the Kmart Gift Card ensures that everyone finds the perfect gift.",
                    cardTypeStatus = "AVAILABLE",
                    disclaimer = "",
                    image = "https://files.prezzee.com.au/media/sku-theme-designs/your-kmart-gift-card-sept-2020-58f3936c-e807-4a8a-ba6b-b75f6c136d2c/your-kmart-gift-card-sept-2020.jpg",
                    terms = "Key conditions of use: Treat this gift card like cash. Lost or stolen giftcards will not be replaced or refunded. To be used for the purchase of goods and services at Kmart stores excluding Kmart Tyre & Auto Service stations, Kmart New Zealand, PixiFoto and purchases initiated by phone, email, online or fax. Not redeemable for cash or payment of credit or store accounts. Gift cards cannot be used to purchase gift cards.  This card will expire 3 years from the issue date. For Full Terms of Use, expiry date or customer service visit giftcards.com.au or phone 1300 304 990.",
                    vendor = "Prezzee"
                )
            )
        )

        val actualResponse = giftCardsRepositoryImplementation.getGiftCards()
        assertThat(actualResponse).isEqualTo(expectedResponse)
    }
}