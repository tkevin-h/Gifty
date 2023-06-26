package com.thavin.giftcardshop.ui.gift_card_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.thavin.giftcardshop.R
import com.thavin.giftcardshop.domain.remote.gift_cards.GiftCardData
import com.thavin.giftcardshop.ui.components.Toolbar
import com.thavin.giftcardshop.ui.SharedViewModel
import com.thavin.giftcardshop.ui.components.BadgedIcon
import com.thavin.giftcardshop.ui.components.ExpandableText
import com.thavin.giftcardshop.ui.components.LoadingOverlay
import com.thavin.giftcardshop.ui.theme.*
import com.thavin.giftcardshop.ui.toCurrencyFormat
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun GiftCardDetailsScreen(
    navController: NavController,
    onNavigate: (String) -> Unit,
    sharedViewModel: SharedViewModel,
    giftCardDetailsViewModel: GiftCardDetailsViewModel = hiltViewModel()
) {

    val state by giftCardDetailsViewModel.giftCardsDetailsState
    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(null) {
        giftCardDetailsViewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                is GiftCardDetailsViewModel.UiEvent.Navigate -> onNavigate(uiEvent.route)
                is GiftCardDetailsViewModel.UiEvent.NavigateBack -> navController.popBackStack()
                is GiftCardDetailsViewModel.UiEvent.SetReceiptAmount ->
                    sharedViewModel.onEvent(SharedViewModel.SharedEvent.SetReceiptAmount(uiEvent.receiptAmount))
                is GiftCardDetailsViewModel.UiEvent.ShowSnackBar -> {
                    coroutineScope.launch {
                        snackBarHostState.currentSnackbarData?.dismiss()

                        val result = snackBarHostState.showSnackbar(
                            message = uiEvent.message,
                            actionLabel = uiEvent.action,
                            withDismissAction = true
                        )

                        if (result == SnackbarResult.ActionPerformed) {
                            giftCardDetailsViewModel.onEvent(GiftCardDetailsViewModel.ClickEvent.BuyNowOnClick)
                        }
                    }
                }
                is GiftCardDetailsViewModel.UiEvent.SetCartCount ->
                    sharedViewModel.onEvent(SharedViewModel.SharedEvent.SetCartCount(uiEvent.count))
            }
        }
    }

    sharedViewModel.selectedGiftCard.value?.let { giftCardItem ->
        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackBarHostState)
            },
            topBar = {
                Toolbar(
                    title = giftCardItem.brand,
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                giftCardDetailsViewModel.onEvent(
                                    GiftCardDetailsViewModel.ClickEvent.BackOnClick
                                )
                            },
                            enabled = state.isBackButtonEnabled,
                            modifier = Modifier
                                .testTag("backButton")
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = stringResource(id = R.string.back_button)
                            )
                        }
                    },
                    actionIcon = {
                        BadgedIcon(
                            badgeCount = sharedViewModel.cartCount.value,
                            onClick = { giftCardDetailsViewModel.onEvent(GiftCardDetailsViewModel.ClickEvent.CartOnClick) },
                            enabled = giftCardDetailsViewModel.giftCardsDetailsState.value.isCartButtonEnabled,
                            modifier = Modifier
                                .padding(end = DimenSmall)
                                .testTag("cartButton")
                        )
                    }
                )
            },
            floatingActionButton = {
                if (giftCardDetailsViewModel.giftCardsDetailsState.value.isBuyNowButtonEnabled) {
                    Button(
                        onClick = {
                            giftCardDetailsViewModel.onEvent(
                                GiftCardDetailsViewModel.ClickEvent.AddToCartOnClick(
                                    giftCardItem
                                )
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = DimenLarge, end = DimenLarge)
                    ) {
                        Text(text = stringResource(id = R.string.add_to_cart_button))
                    }
                }
            },
            floatingActionButtonPosition = FabPosition.Center,
            modifier = Modifier.semantics {
                testTagsAsResourceId = true
            }
        ) { paddingValues ->
            paddingValues.calculateTopPadding()

            GiftCardDetailsContent(
                giftCardData = giftCardItem,
                amount = giftCardDetailsViewModel.giftCardsDetailsState.value.amount,
                discountedAmount = giftCardDetailsViewModel.giftCardsDetailsState.value.discountedAmount,
                amountOnClick = { amount, index ->
                    giftCardDetailsViewModel.onEvent(
                        GiftCardDetailsViewModel.ClickEvent.AmountOnClick(
                            amount, giftCardItem.discountOffTotal[index]
                        )
                    )
                },
                buyNowOnClick = { giftCardDetailsViewModel.onEvent(GiftCardDetailsViewModel.ClickEvent.BuyNowOnClick) },
                buyNowButtonEnabled = giftCardDetailsViewModel.giftCardsDetailsState.value.isBuyNowButtonEnabled,
                isLoading = giftCardDetailsViewModel.giftCardsDetailsState.value.isPurchaseLoading,
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            )
        }
    }
}

