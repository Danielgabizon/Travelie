package org.colman.travelie.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import org.colman.travelie.features.auth.AuthViewModel
import org.colman.travelie.features.comment.CommentScreen
import org.colman.travelie.features.destinations.DestinationsScreen
import org.colman.travelie.features.post.PostViewModel
import org.colman.travelie.features.post.PostsScreen
import org.colman.travelie.features.profile.ProfileScreen
import org.colman.travelie.models.Comment
import org.colman.travelie.models.Post
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.mainAppNestedGraph(authViewModel: AuthViewModel,navController: NavController) {
    navigation(startDestination = Routes.DESTINATIONS, route = Routes.MAIN_GRAPH) {
        composable(Routes.DESTINATIONS) {
            DestinationsScreen()
        }
        composable(Routes.PROFILE) {
             ProfileScreen(authViewModel)
        }
//        composable(Routes.POSTS) {  // 🆕 add this
//            val postViewModel: PostViewModel = koinViewModel()
//            PostsScreen(viewModel = postViewModel)
//        }
        composable(Routes.POSTS) {
            val mockPosts = listOf(
                Post(
                    postId = "1",
                    userId = "user123",
                    userName = "Alice",
                    userProfileUrl = "https://randomuser.me/api/portraits/women/1.jpg",
                    text = "Hello from Tel Aviv!",
                    imageUrl = "https://images.unsplash.com/photo-1551524164-687a55dd1126", // Tel Aviv beach
                    timestamp = System.currentTimeMillis(),
                    likedBy = listOf("user456", "user789"),
                    commentCount = 3,
                    latitude = 32.0853,
                    longitude = 34.7818,
                    locationName = "Tel Aviv"
                ),
                Post(
                    postId = "2",
                    userId = "user456",
                    userName = "David",
                    userProfileUrl = "https://randomuser.me/api/portraits/men/2.jpg",
                    text = "Loving this view 🌅",
                    imageUrl = "https://images.unsplash.com/photo-1506744038136-46273834b3fb", // Sunset view
                    timestamp = System.currentTimeMillis(),
                    likedBy = listOf("user123"),
                    commentCount = 1,
                    locationName = "Dead Sea"
                ),
                Post(
                    postId = "3",
                    userId = "user789",
                    userName = "Sara",
                    userProfileUrl = "https://randomuser.me/api/portraits/women/3.jpg",
                    text = "Shabbat shalom from the north 🌿",
                    imageUrl = "https://images.unsplash.com/photo-1600607681768-8b3cbb29877e", // Forest nature
                    timestamp = System.currentTimeMillis(),
                    likedBy = listOf("user123", "user456", "user999"),
                    commentCount = 5,
                    locationName = "Mitzpe Hila"
                ),
                Post(
                    postId = "4",
                    userId = "user999",
                    userName = "Noam",
                    userProfileUrl = "https://randomuser.me/api/portraits/men/4.jpg",
                    text = "Quick hike in the Negev 🌵",
                    imageUrl = "https://images.unsplash.com/photo-1595684445978-6f9d39d82a0c", // Desert
                    timestamp = System.currentTimeMillis(),
                    likedBy = listOf(),
                    commentCount = 0,
                    locationName = "Makhtesh Ramon"
                ),
                Post(
                    postId = "5",
                    userId = "user321",
                    userName = "Dana",
                    userProfileUrl = "https://randomuser.me/api/portraits/women/5.jpg",
                    text = "Street style vibes 🧡",
                    imageUrl = "https://images.unsplash.com/photo-1612197524012-5de2cafa72b1", // Urban fashion
                    timestamp = System.currentTimeMillis(),
                    likedBy = listOf("user123", "user456", "user789", "user999"),
                    commentCount = 9,
                    locationName = "Florentin, Tel Aviv"
                )
            )


            PostsScreen(posts = mockPosts, navController = navController) // ✅ Create an overload with List<post>
        }
        composable("comments/{postId}") { backStackEntry ->
            val postId = backStackEntry.arguments?.getString("postId") ?: ""
            val mockComments = listOf(
                Comment("c1", postId, "daniel", "https://randomuser.me/api/portraits/men/1.jpg", "Great post!", System.currentTimeMillis()),
                Comment("c2", postId, "sara", "https://randomuser.me/api/portraits/women/2.jpg", "🔥🔥🔥", System.currentTimeMillis()),
                Comment("c3", postId, "noam", "https://randomuser.me/api/portraits/men/3.jpg", "Where is this?", System.currentTimeMillis())
            )

            CommentScreen(postId = postId, comments = mockComments)
        }

    }
}

//fun NavGraphBuilder.mainAppNestedGraph(authViewModel: AuthViewModel,
//                                       postViewModel: PostViewModel
//) {
//    navigation(startDestination = Routes.POSTS, route = Routes.MAIN_GRAPH) {
//        composable(Routes.DESTINATIONS) {
//            DestinationsScreen()
//        }
//        composable(Routes.PROFILE) {
//            ProfileScreen(authViewModel)
//        }
//        composable(Routes.POSTS) {
//            PostsScreen(postViewModel)
//        }
//    }
//}