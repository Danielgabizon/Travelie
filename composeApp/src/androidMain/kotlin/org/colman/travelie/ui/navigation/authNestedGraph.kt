package org.colman.travelie.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import org.colman.travelie.MainAppTab
import org.colman.travelie.features.auth.AuthViewModel
import org.colman.travelie.features.auth.LoginScreen
import org.colman.travelie.features.auth.RegisterScreen
import org.colman.travelie.features.destinations.DestinationsScreen

fun NavGraphBuilder.authNestedGraph(authViewModel: AuthViewModel,
                                    navController: androidx.navigation.NavController) {
    navigation(startDestination = Routes.LOGIN, route = Routes.AUTH_GRAPH) {
        composable(Routes.LOGIN) {
            LoginScreen(
                viewModel = authViewModel,
                onNavigateToRegister = { navController.navigate(Routes.REGISTER) },
            )
        }
        composable(Routes.REGISTER) {
            RegisterScreen(
                viewModel = authViewModel,
                onNavigateToLogin = { navController.navigate(Routes.LOGIN) },
            )
        }

    }
}