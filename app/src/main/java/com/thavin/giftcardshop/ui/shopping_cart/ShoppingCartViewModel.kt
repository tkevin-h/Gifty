package com.thavin.giftcardshop.ui.shopping_cart

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thavin.giftcardshop.domain.local.shopping_cart.ShoppingCartData
import com.thavin.giftcardshop.domain.local.shopping_cart.use_case.ShoppingCartUseCases
import com.thavin.giftcardshop.domain.remote.purchase.PurchaseData
import com.thavin.giftcardshop.domain.remote.purchase.PurchaseUseCase
import com.thavin.giftcardshop.domain.resource.DataResult
import com.thavin.giftcardshop.ui.shopping_cart.ShoppingCartViewModel.CartEvent.*
import com.thavin.giftcardshop.ui.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingCartViewModel @Inject constructor(
    private val shoppingCartUseCases: ShoppingCartUseCases,
    private val purchaseUseCase: PurchaseUseCase
) : ViewModel() {

    private companion object {
        const val ERROR_TITLE = "Purchase failed"
        const val ERROR_ACTION = "Try again"
        const val CARD_REMOVED = "Gift card removed"
    }

    var cartState = mutableStateOf(ShoppingCartState())
        private set

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        cartState.value = cartState.value.copy(isCartLoading = true)
        getCart()
    }

    // Public Functions
    fun onEvent(cartEvent: CartEvent) =
        when (cartEvent) {
            is PurchaseCartOnClick -> {
                cartState.value = cartState.value.copy(
                    isPurchaseCartLoading = true,
                    isPurchaseButtonEnabled = false,
                    isBackButtonEnabled = false
                )

                purchaseCart()
            }
            is DeleteCartItemOnClick -> deleteGiftCard(cartEvent.shoppingCartData)
            is BackOnClick -> sendUiEvent(UiEvent.NavigateBack)
        }

    // Private Functions
    private fun getCart() =
        viewModelScope.launch {
            shoppingCartUseCases.getShoppingCart().collect { cartItems ->
                cartState.value = cartState.value.copy(
                    shoppingCartData = cartItems,
                    isCartLoading = false,
                    isEmptyCart = cartItems.isEmpty(),
                    isPurchaseButtonEnabled = cartItems.isNotEmpty()
                )

                calculateCartAmount()
            }
        }

    private fun purchaseCart() =
        viewModelScope.launch {
            when (purchaseUseCase.purchase(PurchaseData(amount = cartState.value.amount))) {
                is DataResult.Success -> {
                    deleteCart()
                    getCartCount()
                    sendUiEvent(UiEvent.Navigate(Routes.GIFT_CARD_RECEIPT.name))
                }
                is DataResult.Error -> {
                    sendUiEvent(
                        UiEvent.ShowSnackBar(
                            message = ERROR_TITLE,
                            action = ERROR_ACTION
                        )
                    )

                    cartState.value = cartState.value.copy(
                        isPurchaseCartLoading = false,
                        isPurchaseButtonEnabled = true,
                        isBackButtonEnabled = true
                    )
                }
            }
        }

    private fun deleteCart() =
        viewModelScope.launch {
            shoppingCartUseCases.deleteCart(cartState.value.shoppingCartData)
            getCartCount()
        }

    private fun deleteGiftCard(shoppingCartData: ShoppingCartData) =
        viewModelScope.launch {
            shoppingCartUseCases.deleteGiftCard(shoppingCartData)
            getCartCount()
            sendUiEvent(UiEvent.ShowSnackBar(message = CARD_REMOVED))
        }


    private fun calculateCartAmount() {
        cartState.value = cartState.value.copy(
            amount = cartState.value.shoppingCartData.sumOf { shoppingCartItem ->
                (shoppingCartItem.discountedAmount)
            }
        )
    }

    private suspend fun getCartCount() =
        sendUiEvent(UiEvent.SetCartCount(shoppingCartUseCases.getCartCount().first()))

    private fun sendUiEvent(uiEvent: UiEvent) =
        viewModelScope.launch {
            _uiEvent.emit(uiEvent)
        }

    // Event
    sealed class CartEvent {
        object BackOnClick : CartEvent()
        object PurchaseCartOnClick : CartEvent()
        data class DeleteCartItemOnClick(val shoppingCartData: ShoppingCartData) : CartEvent()
    }

    sealed class UiEvent {
        object NavigateBack : UiEvent()
        data class Navigate(val route: String) : UiEvent()
        data class ShowSnackBar(val message: String, val action: String? = null) : UiEvent()
        data class SetCartCount(val count: Int) : UiEvent()
    }
}