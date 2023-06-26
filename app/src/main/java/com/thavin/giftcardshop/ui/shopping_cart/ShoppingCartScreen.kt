package com.thavin.giftcardshop.ui.shopping_cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.thavin.giftcardshop.R
import com.thavin.giftcardshop.domain.local.shopping_cart.ShoppingCartData
import com.thavin.giftcardshop.ui.components.Toolbar
import com.thavin.giftcardshop.ui.SharedViewModel
import com.thavin.giftcardshop.ui.components.LoadingOverlay
import com.thavin.giftcardshop.ui.shopping_cart.ShoppingCartViewModel.CartEvent.*
import com.thavin.giftcardshop.ui.theme.*
import com.thavin.giftcardshop.ui.toCurrencyFormat
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ShoppingCartScreen(
    navController: NavController,
    onNavigate: (String) -> Unit,
    sharedViewModel: SharedViewModel,
    shoppingCartViewModel: ShoppingCartViewModel = hiltViewModel()
) {

    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(null) {
        shoppingCartViewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                is ShoppingCartViewModel.UiEvent.NavigateBack -> navController.popBackStack()
                is ShoppingCartViewModel.UiEvent.Navigate -> onNavigate(uiEvent.route)
                is ShoppingCartViewModel.UiEvent.ShowSnackBar -> {
                    coroutineScope.launch {
                        snackBarHostState.currentSnackbarData?.dismiss()

                        val result = snackBarHostState.showSnackbar(
                            message = uiEvent.message,
                            actionLabel = uiEvent.action,
                            withDismissAction = true
                        )

                        if (result == SnackbarResult.ActionPerformed) {
                            shoppingCartViewModel.onEvent(PurchaseCartOnClick)
                        }
                    }
                }
                is ShoppingCartViewModel.UiEvent.SetCartCount ->
                    sharedViewModel.onEvent(SharedViewModel.SharedEvent.SetCartCount(uiEvent.count))
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        topBar = {
            Toolbar(
                title = stringResource(id = R.string.shopping_cart_title),
                navigationIcon = {
                    IconButton(
                        onClick = { shoppingCartViewModel.onEvent(BackOnClick) },
                        enabled = shoppingCartViewModel.cartState.value.isBackButtonEnabled
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back_button),
                            modifier = Modifier.testTag("backButton")
                        )
                    }
                },
                actionIcon = {}
            )
        },
        floatingActionButton = {
            if (shoppingCartViewModel.cartState.value.isPurchaseButtonEnabled) {
                Button(
                    onClick = {
                        sharedViewModel.onEvent(
                            SharedViewModel.SharedEvent.SetReceiptAmount(
                                shoppingCartViewModel.cartState.value.amount.toCurrencyFormat()
                            )
                        )
                        shoppingCartViewModel.onEvent(PurchaseCartOnClick)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = DimenLarge, end = DimenLarge)
                        .zIndex(1f)
                ) {
                    Text(
                        text = stringResource(
                            id = R.string.checkout_concat
                        ) + shoppingCartViewModel.cartState.value.amount.toCurrencyFormat()
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        modifier = Modifier.semantics {
            testTagsAsResourceId = true
        }
    ) { paddingValues ->
        paddingValues.calculateTopPadding()

        when {
            shoppingCartViewModel.cartState.value.isCartLoading -> LoadingOverlay(
                modifier = Modifier.fillMaxSize()
            )
            shoppingCartViewModel.cartState.value.isEmptyCart -> EmptyCartContent()
            else -> ShoppingCartContent(
                shoppingCartData = shoppingCartViewModel.cartState.value.shoppingCartData,
                deleteOnClick = { shoppingCartItem ->
                    shoppingCartViewModel.onEvent(DeleteCartItemOnClick(shoppingCartItem))
                },
                isLoading = shoppingCartViewModel.cartState.value.isPurchaseCartLoading,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Composable
fun EmptyCartContent(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_empty_cart),
            contentDescription = stringResource(id = R.string.empty_cart),
            modifier = Modifier.testTag("emptyCartImage")
        )

        Spacer(modifier = Modifier.height(DimenSmall))

        Text(
            text = stringResource(id = R.string.empty_cart),
            modifier = Modifier.testTag("emptyCartText")
        )
    }
}

@Composable
private fun ShoppingCartContent(
    modifier: Modifier = Modifier,
    shoppingCartData: List<ShoppingCartData>,
    deleteOnClick: (ShoppingCartData) -> Unit,
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
        modifier = modifier
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("shoppingCartList")
        ) {
            items(
                items = shoppingCartData,
                key = { shoppingCartItem ->
                    shoppingCartItem.id!!
                }
            ) { cartItem ->
                with(cartItem) {
                    ShoppingCartItem(
                        brand = brand,
                        discountedAmount = discountedAmount,
                        amount = amount,
                        imageUrl = cartItem.imageUrl,
                        deleteOnClick = { deleteOnClick(cartItem) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(DimenSmall)
                            .testTag("shoppingCartItem")
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(DimenXxLarge)) }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun ShoppingCartItem(
    modifier: Modifier = Modifier,
    brand: String,
    discountedAmount: Double,
    amount: Double,
    imageUrl: String,
    deleteOnClick: () -> Unit
) {
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                contentAlignment = Alignment.TopStart,
                modifier = Modifier.padding(end = DimenExtraLarge)
            ) {
                Card(
                    modifier = Modifier
                        .height(DimenXxLarge)
                        .width(DimenCartCardWidth)
                        .padding(start = DimenSmall)
                        .testTag("cardImage")
                ) {
                    GlideImage(
                        model = imageUrl,
                        contentDescription = stringResource(id = R.string.card_image),
                        contentScale = ContentScale.FillBounds
                    )
                }

                Text(
                    text = amount.toCurrencyFormat(),
                    color = Color.White,
                    modifier = Modifier
                        .padding(top = DimenMicro, start = DimenMicro)
                        .drawBehind {
                            drawRoundRect(
                                color = Color.Red,
                                cornerRadius = CornerRadius(10f),
                                alpha = 0.8f
                            )
                        }
                        .testTag("giftCardAmount")
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = discountedAmount.toCurrencyFormat(),
                modifier = Modifier.testTag("discountedAmount")
            )

            Spacer(modifier = Modifier.width(DimenSmall))

            IconButton(onClick = { deleteOnClick() }) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = stringResource(id = R.string.delete_icon),
                    tint = Red30,
                    modifier = Modifier.testTag("deleteItemIcon")
                )
            }
        }

        Text(
            text = brand,
            color = Color.DarkGray,
            modifier = Modifier
                .padding(start = DimenMedium)
                .testTag("brand")
        )

        Divider(color = Color.LightGray)

        Spacer(modifier = Modifier.height(DimenSmall))
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CartContentPreview() {
    GiftCardShopTheme {
        ShoppingCartContent(
            shoppingCartData = listOf(
                ShoppingCartData(
                    brand = "Brand",
                    amount = 45.5,
                    discountedAmount = 5.00,
                    imageUrl = "imageurl"
                )
            ),
            deleteOnClick = {},
            isLoading = false
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun EmptyCartContentPreview() {
    GiftCardShopTheme {
        EmptyCartContent()
    }
}

@Preview
@Composable
private fun CartItemPreview() {
    GiftCardShopTheme {
        ShoppingCartData(
            brand = "test2",
            amount = 50.0,
            discountedAmount = 2.00,
            imageUrl = "imageurl"
        )
    }
}