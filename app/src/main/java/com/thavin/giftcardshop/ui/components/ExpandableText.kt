package com.thavin.giftcardshop.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.thavin.giftcardshop.R
import com.thavin.giftcardshop.ui.theme.DimenMedium
import com.thavin.giftcardshop.ui.theme.DimenXxLarge
import com.thavin.giftcardshop.ui.theme.GiftCardShopTheme

@Composable
fun ExpandableText(
    modifier: Modifier = Modifier,
    titleText: String,
    expandedText: String,
) {

    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .clickable(
                    onClick = { isExpanded = !isExpanded }
                )
        ) {
            Text(
                text = titleText,
                color = Color.DarkGray,
                modifier = Modifier
                    .testTag("titleText")
            )
            Spacer(
                modifier = Modifier
                    .weight(1f)
            )
            if (isExpanded) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowUp,
                    contentDescription = stringResource(id = R.string.arrow_up),
                    tint = Color.DarkGray,
                    modifier = Modifier
                        .testTag("arrowUpIcon")
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = stringResource(id = R.string.arrow_down),
                    tint = Color.DarkGray,
                    modifier = Modifier
                        .testTag("arrowDownIcon")
                )
            }
        }

        Divider(
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(DimenMedium))

        if (isExpanded) {
            Text(
                text = expandedText,
                color = Color.Gray,
                modifier = Modifier
                    .testTag("expandedText")
            )

            Spacer(modifier = Modifier.height(DimenXxLarge))
        }
    }
}

@Preview
@Composable
fun ExpandableTextPreview() {
    GiftCardShopTheme {
        ExpandableText(
            titleText = "Title",
            expandedText = "Expanded text"
        )
    }
}