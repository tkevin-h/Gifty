package com.thavin.giftcardshop.ui.gift_cards_list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
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
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.thavin.giftcardshop.R
import com.thavin.giftcardshop.domain.remote.gift_cards.GiftCardData
import com.thavin.giftcardshop.ui.components.Toolbar
import com.thavin.giftcardshop.ui.SharedViewModel
import com.thavin.giftcardshop.ui.components.BadgedIcon
import com.thavin.giftcardshop.ui.components.ErrorOverlay
import com.thavin.giftcardshop.ui.components.LoadingOverlay
import com.thavin.giftcardshop.ui.theme.*

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun GiftCardsListScreen(
    onNavigate: (String) -> Unit,
    sharedViewModel: SharedViewModel,
    giftCardListViewModel: GiftCardListViewModel = hiltViewModel()
) {
    LaunchedEffect(null) {
        giftCardListViewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                is GiftCardListViewModel.UiEvent.GiftCardsLoaded ->
                    sharedViewModel.onEvent(SharedViewModel.SharedEvent.SetGiftCards(uiEvent.giftCards))
                is GiftCardListViewModel.UiEvent.Navigate -> onNavigate(uiEvent.route)
                is GiftCardListViewModel.UiEvent.SetCartCount ->
                    sharedViewModel.onEvent(SharedViewModel.SharedEvent.SetCartCount(uiEvent.count))
            }
        }
    }

    Scaffold(
        topBar = {
            Toolbar(
                title = stringResource(id = R.string.gift_cards_title),
                navigationIcon = {},
                actionIcon = {
                    if (!giftCardListViewModel.giftCardsListState.value.isLoading) {
                        BadgedIcon(
                            badgeCount = sharedViewModel.cartCount.value,
                            onClick = { giftCardListViewModel.onEvent(GiftCardListViewModel.ClickEvent.CartOnClick) },
                            modifier = Modifier
                                .padding(end = DimenSmall)
                                .testTag("cartButton")
                        )
                    }
                }
            )
        },
        modifier = Modifier.semantics {
            testTagsAsResourceId = true
        }
    ) { paddingValues ->
        paddingValues.calculateTopPadding()

        when {
            giftCardListViewModel.giftCardsListState.value.isError -> ErrorOverlay(
                titleText = stringResource(id = R.string.gift_cards_list_loading_error_title),
                buttonText = stringResource(id = R.string.gift_cards_list_loading_error_button),
                modifier = Modifier.fillMaxSize()
            ) {
                giftCardListViewModel.onEvent(GiftCardListViewModel.ClickEvent.TryAgainOnClick)
            }
            giftCardListViewModel.giftCardsListState.value.isLoading -> LoadingOverlay(
                modifier = Modifier.fillMaxSize()
            )
            else -> GiftCardsListContent(
                giftCardData = sharedViewModel.giftCards.value,
                giftCardOnClick = { giftCardItem ->
                    sharedViewModel.onEvent(
                        SharedViewModel.SharedEvent.SetSelectedGiftCard(
                            giftCardItem
                        )
                    )
                    giftCardListViewModel.onEvent(GiftCardListViewModel.ClickEvent.GiftCardOnClick)
                },
                modifier = Modifier.padding(paddingValues)
            )
        }

    }
}

@Composable
private fun GiftCardsListContent(
    modifier: Modifier = Modifier,
    giftCardData: List<GiftCardData>,
    giftCardOnClick: (GiftCardData) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = DimenMinGridSize),
        modifier = modifier.testTag("giftCardsList")
    ) {
        items(giftCardData) { giftCard ->
            with(giftCard) {
                GiftCardsListItem(
                    brand = brand,
                    vendor = vendor,
                    discount = discount,
                    imageUrl = imageUrl,
                    giftCardOnClick = { giftCardOnClick(giftCard) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = DimenSmall,
                            top = DimenSmall,
                            end = DimenSmall
                        )
                        .testTag("giftCardItem")
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
private fun GiftCardsListItem(
    modifier: Modifier = Modifier,
    brand: String,
    vendor: String,
    discount: String,
    imageUrl: String,
    giftCardOnClick: () -> Unit
) {
    Column(modifier = modifier) {
        Box {
            Text(
                text = discount,
                color = Color.White,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .zIndex(1f)
                    .drawBehind {
                        drawCircle(
                            color = Color.Red.copy(alpha = 0.7f),
                            radius = DimenMedium.toPx()
                        )
                    }
                    .testTag("discountText")
            )
            Card(
                onClick = { giftCardOnClick() },
                elevation = CardDefaults.cardElevation(DimenMedium),
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("cardImage")
            ) {
                GlideImage(
                    model = imageUrl,
                    contentDescription = stringResource(id = R.string.card_image),
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .fillMaxSize()
                        .height(DimenListItemHeight)
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(
                    start = DimenMicro,
                    bottom = DimenSmall
                )
        ) {
            Text(
                text = brand,
                color = Color.DarkGray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .testTag("brandText")
            )

            Spacer(modifier = Modifier.width(DimenMicro))

            Text(
                text = vendor,
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .testTag("vendorText")
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun GiftCardsListContentPreview() {
    GiftCardShopTheme {
        GiftCardsListContent(giftCardData = listOf(
            GiftCardData(
                brand = "Sony",
                vendor = "Coles",
                discount = "50%",
                amounts = emptyList(),
                discountOffTotal = emptyList(),
                imageUrl = "",
                terms = ""
            ),
            GiftCardData(
                brand = "Panasonic",
                vendor = "Aldi",
                discount = "95%",
                amounts = emptyList(),
                discountOffTotal = emptyList(),
                imageUrl = "",
                terms = ""
            ),
        ),
            giftCardOnClick = {}
        )
    }
}

@Preview
@Composable
private fun GiftCardsListItemPreview() {
    GiftCardShopTheme {
        GiftCardsListItem(
            brand = "Brand",
            vendor = "Vendor",
            discount = "100%",
            imageUrl = "image.jpg",
            giftCardOnClick = {}
        )
    }
}