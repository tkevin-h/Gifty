package com.thavin.giftcardshop.ui.navigation

import com.thavin.giftcardshop.ui.navigation.Routes.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.thavin.giftcardshop.ui.SharedViewModel
import com.thavin.giftcardshop.ui.gift_card_details.GiftCardDetailsScreen
import com.thavin.giftcardshop.ui.receipt.GiftCardReceiptScreen
import com.thavin.giftcardshop.ui.gift_cards_list.GiftCardsListScreen
import com.thavin.giftcardshop.ui.shopping_cart.ShoppingCartScreen

@Composable
fun GiftCardsNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = GIFT_CARDS_LIST.name,
    sharedViewModel: SharedViewModel = hiltViewModel()
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = GIFT_CARDS_LIST.name) {
            GiftCardsListScreen(
                onNavigate = { navController.navigate(it) },
                sharedViewModel = sharedViewModel
            )
        }

        composable(route = GIFT_CARD_DETAILS.name) {
            GiftCardDetailsScreen(
                navController = navController,
                onNavigate = { navController.navigate(it) },
                sharedViewModel = sharedViewModel
            )
        }

        composable(route = GIFT_CARD_RECEIPT.name) {
            GiftCardReceiptScreen(
                onNavigate = { navController.navigate(it) {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                } },
                sharedViewModel = sharedViewModel
            )
        }

        composable(route = SHOPPING_CART.name) {
            ShoppingCartScreen(
                onNavigate = { navController.navigate(it) },
                navController = navController,
                sharedViewModel = sharedViewModel
            )
        }
    }
}