package org.colman.travelie.features.uploadPost

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import org.colman.travelie.models.Post
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
    val uiState = uploadPostViewModel.uiState.collectAsState().value

    when (uiState) {

        // 🌀 Full-screen spinner when loading or uploading
        is UploadPostState.LoadingUser,
        is UploadPostState.UploadingPost -> {
            Spinner(modifier = Modifier.fillMaxSize())
        }

        // ✅ Post uploaded successfully
        is UploadPostState.PostUploaded -> {
            LaunchedEffect(uiState.post) {
                onPostUploaded(uiState.post)
            }
        }

        // ❌ Error state
        is UploadPostState.Error -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(LightGray)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Error(message = uiState.errorMessage, modifier = Modifier.fillMaxWidth())
            }
        }

        // 📝 Show the form
        is UploadPostState.UserLoaded -> {
            val user = uiState.user
            var description by remember { mutableStateOf("") }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(LightGray)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Upload Post",
                            fontSize = 24.sp,
                            color = Navy,
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            AsyncImage(
                                model = user.profilePicture,
                                contentDescription = "Profile Image",
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(50))
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "${user.firstName} ${user.lastName}",
                                fontSize = 18.sp,
                                color = Navy
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            placeholder = { Text("What's on your mind?") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Terracotta,
                                unfocusedBorderColor = Lavender,
                                cursorColor = Terracotta
                            )
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(modifier = Modifier.fillMaxWidth()) {
                            Button(
                                onClick = onCancel,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 8.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Beige,
                                    contentColor = Navy
                                )
                            ) {
                                Text("Cancel")
                            }

                            Button(
                                onClick = {
                                    uploadPostViewModel.uploadPost(
                                        description = description,
                                        imageUrl = "https://picsum.photos/200/300?random=${UUID.randomUUID()}"
                                    )
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 8.dp),
                                enabled = description.isNotBlank(),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Terracotta,
                                    contentColor = Color.White,
                                    disabledContainerColor = Beige,
                                    disabledContentColor = Navy.copy(alpha = 0.3f)
                                )
                            ) {
                                Text("Upload")
                            }
                        }
                    }
                }
            }
        }
    }
}