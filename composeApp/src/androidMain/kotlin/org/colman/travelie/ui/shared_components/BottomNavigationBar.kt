package org.colman.travelie.ui.shared_components

import androidx.compose.material.icons.Icons
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
    }
}