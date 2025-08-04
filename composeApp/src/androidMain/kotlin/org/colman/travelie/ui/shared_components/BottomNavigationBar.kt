package org.colman.travelie.ui.shared_components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.colman.travelie.ui.navigation.Routes

@Composable
fun BottomNavigationBar(
    currentRoute: String,
    onTabSelected: (String) -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = currentRoute == Routes.FEED,
            onClick = { onTabSelected(Routes.FEED) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Feed") },
            label = { Text("Feed") }
        )
        NavigationBarItem(
            selected = currentRoute == Routes.DESTINATIONS,
            onClick = { onTabSelected(Routes.DESTINATIONS) },
            icon = { Icon(Icons.Default.Place, contentDescription = "Destinations") },
            label = { Text("Destinations") }
        )
        NavigationBarItem(
            selected = currentRoute == Routes.PROFILE,
            onClick = { onTabSelected(Routes.PROFILE) },
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { onTabSelected(Routes.LOGOUT) },
            icon = { Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Logout") },
            label = { Text("Logout") }
        )
    }
}