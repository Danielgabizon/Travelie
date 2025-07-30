package org.colman.travelie.features.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.colman.travelie.R
import org.colman.travelie.features.auth.AuthState
import org.colman.travelie.features.auth.AuthViewModel
import org.colman.travelie.features.user.UserState
import org.colman.travelie.features.user.UserViewModel
import org.colman.travelie.models.User
import org.colman.travelie.ui.shared_components.Error
import org.colman.travelie.ui.shared_components.Spinner
import org.colman.travelie.ui.theme.*
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel = koinViewModel()
) {
    val authState = authViewModel.uiState.collectAsState().value
    val userUIState = userViewModel.uiState.collectAsState().value
    val authUser = (authState as? AuthState.Loaded)?.user
    val uid = authUser?.uid

    LaunchedEffect(uid) {
        if (uid != null) {
            userViewModel.getUserDetails(uid)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightGray)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {


        when (userUIState) {
            is UserState.Loading -> Spinner(modifier = Modifier.fillMaxSize())
            is UserState.Loaded -> ProfileContent(userUIState.user)
            is UserState.Error -> Error(
                message = userUIState.errorMessage,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun ProfileContent(user: User?) {
    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Column(
            modifier = Modifier.padding(24.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = R.drawable.default_avatar),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "${user?.firstName ?: "First Name"} ${user?.lastName ?: "Last Name"}",
                fontSize = 24.sp,
                color = Terracotta,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = user?.email ?: "No Email",
                style = MaterialTheme.typography.bodyLarge,
                color = Lavender
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (!user?.bio.isNullOrBlank()) {
                Text(
                    text = user?.bio ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Navy
                )
            }
        }
    }
}
