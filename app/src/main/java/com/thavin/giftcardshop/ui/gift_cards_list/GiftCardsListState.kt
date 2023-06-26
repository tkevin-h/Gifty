package com.thavin.giftcardshop.ui.gift_cards_list

data class GiftCardsListState(
    var isLoading: Boolean = false,
    var isError: Boolean = false,
    var isCartButtonEnabled: Boolean = true
)