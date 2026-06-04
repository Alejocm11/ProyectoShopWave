package com.taller.proyectofinalcomponentes.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.taller.proyectofinalcomponentes.domain.model.UserRole
import com.taller.proyectofinalcomponentes.presentation.ui.screens.CartScreen
import com.taller.proyectofinalcomponentes.presentation.ui.screens.CheckoutScreen
import com.taller.proyectofinalcomponentes.presentation.ui.screens.HomeScreen
import com.taller.proyectofinalcomponentes.presentation.ui.screens.LoginScreen
import com.taller.proyectofinalcomponentes.presentation.ui.screens.ProductDetailScreen
import com.taller.proyectofinalcomponentes.presentation.ui.screens.ProductListScreen
import com.taller.proyectofinalcomponentes.presentation.ui.screens.WishlistScreen
import com.taller.proyectofinalcomponentes.presentation.ui.screens.ProfileScreen
import com.taller.proyectofinalcomponentes.presentation.ui.screens.SellerProductScreen
import com.taller.proyectofinalcomponentes.presentation.ui.screens.PurchaseHistoryScreen
import com.taller.proyectofinalcomponentes.presentation.ui.screens.SalesHistoryScreen
import com.taller.proyectofinalcomponentes.presentation.viewmodels.AuthViewModel
import com.taller.proyectofinalcomponentes.presentation.viewmodels.CartViewModel
import com.taller.proyectofinalcomponentes.presentation.viewmodels.LanguageViewModel
import com.taller.proyectofinalcomponentes.presentation.viewmodels.ShopViewModel
import com.taller.proyectofinalcomponentes.presentation.viewmodels.OrderHistoryViewModel

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val cartViewModel: CartViewModel = viewModel()
    val shopViewModel: ShopViewModel = viewModel()
    val languageViewModel: LanguageViewModel = viewModel()
    val authViewModel: AuthViewModel = viewModel()
    val orderHistoryViewModel: OrderHistoryViewModel = viewModel()

    fun logoutToLogin() {
        authViewModel.logout()
        navController.navigate(AppRoutes.Login.route) {
            popUpTo(0)
        }
    }

    NavHost(
        navController = navController,
        startDestination = AppRoutes.Login.route
    ) {
        composable(AppRoutes.Login.route) {
            LoginScreen(
                languageViewModel = languageViewModel,
                authViewModel = authViewModel,
                onLoginSuccess = { role ->
                    val destination = if (role == UserRole.Seller) AppRoutes.Seller.route else AppRoutes.Home.route
                    navController.navigate(destination) {
                        popUpTo(AppRoutes.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(AppRoutes.Home.route) {
            HomeScreen(
                cartViewModel = cartViewModel,
                shopViewModel = shopViewModel,
                languageViewModel = languageViewModel,
                onCategoryClick = { category -> navController.navigate(AppRoutes.ProductList.createRoute(category)) },
                onCartClick = { navController.navigate(AppRoutes.Cart.route) },
                onProductClick = { productId -> navController.navigate(AppRoutes.ProductDetail.createRoute(productId)) },
                onWishlistClick = { navController.navigate(AppRoutes.Wishlist.route) },
                onProfileClick = { navController.navigate(AppRoutes.Profile.route) },
                onPurchasesClick = { navController.navigate(AppRoutes.Purchases.route) },
                onLogout = { logoutToLogin() }
            )
        }

        composable(AppRoutes.Seller.route) {
            SellerProductScreen(
                shopViewModel = shopViewModel,
                languageViewModel = languageViewModel,
                onSalesClick = { navController.navigate(AppRoutes.Sales.route) },
                onLogout = { logoutToLogin() }
            )
        }

        composable(
            route = AppRoutes.ProductList.route,
            arguments = listOf(navArgument("category") { type = NavType.StringType })
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: ""
            ProductListScreen(
                category = category,
                cartViewModel = cartViewModel,
                shopViewModel = shopViewModel,
                languageViewModel = languageViewModel,
                onBack = { navController.popBackStack() },
                onProductClick = { productId -> navController.navigate(AppRoutes.ProductDetail.createRoute(productId)) },
                onCartClick = { navController.navigate(AppRoutes.Cart.route) }
            )
        }

        composable(
            route = AppRoutes.ProductDetail.route,
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId") ?: 0
            ProductDetailScreen(
                productId = productId,
                cartViewModel = cartViewModel,
                shopViewModel = shopViewModel,
                languageViewModel = languageViewModel,
                onBack = { navController.popBackStack() },
                onGoToCart = { navController.navigate(AppRoutes.Cart.route) }
            )
        }

        composable(AppRoutes.Cart.route) {
            CartScreen(
                cartViewModel = cartViewModel,
                languageViewModel = languageViewModel,
                onBack = { navController.popBackStack() },
                onCheckout = { navController.navigate(AppRoutes.Checkout.route) }
            )
        }

        composable(AppRoutes.Checkout.route) {
            CheckoutScreen(
                cartViewModel = cartViewModel,
                languageViewModel = languageViewModel,
                authViewModel = authViewModel,
                orderHistoryViewModel = orderHistoryViewModel,
                onBack = { navController.popBackStack() },
                onFinish = {
                    navController.navigate(AppRoutes.Home.route) {
                        popUpTo(AppRoutes.Home.route) { inclusive = false }
                    }
                }
            )
        }

        composable(AppRoutes.Wishlist.route) {
            WishlistScreen(
                shopViewModel = shopViewModel,
                languageViewModel = languageViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(AppRoutes.Profile.route) {
            ProfileScreen(
                authViewModel = authViewModel,
                languageViewModel = languageViewModel,
                onBack = { navController.popBackStack() },
                onLogout = { logoutToLogin() }
            )
        }


        composable(AppRoutes.Purchases.route) {
            PurchaseHistoryScreen(
                authViewModel = authViewModel,
                languageViewModel = languageViewModel,
                orderHistoryViewModel = orderHistoryViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(AppRoutes.Sales.route) {
            SalesHistoryScreen(
                languageViewModel = languageViewModel,
                orderHistoryViewModel = orderHistoryViewModel,
                onBack = { navController.popBackStack() }
            )
        }

    }
}
