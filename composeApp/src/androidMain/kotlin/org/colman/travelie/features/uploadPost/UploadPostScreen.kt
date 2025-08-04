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
import org.colman.travelie.features.auth.AuthState
import org.colman.travelie.features.auth.AuthViewModel
import org.colman.travelie.models.Post
import org.colman.travelie.ui.shared_components.Error
import org.colman.travelie.ui.shared_components.Spinner
import org.colman.travelie.ui.theme.*
import org.koin.androidx.compose.koinViewModel
import java.util.*
@Composable
fun UploadPostScreen(
    authViewModel: AuthViewModel,
    uploadPostViewModel: UploadPostViewModel = koinViewModel(),
    onPostUploaded: (Post) -> Unit,
    onCancel: () -> Unit
) {
    val authState = authViewModel.uiState.collectAsState().value
    val user = (authState as? AuthState.Loaded)?.user

    val uiState = uploadPostViewModel.uiState.collectAsState().value
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

                if (user != null) {
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
                            text ="${user.firstName} ${user.lastName}",
                            fontSize = 18.sp,
                            color = Navy
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }

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
                            if (user != null) {
                                uploadPostViewModel.uploadPost(
                                    user = user,
                                    description = description,
                                    imageUrl = "https://picsum.photos/200/300?random=${UUID.randomUUID()}"
                                )
                            }
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

        Spacer(modifier = Modifier.height(32.dp))

        when (uiState) {
            is UploadPostState.Loading -> Spinner(modifier = Modifier.fillMaxWidth())
            is UploadPostState.Loaded -> {
                uiState.post?.let { post -> onPostUploaded(post) }
            }
            is UploadPostState.Error -> Error(
                message = uiState.errorMessage,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
