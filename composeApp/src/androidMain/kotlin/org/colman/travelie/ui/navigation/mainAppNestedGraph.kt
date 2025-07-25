package org.colman.travelie.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import org.colman.travelie.MainAppTab
import org.colman.travelie.features.destinations.DestinationsScreen

fun NavGraphBuilder.mainAppNestedGraph() {
    navigation(startDestination = Routes.DESTINATIONS, route = Routes.MAIN_GRAPH) {
        composable(Routes.DESTINATIONS) {
            DestinationsScreen()
        }
    }
}