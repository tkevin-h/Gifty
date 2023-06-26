package com.thavin.giftcardshop.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.thavin.giftcardshop.domain.remote.gift_cards.GiftCardData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {

    companion object {
        const val EMPTY_TEXT = ""
        const val EMPTY_CART = 0
    }

    var giftCards = mutableStateOf<List<GiftCardData>>(emptyList())
        private set

    var selectedGiftCard = mutableStateOf<GiftCardData?>(null)
        private set

    var receiptAmount = mutableStateOf(EMPTY_TEXT)
        private set

    var cartCount = mutableStateOf(EMPTY_CART)
        private set

    // Public Functions
    fun onEvent(sharedEvent: SharedEvent) {
        when (sharedEvent) {
            is SharedEvent.SetGiftCards -> giftCards.value = sharedEvent.giftCards
            is SharedEvent.SetSelectedGiftCard -> selectedGiftCard.value = sharedEvent.giftCardData
            is SharedEvent.SetReceiptAmount -> receiptAmount.value = sharedEvent.receiptAmount
            is SharedEvent.SetCartCount -> cartCount.value = sharedEvent.count
        }
    }

    // Events
    sealed class SharedEvent {
        data class SetGiftCards(val giftCards: List<GiftCardData>) : SharedEvent()
        data class SetSelectedGiftCard(val giftCardData: GiftCardData) : SharedEvent()
        data class SetReceiptAmount(val receiptAmount: String) : SharedEvent()
        data class SetCartCount(val count: Int) : SharedEvent()
    }
}