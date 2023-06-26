package com.thavin.giftcardshop.ui.gift_card_details

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thavin.giftcardshop.domain.local.shopping_cart.toShoppingCartData
import com.thavin.giftcardshop.domain.local.shopping_cart.use_case.ShoppingCartUseCases
import com.thavin.giftcardshop.domain.remote.gift_cards.GiftCardData
import com.thavin.giftcardshop.domain.remote.purchase.PurchaseData
import com.thavin.giftcardshop.domain.remote.purchase.PurchaseUseCase
import com.thavin.giftcardshop.domain.resource.DataResult
import com.thavin.giftcardshop.ui.navigation.Routes
import com.thavin.giftcardshop.ui.toCurrencyFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GiftCardDetailsViewModel @Inject constructor(
    private val purchaseUseCase: PurchaseUseCase,
    private val shoppingCartUseCases: ShoppingCartUseCases
) : ViewModel() {

    private companion object {
        const val ERROR_TITLE = "Purchase failed"
        const val ERROR_ACTION = "Try again"
        const val CART_ADDED = "Added to cart"
    }

    var giftCardsDetailsState = mutableStateOf(GiftCardsDetailState())
        private set

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    // Public Functions
    fun onEvent(clickEvent: ClickEvent) =
        when (clickEvent) {
            is ClickEvent.AmountOnClick ->
                giftCardsDetailsState.value = giftCardsDetailsState.value.copy(
                    amount = clickEvent.amount,
                    discountedAmount = clickEvent.discountedAmount,
                    isBuyNowButtonEnabled = true,
                    isAddToCartButtonEnabled = true
                )
            is ClickEvent.CartOnClick -> sendUiEvent(UiEvent.Navigate(Routes.SHOPPING_CART.name))
            is ClickEvent.BackOnClick -> sendUiEvent(UiEvent.NavigateBack)
            is ClickEvent.BuyNowOnClick -> {
                giftCardsDetailsState.value = giftCardsDetailsState.value.copy(
                    isBuyNowButtonEnabled = false,
                    isAddToCartButtonEnabled = false,
                    isCartButtonEnabled = false,
                    isBackButtonEnabled = false,
                    isPurchaseLoading = true
                )

                purchaseGiftCards()
            }
            is ClickEvent.AddToCartOnClick -> {
                insertGiftCard(clickEvent.giftCardData)
            }
        }

    // Private Functions
    private fun purchaseGiftCards() {
        viewModelScope.launch {
            when (val result = purchaseUseCase.purchase(
                PurchaseData(amount = giftCardsDetailsState.value.discountedAmount)
            )) {
                is DataResult.Success -> {
                    giftCardsDetailsState.value =
                        giftCardsDetailsState.value.copy(isPurchaseLoading = false)

                    sendUiEvent(UiEvent.SetReceiptAmount(result.data.amount.toCurrencyFormat()))

                    sendUiEvent(UiEvent.Navigate(Routes.GIFT_CARD_RECEIPT.name))
                }
                is DataResult.Error -> {
                    giftCardsDetailsState.value =
                        giftCardsDetailsState.value.copy(
                            isBuyNowButtonEnabled = true,
                            isAddToCartButtonEnabled = true,
                            isCartButtonEnabled = true,
                            isBackButtonEnabled = true,
                            isPurchaseLoading = false
                        )

                    sendUiEvent(
                        UiEvent.ShowSnackBar(
                            message = ERROR_TITLE,
                            action = ERROR_ACTION
                        )
                    )
                }
            }
        }
    }

    private fun insertGiftCard(giftCardData: GiftCardData) {
        viewModelScope.launch {
            shoppingCartUseCases.insertGiftCard(
                giftCardData.toShoppingCartData(
                    amount = giftCardsDetailsState.value.amount,
                    discountedAmount = giftCardsDetailsState.value.discountedAmount
                )
            )
            getCartCount()
            sendUiEvent(UiEvent.ShowSnackBar(message = CART_ADDED))
        }
    }

    private suspend fun getCartCount() =
        sendUiEvent(UiEvent.SetCartCount(shoppingCartUseCases.getCartCount().first()))


    private fun sendUiEvent(uiEvent: UiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(uiEvent)
        }
    }

    // Events
    sealed class ClickEvent {
        data class AmountOnClick(val amount: Double, val discountedAmount: Double) : ClickEvent()
        object CartOnClick : ClickEvent()
        object BackOnClick : ClickEvent()
        object BuyNowOnClick : ClickEvent()
        data class AddToCartOnClick(val giftCardData: GiftCardData) : ClickEvent()
    }

    sealed class UiEvent {
        object NavigateBack : UiEvent()
        data class Navigate(val route: String) : UiEvent()
        data class SetReceiptAmount(val receiptAmount: String) : UiEvent()
        data class ShowSnackBar(val message: String, val action: String? = null) : UiEvent()
        data class SetCartCount(val count: Int) : UiEvent()
    }
}