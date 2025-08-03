package org.colman.travelie.features.uploadPost

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

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
                Text("Upload Post",
                    fontSize = 24.sp,
                    color = Navy,
                )

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
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Terracotta,
                        contentColor = Color.White,
                        disabledContainerColor = Beige,
                        disabledContentColor = Navy.copy(alpha = 0.3f))) {
                    Text("Upload Post", color = Color.White)
                }



            }

        }

        Spacer(modifier = Modifier.height(32.dp))

        when (uiState) {
            is UploadPostState.Loading -> Spinner(Modifier.fillMaxWidth())

            is UploadPostState.Loaded -> {
                val post = uiState.post
                uiState.post?.let { post ->
                    onPostUploaded(post)
                }
            }
            is UploadPostState.Error -> Error(
                uiState.errorMessage,
                Modifier.fillMaxWidth()
            )
        }
    }
}
