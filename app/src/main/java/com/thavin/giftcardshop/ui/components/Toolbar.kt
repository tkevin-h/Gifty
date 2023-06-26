package com.thavin.giftcardshop.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.thavin.giftcardshop.ui.theme.GiftCardShopTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Toolbar(
    modifier: Modifier = Modifier,
    title: String,
    navigationIcon: @Composable () -> Unit,
    actionIcon: @Composable () -> Unit,
) {

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .testTag("topBarTitle")
            )
        },
        navigationIcon = { navigationIcon() },
        actions = { actionIcon() },
        modifier = modifier
            .testTag("topAppBar")
    )
}

@Preview
@Composable
fun ToolbarPreview() {
    GiftCardShopTheme {
        Toolbar(
            title = "Gift Cards",
            navigationIcon = {
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back Button"
                    )
                }
            },
            actionIcon = {
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Filled.ShoppingCart,
                        contentDescription = "Action Button"
                    )
                }
            }
        )
    }
}