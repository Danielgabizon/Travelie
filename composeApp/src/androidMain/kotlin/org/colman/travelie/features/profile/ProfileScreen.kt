package org.colman.travelie.features.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import org.colman.travelie.R
import org.colman.travelie.models.Post
import org.colman.travelie.models.Posts
import org.colman.travelie.models.User
import org.colman.travelie.ui.shared_components.Error
import org.colman.travelie.ui.shared_components.Spinner
import org.colman.travelie.ui.theme.*
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel = koinViewModel()
) {
    val uiState by profileViewModel.uiState.collectAsState()
    val user by profileViewModel.user.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(4.dp))

        when (uiState) {
            is ProfileState.Loading -> {
                Spinner(modifier = Modifier.fillMaxSize())
            }
            is ProfileState.Loaded -> {
                ProfileContent(user = user)
                PostsContent(posts = (uiState as ProfileState.Loaded).posts)
            }
            is ProfileState.Error -> {
                val errorMessage = (uiState as ProfileState.Error).errorMessage
                Error(message = errorMessage, modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
fun ProfileContent(user: User?) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RectangleShape,
        modifier = Modifier
            .fillMaxWidth()
    ) {
            Row(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(24.dp),

            ) {

                if (user?.profilePicture != "" ) {
                    AsyncImage(
                        model = user?.profilePicture,
                        contentDescription = "Profile Image",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(50)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.default_avatar),
                        contentDescription = "Profile Image",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(50))
                            .weight(0.3f, fill = false)
                    )
                }


                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(0.7f),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text(
                        text = "${user?.firstName ?: "First Name"} ${user?.lastName ?: "Last Name"}",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = user?.bio ?: "No bio provided",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    )
                }
            }


    }

}


@Composable
fun PostsContent(
    posts: Posts,
    lazyGridState: LazyGridState = rememberLazyGridState()
) {
    if (posts.items.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No posts to show",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            )
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            state = lazyGridState,
            verticalArrangement = Arrangement.spacedBy(2.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier
                .fillMaxSize()

        ) {
            items(posts.items) { post ->
                PostItem(post)
            }
        }
    }
}

@Composable
fun PostItem(post: Post) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.73f)
    ) {
        AsyncImage(
            model = post.imageUrl,
            contentDescription = post.description,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}


