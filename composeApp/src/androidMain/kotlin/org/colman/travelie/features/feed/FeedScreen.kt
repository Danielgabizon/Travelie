package org.colman.travelie.features.feed

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import org.colman.travelie.R
import org.colman.travelie.models.Post
import org.colman.travelie.models.Posts
import org.colman.travelie.ui.shared_components.Error
import org.colman.travelie.ui.shared_components.Spinner
import org.colman.travelie.ui.theme.*
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FeedScreen(
    onAddPost: () -> Unit,
    viewModel: FeedViewModel = koinViewModel(),
    onCommentsClick: (postId: String) -> Unit,
    ) {

    val uiState = viewModel.uiState.collectAsState().value

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddPost,
                shape = RoundedCornerShape(50),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "New post")
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp)
            ) {
                when (uiState) {
                    is FeedState.Loading -> Spinner(modifier = Modifier.fillMaxSize())
                    is FeedState.Error -> Error(message = uiState.errorMessage)
                    is FeedState.Loaded -> PostsContent(uiState.posts, onCommentsClick)
                }
            }
        }
    )
}
@Composable
fun PostsContent(
    posts: Posts,
    onCommentsClick: (postId: String) -> Unit
) {
    if (posts.items.isEmpty()) {
        Text(
            text = "No posts to show",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            textAlign = TextAlign.Center
        )
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            items(posts.items) { post ->
                PostItem(post, onCommentsClick)
            }
        }
    }
}@Composable
fun PostItem(post: Post, onCommentsClick: (postId: String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
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
                text = post.creatorUsername,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleSmall
            )
        }


        val hasImage = post.imageUrl.isNotBlank()
        val hasDescription = post.description.isNotBlank()

        if (hasImage) {
            Spacer(modifier = Modifier.height(12.dp))
            Box (
                modifier = Modifier
                    .fillMaxWidth()
                    .size(350.dp)
            ) {
                AsyncImage(
                    model = post.imageUrl,
                    contentDescription = post.description,
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }


        // --- Icon placement ---
        if (hasImage && hasDescription) {
            CommentButton(post, onCommentsClick)
            DescriptionText(post)
        } else if (hasImage) {
            CommentButton(post, onCommentsClick)
        } else if (hasDescription) {
            DescriptionText(post)
            CommentButton(post, onCommentsClick)
        }


    }
}

@Composable
private fun DescriptionText(post: Post) {
    Text(
        text = buildAnnotatedString {
            if (post.imageUrl.isNotBlank()) {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(post.creatorUsername)
                }
                append(": ")
            }
            append(post.description)
        },
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(
            start = 16.dp,
            top = 8.dp))
    }

@Composable
fun CommentButton(post: Post, onClick: (String) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        IconButton(onClick = { onClick(post.postId) }) {
            Icon(
                imageVector = Icons.Default.Comment,
                contentDescription = "Comments"
            )
        }
        Text(
            text = post.commentCount.toString(),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

