package com.thavin.giftcardshop.ui.receipt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thavin.giftcardshop.ui.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReceiptViewModel @Inject constructor() : ViewModel() {

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    // Public Functions
    fun onEvent(clickEvent: ClickEvent) {
        when (clickEvent) {
            is ClickEvent.CloseReceipt -> sendUiEvent(UiEvent.Navigate(Routes.GIFT_CARDS_LIST.name))
        }
    }

    // Private Functions
    private fun sendUiEvent(uiEvent: UiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(uiEvent)
        }
    }

    // Events
    sealed class ClickEvent {
        object CloseReceipt : ClickEvent()
    }

    sealed class UiEvent {
        data class Navigate(val route: String) : UiEvent()
    }
}