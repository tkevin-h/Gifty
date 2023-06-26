package com.thavin.giftcardshop.ui.receipt

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.thavin.giftcardshop.R
import com.thavin.giftcardshop.ui.components.Toolbar
import com.thavin.giftcardshop.ui.SharedViewModel
import com.thavin.giftcardshop.ui.theme.DimenMicro
import com.thavin.giftcardshop.ui.theme.GiftCardShopTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun GiftCardReceiptScreen(
    onNavigate: (String) -> Unit,
    sharedViewModel: SharedViewModel,
    receiptViewModel: ReceiptViewModel = hiltViewModel()
) {
    LaunchedEffect(null) {
        receiptViewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                is ReceiptViewModel.UiEvent.Navigate -> onNavigate(uiEvent.route)
            }
        }
    }

    BackHandler {
        receiptViewModel.onEvent(ReceiptViewModel.ClickEvent.CloseReceipt)
    }

    Scaffold(
        topBar = {
            Toolbar(
                title = stringResource(id = R.string.receipt_title),
                navigationIcon = {
                    IconButton(
                        onClick = { receiptViewModel.onEvent(ReceiptViewModel.ClickEvent.CloseReceipt) }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = stringResource(id = R.string.back_button),
                            modifier = Modifier.testTag("backButton")
                        )
                    }
                },
                actionIcon = {}
            )
        },
        modifier = Modifier.semantics {
            testTagsAsResourceId = true
        }
    ) { paddingValues ->
        paddingValues.calculateTopPadding()

        GiftCardReceiptContent(
            receiptAmount = sharedViewModel.receiptAmount.value,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        )
    }
}

@Composable
private fun GiftCardReceiptContent(
    modifier: Modifier = Modifier,
    receiptAmount: String
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painterResource(id = R.drawable.ic_receipt), contentDescription = stringResource(
                    id = R.string.receipt_image
                ),
                modifier = Modifier.testTag("receiptImage")
            )

            Spacer(modifier = Modifier.height(DimenMicro))

            Text(
                text = stringResource(id = R.string.purchase_complete),
                modifier = Modifier.testTag("receiptTitle")
            )

            Spacer(modifier = Modifier.height(DimenMicro))

            Text(
                text = stringResource(id = R.string.total) + receiptAmount,
                modifier = Modifier.testTag("receiptTotalText")
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun GiftCardReceiptContentPreview() {
    GiftCardShopTheme {
        GiftCardReceiptContent(
            receiptAmount = "$300.45"
        )
    }
}