@Composable
private fun GiftCardDetailsContent(
    modifier: Modifier = Modifier,
    giftCardData: GiftCardData,
    amount: Double,
    discountedAmount: Double,
    amountOnClick: (Double, Int) -> Unit,
    buyNowOnClick: () -> Unit,
    buyNowButtonEnabled: Boolean,
    isLoading: Boolean,
) {
    if (isLoading) {
        LoadingOverlay(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
                .zIndex(1f)
                .testTag("loadingOverlay")
        )
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        GiftCardDetailsCard(
            imageUrl = giftCardData.imageUrl,
            modifier = Modifier
                .fillMaxWidth()
                .height(DimenDetailsCardHeight)
        )

        Spacer(modifier = Modifier.height(DimenMicro))

        Row(
            modifier = Modifier.align(Alignment.Start)
        ) {
            Spacer(modifier = Modifier.width(DimenMicro))

            Text(
                text = stringResource(id = R.string.discount_concat) + giftCardData.discount,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier
                    .drawBehind {
                        drawRoundRect(
                            color = Color.LightGray,
                            cornerRadius = CornerRadius(50f)
                        )
                    }
                    .padding(DimenMicro)
                    .testTag("discountText")
            )
        }

        Spacer(modifier = Modifier.height(DimenLarge))

        GiftCardDetailsDenominationDropDownMenu(
            giftCardAmounts = giftCardData.amounts,
            amount = amount.toCurrencyFormat(),
            amountOnClick = amountOnClick
        )

        Spacer(modifier = Modifier.height(DimenExtraLarge))

        Button(
            onClick = { buyNowOnClick() },
            enabled = buyNowButtonEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = DimenLarge, end = DimenLarge)
                .testTag("buyNowButton")
        ) {
            Text(
                text = stringResource(
                    id = R.string.buy_now_concat
                ) + discountedAmount.toCurrencyFormat()
            )
        }

        Spacer(modifier = Modifier.height(DimenExtraLarge))

        ExpandableText(
            titleText = stringResource(id = R.string.terms_and_conditions),
            expandedText = giftCardData.terms,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = DimenLarge, end = DimenLarge)
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun GiftCardDetailsCard(
    modifier: Modifier = Modifier,
    imageUrl: String
) {
    Card(
        shape = RoundedCornerShape(DimenMedium),
        elevation = CardDefaults.cardElevation(DimenMicro),
        modifier = modifier.testTag("giftCardCard")
    ) {
        GlideImage(
            model = imageUrl,
            contentDescription = stringResource(id = R.string.card_image),
            contentScale = ContentScale.FillBounds
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GiftCardDetailsDenominationDropDownMenu(
    giftCardAmounts: List<Double>,
    amount: String,
    amountOnClick: (Double, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var dropDownMenuExpanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = modifier.testTag("amountDropDownMenu"),
        expanded = dropDownMenuExpanded,
        onExpandedChange = { dropDownMenuExpanded = !dropDownMenuExpanded },
    ) {
        OutlinedTextField(
            value = amount,
            onValueChange = {},
            readOnly = true,
            label = {
                Text(text = stringResource(id = R.string.gift_card_amount_label))
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(

            ),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropDownMenuExpanded)
            },
            modifier = Modifier
                .testTag("amountTextField")
                .menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = dropDownMenuExpanded,
            onDismissRequest = { dropDownMenuExpanded = false },
            modifier = Modifier
                .height(DimenDropDownMenuHeight)
                .background(color = Color.White)
        ) {
            giftCardAmounts.forEachIndexed { index, amount ->
                DropdownMenuItem(
                    text = { Text(text = amount.toCurrencyFormat()) },
                    onClick = {
                        amountOnClick(amount, index)
                        dropDownMenuExpanded = false
                    },
                    modifier = Modifier.testTag("dropDownMenuItem")
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun GiftCardDetailsContentPreview() {
    GiftCardShopTheme {
        GiftCardDetailsContent(
            giftCardData = GiftCardData(
                brand = "Brand",
                vendor = "Vendor",
                discount = "100%",
                amounts = listOf(15.00, 90.00, 45.00),
                discountOffTotal = listOf(10.00, 9.00, 11.00),
                imageUrl = "",
                terms = ""
            ),
            amount = 500.00,
            discountedAmount = 5.00,
            amountOnClick = { _, _ -> },
            buyNowOnClick = {},
            buyNowButtonEnabled = true,
            isLoading = false
        )
    }
}

@Preview
@Composable
private fun GiftCardDetailsDenominationDropDownMenuPreview() {
    GiftCardShopTheme {
        GiftCardDetailsDenominationDropDownMenu(
            giftCardAmounts = listOf(199.00, 45.0),
            amount = "$500.00",
            amountOnClick = { _, _ -> }
        )
    }
}