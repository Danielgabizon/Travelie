package org.colman.travelie.features.uploadPost

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.remember
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
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import org.colman.travelie.R
import org.colman.travelie.models.Post
import org.colman.travelie.models.User
import org.colman.travelie.ui.shared_components.Error
import org.colman.travelie.ui.shared_components.Spinner
import org.colman.travelie.ui.theme.*
import org.colman.travelie.utils.loadImageData
import org.koin.androidx.compose.koinViewModel
import java.util.*
@Composable
fun UploadPostScreen(
    uploadPostViewModel: UploadPostViewModel = koinViewModel(),
    onPostUploaded: (Post) -> Unit,
    onCancel: () -> Unit
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val uiState by uploadPostViewModel.uiState.collectAsState()
    val user by uploadPostViewModel.user.collectAsState()

    var description by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    when (uiState) {
        is UploadPostState.Idle,
        is UploadPostState.Error -> {
            UploadPostForm(
                user = user,
                description = description,
                onDescriptionChange = { description = it },
                selectedImageUri = selectedImageUri,
                onImagePick = { imagePickerLauncher.launch("image/*") },
                onUploadClick = {
                    val (imageBytes, mimeType) = loadImageData(context, selectedImageUri)
                    uploadPostViewModel.uploadPost(
                        description = description,
                        postImageBytes = imageBytes,
                        postImageContentType = mimeType
                    )
                },
                onCancel = onCancel,
                errorMessage =(uiState as? UploadPostState.Error)?.errorMessage ,
                scrollState = scrollState

            )
        }
        is UploadPostState.Loading -> Spinner(modifier = Modifier.fillMaxSize())
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
    description: String,
    onDescriptionChange: (String) -> Unit,
    selectedImageUri: Uri?,
    onImagePick: () -> Unit,
    onUploadClick: () -> Unit,
    onCancel: () -> Unit,
    errorMessage: String?,
    scrollState: ScrollState
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
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
                Text(
                    text = "New Post",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(16.dp))

                // User Info
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (!user?.profilePicture.isNullOrBlank()) {
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
                            contentDescription = "Default Avatar",
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = user?.username ?: "",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Description Field
                OutlinedTextField(
                    value = description,
                    onValueChange = onDescriptionChange,
                    placeholder = { Text("What's on your mind?") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = postTextFieldColors()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Image Preview / Picker
                UploadImagePicker(selectedImageUri, onImagePick)

                Spacer(modifier = Modifier.height(24.dp))

                // Error Message
                errorMessage?.let {
                    Error(it, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Action Buttons
                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = onCancel,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Text("Cancel", style = MaterialTheme.typography.labelLarge)
                    }

                    Button(
                        onClick = onUploadClick,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp),
                        enabled = description.isNotBlank() || selectedImageUri != null,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Text("Upload", style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
        }
    }
}

@Composable
private fun UploadImagePicker(
    selectedImageUri: Uri?,
    onImagePick: () -> Unit
) {
    Box(
        modifier = Modifier
            .then(
                if (selectedImageUri != null) {
                    Modifier
                        .fillMaxWidth()
                        .size(350.dp)
                } else {
                    Modifier.wrapContentSize()
                }
            )
            .clip(MaterialTheme.shapes.medium)
            .clickable { onImagePick() },
        contentAlignment = Alignment.Center
    ) {
        if (selectedImageUri != null) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(selectedImageUri)
                        .crossfade(true)
                        .build()
                ),
                contentDescription = "Selected Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
            )
        } else {
            Text(
                text = "Pick an Image",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
            )
        }
    }
}


@Composable
private fun postTextFieldColors(): TextFieldColors = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = MaterialTheme.colorScheme.secondary,
    unfocusedBorderColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f),
    cursorColor = MaterialTheme.colorScheme.secondary
)