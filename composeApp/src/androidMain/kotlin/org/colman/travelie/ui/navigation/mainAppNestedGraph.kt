package org.colman.travelie.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import org.colman.travelie.features.auth.AuthViewModel
import org.colman.travelie.features.destinations.DestinationsScreen
import org.colman.travelie.features.feed.FeedScreen
import org.colman.travelie.features.profile.ProfileScreen
import org.colman.travelie.features.uploadPost.UploadPostScreen

fun NavGraphBuilder.mainAppNestedGraph(authViewModel: AuthViewModel,navController: NavController) {
    navigation(startDestination = Routes.DESTINATIONS, route = Routes.MAIN_GRAPH) {
        composable(Routes.FEED) {
             FeedScreen(onAddPost = { navController.navigate(Routes.UPLOAD_POST) })
        }
        composable(Routes.UPLOAD_POST) {
            UploadPostScreen(
                authViewModel = authViewModel,
                onPostUploaded = {
                    navController.popBackStack()
                    // refresh feed
                    navController.navigate(Routes.FEED) {
                        popUpTo(Routes.FEED) { inclusive = true }
                    }
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
             ProfileScreen(authViewModel)
        }
    }
}