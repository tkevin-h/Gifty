package com.thavin.giftcardshop.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.thavin.giftcardshop.R
import com.thavin.giftcardshop.ui.theme.DimenLarge
import com.thavin.giftcardshop.ui.theme.DimenNano
import com.thavin.giftcardshop.ui.theme.DimenXxLarge
import com.thavin.giftcardshop.ui.theme.GiftCardShopTheme

@Composable
fun LoadingOverlay(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_gift_card),
            contentDescription = stringResource(id = R.string.loading_icon),
            modifier = Modifier
                .size(DimenLarge)
                .testTag("loadingIcon")
        )
        CircularProgressIndicator(
            strokeWidth = DimenNano,
            modifier = Modifier
                .size(DimenXxLarge)
                .testTag("progressIndicator")
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun LoadingOverlayPreview() {
    GiftCardShopTheme {
        LoadingOverlay()
    }
}