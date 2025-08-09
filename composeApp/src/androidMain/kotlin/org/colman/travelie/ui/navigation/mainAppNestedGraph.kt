package org.colman.travelie.ui.navigation

import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import org.colman.travelie.features.comments.CommentsScreen

import org.colman.travelie.features.destinations.DestinationsScreen
import org.colman.travelie.features.feed.FeedScreen
import org.colman.travelie.features.feed.FeedViewModel
import org.colman.travelie.features.profile.ProfileScreen
import org.colman.travelie.features.profile.ProfileViewModel
import org.colman.travelie.features.uploadPost.UploadPostScreen
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

fun NavGraphBuilder.mainAppNestedGraph(navController: NavController) {
    navigation(startDestination = Routes.FEED, route = Routes.MAIN_GRAPH) {
        composable(Routes.FEED) {
             FeedScreen(
                 onAddPost = { navController.navigate(Routes.UPLOAD_POST) },
                 onCommentsClick = { postId ->
                     navController.navigate("${Routes.COMMENTS}/$postId")
                 }
             )
        }
        composable(Routes.UPLOAD_POST) {
            UploadPostScreen(
                onPostUploaded = { _ ->
                    navController.popBackStack()
                },
                onCancel = {
                    navController.popBackStack()
                },
            )
        }
        composable(
            route = Routes.COMMENTS_WITH_ARG,
            arguments = listOf(navArgument("postId") { type = NavType.StringType })
        ) { entry ->
            val postId = entry.arguments?.getString("postId") ?: return@composable
            CommentsScreen(postId = postId)
        }

        composable(Routes.DESTINATIONS) {
            DestinationsScreen()
        }
        composable(Routes.PROFILE) {
             ProfileScreen()
        }
    }
}