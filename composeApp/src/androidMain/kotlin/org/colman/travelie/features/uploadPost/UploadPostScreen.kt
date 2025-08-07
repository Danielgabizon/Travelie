package org.colman.travelie.features.uploadPost

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import org.colman.travelie.R
import org.colman.travelie.models.Post
import org.colman.travelie.models.User
import org.colman.travelie.ui.shared_components.Error
import org.colman.travelie.ui.shared_components.Spinner
import org.colman.travelie.ui.theme.*
import org.koin.androidx.compose.koinViewModel
import java.util.*
@Composable
fun UploadPostScreen(
    uploadPostViewModel: UploadPostViewModel = koinViewModel(),
    onPostUploaded: (Post) -> Unit,
    onCancel: () -> Unit
) {
    val uiState by uploadPostViewModel.uiState.collectAsState()
    val user by uploadPostViewModel.user.collectAsState()

    when (uiState) {
        is UploadPostState.Idle,
        is UploadPostState.Error -> {
            val errorMessage = (uiState as? UploadPostState.Error)?.errorMessage
            UploadPostForm(
                user = user,
                onUpload = { description ->
                    uploadPostViewModel.uploadPost(
                        description = description,
                        imageUrl = "https://picsum.photos/200/300?random=${UUID.randomUUID()}"
                    )
                },
                onCancel = onCancel,
                errorMessage = errorMessage
            )
        }

        is UploadPostState.Loading -> {
            Spinner(modifier = Modifier.fillMaxSize())
        }

        is UploadPostState.Loaded -> {
            LaunchedEffect(Unit) {
                onPostUploaded((uiState as UploadPostState.Loaded).post)
            }
        }


    }
}

@Composable
private fun UploadPostForm(
    user: User?,
    onUpload: (String) -> Unit,
    onCancel: () -> Unit,
    errorMessage: String? = null
) {
    var description by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightGray)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            shape = MaterialTheme.shapes.large,
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Upload Post",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
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
                                .size(40.dp)
                                .clip(RoundedCornerShape(50))
                                .weight(0.3f, fill = false)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = user?.username ?: "",
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 20.sp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = { Text("What's on your mind?") },
                    modifier = Modifier
                        .fillMaxWidth().height(120.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.secondary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f),
                        cursorColor = MaterialTheme.colorScheme.secondary
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                errorMessage?.let {
                    Error(it, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = onCancel,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        shape = MaterialTheme.shapes.medium,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary,
                            disabledContainerColor = MaterialTheme.colorScheme.tertiary,
                            disabledContentColor = MaterialTheme.colorScheme.onTertiary.copy(alpha = 0.3f)
                        )
                    ) {
                        Text("Cancel", color = MaterialTheme.colorScheme.onSecondary,style = MaterialTheme.typography.labelLarge)
                    }

                    Button(
                        onClick = { onUpload(description) },
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp),
                        enabled = description.isNotBlank(),
                        shape = MaterialTheme.shapes.medium,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary,
                            disabledContainerColor = MaterialTheme.colorScheme.tertiary,
                            disabledContentColor = MaterialTheme.colorScheme.onTertiary.copy(alpha = 0.3f)
                        )
                    ) {
                        Text("Upload", color = MaterialTheme.colorScheme.onSecondary,style = MaterialTheme.typography.labelLarge)

                    }
                }
            }
        }
    }
}
