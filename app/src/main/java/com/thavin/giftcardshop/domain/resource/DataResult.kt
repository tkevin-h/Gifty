package com.thavin.giftcardshop.domain.resource

private const val DEFAULT_ERROR_MESSAGE = "Error getting data"

sealed class DataResult<T> {
    data class Success<T>(val data: T) : DataResult<T>()
    data class Error<T>(val data: T? = null, val message: String = DEFAULT_ERROR_MESSAGE) :
        DataResult<T>()
}