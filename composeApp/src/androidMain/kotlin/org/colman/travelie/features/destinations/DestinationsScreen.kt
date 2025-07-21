package org.colman.travelie.features.destinations
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import org.colman.travelie.models.Destination
import org.colman.travelie.models.Destinations
import org.koin.androidx.compose.koinViewModel
import org.colman.travelie.features.destinations.permissions.HandleLocationPermission


private val Navy = Color(0xFF263238)
private val LightGray = Color(0xFFECEFF1)
private val Lavender = Color(0xFFDAD4DA)
private val Beige = Color(0xFFD7B8A5)
private val Terracotta = Color(0xFFC97C5D)



@Composable
fun DestinationsScreen(
    viewModel: DestinationsViewModel = koinViewModel()
) {


    val context = LocalContext.current
    val uiState = viewModel.uiState.collectAsState().value
    var searchQuery by remember { mutableStateOf("") }

    HandleLocationPermission {
        viewModel.searchByCurrentLocation()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightGray)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search destinations") },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Terracotta,
                    unfocusedBorderColor = Lavender,
                    cursorColor = Terracotta
                )
            )

            Button(
                onClick = { viewModel.search(searchQuery) },
                enabled = searchQuery.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Terracotta,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Search")
            }
        }


        when (uiState) {
            is DestinationsState.Error -> ErrorContent(uiState.errorMessage)
            is DestinationsState.Loaded -> DestinationsContent(uiState.destinations)
            DestinationsState.Loading -> LoadingContent()
        }
    }
}

@Composable
fun DestinationsContent(
    destinations: Destinations,
    lazyGridState: LazyGridState = rememberLazyGridState()
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        state = lazyGridState,
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 2.dp)
    ) {
        items(destinations.items) { destination ->
            DestinationGridItem(destination)
        }
    }
}
@Composable
fun DestinationGridItem(destination: Destination) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.73f),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()

        ) {
            // Image on top
            AsyncImage(
                model = destination.thumbnail ?: "",
                contentDescription = destination.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = destination.title ?: "",
                    style = MaterialTheme.typography.titleMedium.copy(color = Navy),

                )
                Text(
                    text = destination.description ?: "",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.DarkGray),
                    maxLines = 2,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            destination.link?.let { link ->
                Text(
                    text = "View more",
                    color = Terracotta,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 8.dp)
                        .clickable {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                            context.startActivity(intent)
                        }
                )
            }
        }
    }
}



@Composable
fun ErrorContent(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = message,
            style = TextStyle(fontSize = 22.sp, color = Terracotta, textAlign = TextAlign.Center)
        )
    }
}

@Composable
fun LoadingContent() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp),
            color = Terracotta,
            trackColor = Lavender
        )
    }
}



@Preview(showBackground = true)
@Composable
fun DestinationsContentPreview() {
    val sampleDestinations = Destinations(
        items = listOf(
            Destination(
                title = "Paris",
                description = "The city of lights",
                flightPrice = 320,
                hotelPrice = 450,
                link = "https://example.com/paris",
                thumbnail = "https://picsum.photos/400/300"
            ),
            Destination(
                title = "Tokyo",
                description = "Land of the rising sun",
                flightPrice = 700,
                hotelPrice = 850,
                link = "https://example.com/tokyo",
                thumbnail = "https://picsum.photos/400/301"
            ),
            Destination(
                title = "New York",
                description = "The city that never sleeps",
                flightPrice = 500,
                hotelPrice = 600,
                link = "https://example.com/nyc",
                thumbnail = "https://picsum.photos/400/302"
            ),
            Destination(
                title = "Rome",
                description = "The eternal city bld bld dbld bld ",
                flightPrice = 350,
                hotelPrice = 400,
                link = "https://example.com/rome",
                thumbnail = "https://picsum.photos/400/303"
            )
        )
    )

    MaterialTheme {
        DestinationsContent(destinations = sampleDestinations)
    }
}
