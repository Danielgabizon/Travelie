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
    val uiState = profileViewModel.uiState.collectAsState().value


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightGray),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {


        Spacer(modifier = Modifier.height(16.dp))

        when (uiState) {
            is ProfileState.Loading -> Spinner(
                modifier = Modifier.fillMaxSize()
            )
            is ProfileState.Error -> Error(message = uiState.errorMessage,
                modifier = Modifier.fillMaxSize()
            )
            is ProfileState.Loaded ->{
                ProfileContent(user = uiState.user)
                PostsContent(posts = uiState.userPosts)
            }

        }

    }
}
@Composable
fun ProfileContent(user: User?) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
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

                Image(
                    painter = painterResource(id = R.drawable.default_avatar),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .weight(0.3f, fill = false)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(0.7f),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text(
                        text = "${user?.firstName ?: "First Name"} ${user?.lastName ?: "Last Name"}",
                        fontSize = 20.sp,
                        color = Terracotta,
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = user?.bio ?: "No bio provided",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Navy
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
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        state = lazyGridState,
        verticalArrangement = Arrangement.spacedBy(2.dp), // vertical spacing between rows
        horizontalArrangement = Arrangement.spacedBy(2.dp), // horizontal spacing between columns
        modifier = Modifier
            .fillMaxSize()

    ) {
        items(posts.items) { post ->
            PostItem(post)
        }
    }
}

@Composable
fun PostItem(post: Post) {
    val context = LocalContext.current

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


