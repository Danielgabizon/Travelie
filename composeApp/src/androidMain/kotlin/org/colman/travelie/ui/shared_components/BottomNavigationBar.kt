package org.colman.travelie.ui.shared_components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.colman.travelie.ui.navigation.Routes

@Composable
fun BottomNavigationBar(
    currentRoute: String,
    onTabSelected: (String) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface

    ){
        NavigationBarItem(
            selected = currentRoute == Routes.FEED,
            onClick = { onTabSelected(Routes.FEED) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Feed") },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor =  MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
            )
        )
        NavigationBarItem(
            selected = currentRoute == Routes.DESTINATIONS,
            onClick = { onTabSelected(Routes.DESTINATIONS) },
            icon = { Icon(Icons.Default.Place, contentDescription = "Destinations") },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor =  MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
            )
        )
        NavigationBarItem(
            selected = currentRoute == Routes.PROFILE,
            onClick = { onTabSelected(Routes.PROFILE) },
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor =  MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = { onTabSelected(Routes.LOGOUT) },
            icon = { Icon(Icons.Default.Logout, contentDescription = "Logout") },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor =  MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
            )
        )
    }
}