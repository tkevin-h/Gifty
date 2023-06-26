package com.thavin.giftcardshop.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.thavin.giftcardshop.R
import com.thavin.giftcardshop.ui.theme.GiftCardShopTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BadgedIcon(
    modifier: Modifier = Modifier,
    badgeCount: Int,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    BadgedBox(
        badge = {
            if (badgeCount > 0) {
                Badge {
                    Text(text = "$badgeCount")
                }
            }
        },
        modifier = modifier
            .clickable(
                enabled = enabled
            ) { onClick() }
    ) {
        Icon(
            imageVector = Icons.Filled.ShoppingCart,
            contentDescription = stringResource(id = R.string.badged_icon)
        )
    }
}

@Preview
@Composable
fun BadgedIconPreview() {
    GiftCardShopTheme {
        BadgedIcon(
            badgeCount = 3,
            onClick = {},
            enabled = true
        )
    }
}