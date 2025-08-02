package org.colman.travelie.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import org.colman.travelie.features.auth.AuthViewModel
import org.colman.travelie.features.destinations.DestinationsScreen
import org.colman.travelie.features.feed.FeedScreen
import org.colman.travelie.features.profile.ProfileScreen

fun NavGraphBuilder.mainAppNestedGraph(authViewModel: AuthViewModel) {
    navigation(startDestination = Routes.DESTINATIONS, route = Routes.MAIN_GRAPH) {
        composable(Routes.FEED) {
             FeedScreen()
        }
        composable(Routes.DESTINATIONS) {
            DestinationsScreen()
        }
        composable(Routes.PROFILE) {
             ProfileScreen(authViewModel)
        }
    }
}