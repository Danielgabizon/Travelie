package org.colman.travelie.features.feed

import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.colman.travelie.R
import org.colman.travelie.models.Post
import org.colman.travelie.models.Posts
import org.koin.androidx.compose.koinViewModel
import org.colman.travelie.ui.shared_components.Error
import org.colman.travelie.ui.shared_components.Spinner
import org.colman.travelie.ui.theme.*

@Composable
fun FeedScreen(viewModel: FeedViewModel = koinViewModel()) {
    val uiState = viewModel.uiState.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.getPosts()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightGray)
            .padding(16.dp)
    ) {

        when (uiState) {
            is FeedState.Loading -> Spinner(modifier = Modifier.fillMaxWidth())
            is FeedState.Error -> Error(message = uiState.errorMessage)
            is FeedState.Loaded -> PostsContent(uiState.posts)

            }
        }
    }

@Composable
fun PostsContent(
    posts: Posts,
) {
    if (posts.items.isEmpty()) {
        Text(
            text = "No posts available",
            style = MaterialTheme.typography.bodyMedium,
            color = Navy,
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            textAlign = TextAlign.Center
        )
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(posts.items) { post ->
                PostItem(post)
            }
        }
    }
}
@Composable
fun PostItem(post: Post) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (post.creatorImageUrl.isNotBlank()) {
                    AsyncImage(
                        model = post.creatorImageUrl,
                        contentDescription = "Profile Image",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(50)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.default_avatar),
                        contentDescription = "Default Profile Image",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(50)),
                        contentScale = ContentScale.Crop
                    )
                }

                Text(
                    text = post.creatorName,
                    color = Terracotta,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = post.description,
                style = MaterialTheme.typography.bodyMedium,
                color = Navy
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (post.imageUrl.isNotBlank()) {
                AsyncImage(
                    model = post.imageUrl,
                    contentDescription = post.description,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}
