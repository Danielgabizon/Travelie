package org.colman.travelie.ui.navigation

import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation

import org.colman.travelie.features.destinations.DestinationsScreen
import org.colman.travelie.features.feed.FeedScreen
import org.colman.travelie.features.feed.FeedViewModel
import org.colman.travelie.features.profile.ProfileScreen
import org.colman.travelie.features.profile.ProfileViewModel
import org.colman.travelie.features.uploadPost.UploadPostScreen
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.mainAppNestedGraph(navController: NavController) {
    navigation(startDestination = Routes.FEED, route = Routes.MAIN_GRAPH) {
        composable(Routes.FEED) {
             FeedScreen(onAddPost = { navController.navigate(Routes.UPLOAD_POST) })
        }
        composable(Routes.UPLOAD_POST) {
            UploadPostScreen(
                onPostUploaded = {
                    navController.popBackStack()
                },
                onCancel = {
                    // go back to feed
                    navController.popBackStack()
                },
            )
        }
        composable(Routes.DESTINATIONS) {
            DestinationsScreen()
        }
        composable(Routes.PROFILE) {
             ProfileScreen()
        }
    }
}