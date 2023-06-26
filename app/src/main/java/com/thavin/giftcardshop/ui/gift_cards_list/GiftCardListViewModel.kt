package com.thavin.giftcardshop.ui.gift_cards_list

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thavin.giftcardshop.domain.local.shopping_cart.use_case.ShoppingCartUseCases
import com.thavin.giftcardshop.domain.remote.gift_cards.GetGiftCardsUseCase
import com.thavin.giftcardshop.domain.remote.gift_cards.GiftCardData
import com.thavin.giftcardshop.domain.resource.DataResult
import com.thavin.giftcardshop.ui.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GiftCardListViewModel @Inject constructor(
    private val getGiftCardsUseCase: GetGiftCardsUseCase,
    private val shoppingCartUseCases: ShoppingCartUseCases
) : ViewModel() {

    var giftCardsListState = mutableStateOf(GiftCardsListState())
        private set

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        setLoadingState()
        getCartCount()
        getGiftCardsList()
    }

    // Public Functions
    fun onEvent(clickEvent: ClickEvent) =
        when (clickEvent) {
            is ClickEvent.GiftCardOnClick -> sendUiEvent(UiEvent.Navigate(Routes.GIFT_CARD_DETAILS.name))
            is ClickEvent.CartOnClick -> sendUiEvent(UiEvent.Navigate(Routes.SHOPPING_CART.name))
            is ClickEvent.TryAgainOnClick -> {
                setLoadingState()
                getGiftCardsList()
            }
        }

    // Private Functions
    private fun getGiftCardsList() {
        viewModelScope.launch {
            when (val result = getGiftCardsUseCase.getGiftCards()) {
                is DataResult.Success -> {
                    giftCardsListState.value =
                        giftCardsListState.value.copy(
                            isLoading = false,
                            isCartButtonEnabled = true
                        )

                    sendUiEvent(UiEvent.GiftCardsLoaded(result.data))
                }
                is DataResult.Error -> giftCardsListState.value =
                    giftCardsListState.value.copy(
                        isLoading = false,
                        isError = true,
                        isCartButtonEnabled = true
                    )
            }
        }
    }

    private fun getCartCount() {
        viewModelScope.launch {
            sendUiEvent(UiEvent.SetCartCount(shoppingCartUseCases.getCartCount().first()))
        }
    }

    private fun setLoadingState() {
        giftCardsListState.value =
            giftCardsListState.value.copy(
                isLoading = true,
                isError = false,
                isCartButtonEnabled = false
            )
    }

    private fun sendUiEvent(uiEvent: UiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(uiEvent)
        }
    }

    // Events
    sealed class ClickEvent {
        object GiftCardOnClick : ClickEvent()
        object CartOnClick : ClickEvent()
        object TryAgainOnClick : ClickEvent()
    }

    sealed class UiEvent {
        data class Navigate(val route: String) : UiEvent()
        data class GiftCardsLoaded(val giftCards: List<GiftCardData>) : UiEvent()
        data class SetCartCount(val count: Int) : UiEvent()
    }
}