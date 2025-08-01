package org.colman.travelie.ui.shared_components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.colman.travelie.MainAppTab

@Composable
fun BottomNavigationBar(
    selectedTab: MainAppTab,
    onTabSelected: (MainAppTab) -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = selectedTab is MainAppTab.Destinations,
            onClick = { onTabSelected(MainAppTab.Destinations) },
            icon = { Icon(Icons.Default.Place, contentDescription = "Destinations") },
            label = { Text("Destinations") }
        )
        NavigationBarItem(
            selected = selectedTab is MainAppTab.Posts,
            onClick = { onTabSelected(MainAppTab.Posts) },
            icon = { Icon(Icons.Default.Place, contentDescription = "Posts") }, // Use better icon later
            label = { Text("Posts") }
        )
        NavigationBarItem(
            selected = selectedTab is MainAppTab.Profile,
            onClick = { onTabSelected(MainAppTab.Profile) },
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") }
        )
        NavigationBarItem(
            selected = false,
            onClick = {  onTabSelected(MainAppTab.Logout) },
            icon = { Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Logout") },
            label = { Text("Logout") }
        )
    }
}