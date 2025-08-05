package org.colman.travelie.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import org.colman.travelie.features.login.LoginScreen
import org.colman.travelie.features.register.RegisterScreen

fun NavGraphBuilder.authNestedGraph(navController: androidx.navigation.NavController) {
    navigation(startDestination = Routes.LOGIN, route = Routes.AUTH_GRAPH) {
        println("defining")

        composable(Routes.LOGIN) {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(Routes.REGISTER)
                }
            )
        }
        composable(Routes.REGISTER) {
            RegisterScreen(
                onNavigateToLogin = {
                    navController.popBackStack() }
            )
        }

    }
}