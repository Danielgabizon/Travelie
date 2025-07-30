package org.colman.travelie.features.profile

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import org.colman.travelie.R
import org.colman.travelie.features.auth.AuthState
import org.colman.travelie.features.auth.AuthViewModel
import org.colman.travelie.features.user.UserState
import org.colman.travelie.features.user.UserViewModel
import org.colman.travelie.models.Destination
import org.colman.travelie.models.Destinations
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
            .background(LightGray),
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
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RectangleShape
    ) {
        Box(modifier = Modifier.padding(24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
            ) {

                Image(
                    painter = painterResource(id = R.drawable.default_avatar),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.weight(1f)
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
    Posts(
        destinations = mockDestinations(),
        lazyGridState = rememberLazyGridState()
    )
}

@Composable
fun Posts(
    destinations: Destinations,
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
        items(destinations.items) { destination ->
            PostItem(destination)
        }
    }
}

@Composable
fun PostItem(destination: Destination) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.73f)
    ) {
        AsyncImage(
            model = destination.thumbnail,
            contentDescription = destination.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}



fun mockDestinations(): Destinations {
    return Destinations(
        items = listOf(
            Destination(
                title = "Paris, France",
                description = "Romantic getaway in the city of lights",
                link = "https://example.com/paris",
                flightPrice = 350,
                hotelPrice = 120,
                thumbnail = "https://images.unsplash.com/photo-1502602898657-3e91760cbb34"
            ),
            Destination(
                title = "Tokyo, Japan",
                description = "Explore high-tech culture and ancient temples",
                link = "https://example.com/tokyo",
                flightPrice = 900,
                hotelPrice = 200,
                thumbnail = "https://images.unsplash.com/photo-1549692520-acc6669e2f0c"
            ),
            Destination(
                title = "New York, USA",
                description = "The city that never sleeps awaits",
                link = "https://example.com/newyork",
                flightPrice = 600,
                hotelPrice = 180,
                thumbnail = "https://images.unsplash.com/photo-1549924231-f129b911e442"
            ),
            Destination(
                title = "Paris, France",
                description = "Romantic getaway in the city of lights",
                link = "https://example.com/paris",
                flightPrice = 350,
                hotelPrice = 120,
                thumbnail = "https://images.unsplash.com/photo-1502602898657-3e91760cbb34"
            ),
            Destination(
                title = "Tokyo, Japan",
                description = "Explore high-tech culture and ancient temples",
                link = "https://example.com/tokyo",
                flightPrice = 900,
                hotelPrice = 200,
                thumbnail = "https://images.unsplash.com/photo-1549692520-acc6669e2f0c"
            ),
            Destination(
                title = "New York, USA",
                description = "The city that never sleeps awaits",
                link = "https://example.com/newyork",
                flightPrice = 600,
                hotelPrice = 180,
                thumbnail = "https://images.unsplash.com/photo-1549924231-f129b911e442"
            ),
            Destination(
                title = "Paris, France",
                description = "Romantic getaway in the city of lights",
                link = "https://example.com/paris",
                flightPrice = 350,
                hotelPrice = 120,
                thumbnail = "https://images.unsplash.com/photo-1502602898657-3e91760cbb34"
            ),
            Destination(
                title = "Tokyo, Japan",
                description = "Explore high-tech culture and ancient temples",
                link = "https://example.com/tokyo",
                flightPrice = 900,
                hotelPrice = 200,
                thumbnail = "https://images.unsplash.com/photo-1549692520-acc6669e2f0c"
            ),
            Destination(
                title = "New York, USA",
                description = "The city that never sleeps awaits",
                link = "https://example.com/newyork",
                flightPrice = 600,
                hotelPrice = 180,
                thumbnail = "https://images.unsplash.com/photo-1549924231-f129b911e442"
            ),
        )
    )
}
