package org.colman.travelie.features.comments

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.colman.travelie.R
import org.colman.travelie.features.feed.PostItem
import org.colman.travelie.models.Comment
import org.colman.travelie.models.Comments
import org.colman.travelie.models.Posts
import org.colman.travelie.models.User
import org.colman.travelie.ui.shared_components.Error
import org.colman.travelie.ui.shared_components.Spinner
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
@Composable


fun CommentsScreen(postId: String) {
    val commentsViewModel: CommentsViewModel =
        koinViewModel(parameters = { parametersOf(postId) })

    val uiState by commentsViewModel.uiState.collectAsState()
    val submitState by commentsViewModel.submitState.collectAsState()
    val user by commentsViewModel.user.collectAsState()

    var input by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(submitState) {
        if (submitState is SubmitCommentState.Error) {
            snackbarHostState.showSnackbar((submitState as SubmitCommentState.Error).errorMessage)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            when (uiState) {
                is CommentsState.Loading -> {
                    Spinner(modifier = Modifier.weight(1f).fillMaxSize())
                    CommentForm(
                        user = user,
                        input = input,
                        isSubmitting = submitState is SubmitCommentState.Submitting,
                        onInputChange = { input = it },
                        onUploadClick = {
                            commentsViewModel.addComment(input.trim())
                            input = ""
                        }
                    )
                }

                is CommentsState.Error -> {
                    Error(message = (uiState as CommentsState.Error).errorMessage)
                }

                is CommentsState.Loaded -> {
                    val comments = (uiState as CommentsState.Loaded).comments

                    CommentsContent(comments,modifier = Modifier.weight(1f))

                    Spacer(modifier = Modifier.height(12.dp))

                    CommentForm(
                        user = user,
                        input = input,
                        isSubmitting = submitState is SubmitCommentState.Submitting,
                        onInputChange = { input = it },
                        onUploadClick = {
                            commentsViewModel.addComment(input.trim())
                            input = ""
                        }
                    )

                }
            }
        }
    }
}

@Composable
fun CommentsContent(
    comments: Comments,
    modifier: Modifier = Modifier
) {
    Column (
        modifier = modifier
            .fillMaxSize()
    ) {
        if (comments.items.isEmpty()) {
            Text(
                text = "No comments to show",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                textAlign = TextAlign.Center
            )
        } else {
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(comments.items) { comment ->
                    CommentItem(comment)
                }
            }
        }
    }
}

@Composable
private fun CommentItem(comment: Comment) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        if (comment.userImageUrl.isNotBlank()) {
            AsyncImage(
                model = comment.userImageUrl,
                contentDescription = "User Image",
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(50)),
                contentScale = ContentScale.Crop

            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.default_avatar),
                contentDescription = "Default Avatar",
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(50)),
                contentScale = ContentScale.Crop

            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = comment.username,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = comment.content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
private fun CommentForm(
    user: User?,
    input: String,
    isSubmitting: Boolean,
    onInputChange: (String) -> Unit,
    onUploadClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        if (!user?.profilePicture.isNullOrBlank()) {
            AsyncImage(
                model = user?.profilePicture,
                contentDescription = "User Image",
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(50)),
                contentScale = ContentScale.Crop

            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.default_avatar),
                contentDescription = "Default Avatar",
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(50)),
                contentScale = ContentScale.Crop

            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        OutlinedTextField(
            value = input,
            onValueChange = onInputChange,
            placeholder = { Text("Add a comment...") },
            modifier = Modifier
                .weight(1f)
                .heightIn(min = 40.dp),
            shape = MaterialTheme.shapes.medium,
            colors = postTextFieldColors(),
            maxLines = 4,
            textStyle = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.width(4.dp))

        if (isSubmitting) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                strokeWidth = 2.dp,
                color = MaterialTheme.colorScheme.secondary
            )
        } else {
            IconButton(
                onClick = onUploadClick,
                enabled = input.isNotBlank()
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

@Composable
private fun postTextFieldColors(): TextFieldColors = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = MaterialTheme.colorScheme.secondary,
    unfocusedBorderColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f),
    cursorColor = MaterialTheme.colorScheme.secondary
)
