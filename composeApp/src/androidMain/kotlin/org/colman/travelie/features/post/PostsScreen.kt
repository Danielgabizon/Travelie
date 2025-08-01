package org.colman.travelie.features.post

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import org.colman.travelie.models.Post

import org.colman.travelie.ui.shared_components.Error
import org.colman.travelie.ui.shared_components.Spinner
import org.colman.travelie.ui.theme.*

//@Composable
//fun PostsScreen(posts: List<post>) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(LightGray)
//            .padding(16.dp)
//    ) {
//        Text(
//            text = "Posts",
//            fontSize = 24.sp,
//            color = Navy,
//            modifier = Modifier.align(Alignment.CenterHorizontally)
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        LazyColumn(modifier = Modifier.fillMaxSize()) {
//            items(posts) { post ->
//                PostCard(post)
//            }
//        }
//    }
@Composable
fun PostsScreen(posts: List<Post>,navController: NavController) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        items(posts) { singlePost ->
            PostCard(post = singlePost){
                navController.navigate("comments/${singlePost.postId}")
            }
        }
    }
}

//}


//package org.colman.travelie.features.post
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import coil.compose.AsyncImage
//import org.colman.travelie.models.post
//import org.colman.travelie.ui.shared_components.Error
//import org.colman.travelie.ui.shared_components.Spinner
//import org.colman.travelie.ui.theme.*
//
//@Composable
//fun PostsScreen(viewModel: PostViewModel) {
//    val uiState = viewModel.uiState.collectAsState().value
//    val mockPosts = listOf(
//        post(
//            postId = "1",
//            userId = "user123",
//            userName = "Alice",
//            userProfileUrl = null,
//            text = "Hello from Tel Aviv!",
//            imageUrl = null,
//            timestamp = System.currentTimeMillis(),
//            likedBy = listOf("user456", "user789"),
//            commentCount = 3,
//            latitude = 32.0853,
//            longitude = 34.7818,
//            locationName = "Tel Aviv"
//        ),
//        post(
//            postId = "2",
//            userId = "user456",
//            userName = "Bob",
//            userProfileUrl = null,
//            text = "Loving this view 🌅",
//            imageUrl = "https://images.unsplash.com/photo-123456789", // Replace with any valid image
//            timestamp = System.currentTimeMillis(),
//            likedBy = emptyList(),
//            commentCount = 0,
//            locationName = "Dead Sea"
//        )
//    )
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(LightGray)
//            .padding(16.dp)
//    ) {
//        Text(
//            text = "Posts",
//            fontSize = 24.sp,
//            color = Navy,
//            modifier = Modifier.align(Alignment.CenterHorizontally)
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        when (uiState) {
//            is PostState.Loading -> Spinner(modifier = Modifier.fillMaxWidth())
//            is PostState.Error -> Error(uiState.message, modifier = Modifier.fillMaxWidth())
//            is PostState.Loaded -> LazyColumn(modifier = Modifier.fillMaxSize()) {
//                items(uiState.posts) { post ->
//                    PostCard(post)
//                }
//            }
////            else -> {}
//            else -> LazyColumn(modifier = Modifier.fillMaxSize()) {
//                items(mockPosts) { post ->
//                    PostCard(post)
//                }
//            }
//        }
//    }
//}
