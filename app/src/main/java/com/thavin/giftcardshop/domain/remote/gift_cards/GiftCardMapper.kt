package com.thavin.giftcardshop.domain.remote.gift_cards

import com.thavin.giftcardshop.data.remote.gift_cards.api.Denominations
import com.thavin.giftcardshop.data.remote.gift_cards.api.GiftCardDto

private const val PERCENTAGE = "%"
private const val PERCENTAGE_OFF = 100.0

fun GiftCardDto.toGiftCardData() =
    GiftCardData(
        brand = brand,
        amounts = mapDenominationsPrice(denominations),
        discountOffTotal = mapPrice(denominations, discount),
        discount = discount.toPercentageString(),
        imageUrl = image,
        terms = terms,
        vendor = vendor
    )

private fun Double.toPercentageString(): String =
    "$this$PERCENTAGE"

private fun Double.percentageOff(percent: Double): Double =
    ((percent / PERCENTAGE_OFF) * this)

private fun mapDenominationsPrice(denominations: List<Denominations>) =
    denominations
        .map { denomination ->
            denomination.price
        }

private fun mapPrice(amounts: List<Denominations>, discount: Double): List<Double> =
    amounts.map { amount ->
        amount.price.percentageOff(discount)
    }
