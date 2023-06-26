package com.thavin.giftcardshop.ui

import java.text.DecimalFormat

private const val CURRENCY_FORMAT = "$#,##0.00"

fun Double.toCurrencyFormat(): String =
    DecimalFormat(CURRENCY_FORMAT).format(this)