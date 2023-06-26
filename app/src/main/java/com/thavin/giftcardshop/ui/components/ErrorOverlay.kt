package com.thavin.giftcardshop.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.thavin.giftcardshop.R
import com.thavin.giftcardshop.ui.theme.DimenMicro
import com.thavin.giftcardshop.ui.theme.DimenSmall
import com.thavin.giftcardshop.ui.theme.GiftCardShopTheme

@Composable
fun ErrorOverlay(
    modifier: Modifier = Modifier,
    titleText: String,
    buttonText: String,
    buttonOnClick: () -> Unit
) {

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_error), 
                contentDescription = stringResource(id = R.string.error_icon),
                modifier = Modifier
                    .testTag("errorIcon")
            )

            Spacer(modifier = Modifier.height(DimenSmall))
            
            Text(
                text = titleText,
                modifier = Modifier
                    .testTag("titleText")
            )

            Spacer(modifier = Modifier.height(DimenMicro))

            Button(onClick = { buttonOnClick() }) {
                Text(
                    text = buttonText,
                    modifier = Modifier
                        .testTag("tryAgainButton")
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ErrorOverlayPreview() {
    GiftCardShopTheme {
        ErrorOverlay(
            titleText = "Error please try again",
            buttonText = "Try again",
            buttonOnClick = {})
    }
}