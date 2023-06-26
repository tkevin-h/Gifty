package com.thavin.giftcardshop.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.SideEffect
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.thavin.giftcardshop.ui.navigation.GiftCardsNavHost
import com.thavin.giftcardshop.ui.theme.GiftCardShopTheme
import com.thavin.giftcardshop.ui.theme.White99
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GiftCardShopTheme {
                val systemUiController = rememberSystemUiController()
                SideEffect {
                    systemUiController.setStatusBarColor(White99)
                }

                GiftCardsNavHost()
            }
        }
    }
}