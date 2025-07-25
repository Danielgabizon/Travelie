package org.colman.travelie.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import org.colman.travelie.MainAppTab
import org.colman.travelie.features.destinations.DestinationsScreen

fun NavGraphBuilder.mainAppNestedGraph() {
    navigation(startDestination = MainAppTab.Destinations.route, route = "main_graph") {
        composable(MainAppTab.Destinations.route) {
            DestinationsScreen()
        }
    }
}