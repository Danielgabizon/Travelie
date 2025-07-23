package org.colman.travelie
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import org.colman.travelie.features.auth.LoginScreen
import org.colman.travelie.features.auth.RegisterScreen
import org.colman.travelie.features.destinations.DestinationsScreen
import org.colman.travelie.ui.shared_components.BottomNavigationBar
import org.colman.travelie.ui.theme.AppTheme

sealed class MainAppTab(val route: String, val title: String) {
    data object Destinations : MainAppTab("destinations", "Destinations")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        setContent {
            AppTheme {
                val navController = rememberNavController()

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                val showNavBars: Boolean = currentRoute in listOf(MainAppTab.Destinations.route)

                var selectedTab by remember { mutableStateOf<MainAppTab?>(null) }


                Scaffold(
                    topBar = {
                        if (showNavBars && selectedTab != null) {
                            AppBar(
                                title = selectedTab!!.title,
                                showBackButton = false,
                                onBackClick = { navController.popBackStack() }
                            )
                        }
                    },
                    bottomBar = {
                        if (showNavBars && selectedTab != null) {
                            BottomNavigationBar(
                                selectedTab = selectedTab!!,
                                onTabSelected = { tab ->
                                    selectedTab = tab
                                    navController.navigate(tab.route) {
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true // to save the state of the previous screens
                                        }
                                        launchSingleTop = true // to avoid multiple instances in the back stack
                                        restoreState = true // if state is saved, restore it
                                    }
                                }
                            )
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "login",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("login") {
                            LoginScreen(
                                onNavigateToRegister = { navController.navigate("register") },
                                onLoginSuccess = {
                                    selectedTab = MainAppTab.Destinations
                                    navController.navigate(MainAppTab.Destinations.route)
                                }
                            )
                        }

                        composable("register") {
                            RegisterScreen(
                                onNavigateToLogin = { navController.navigate("login") },
                                onRegisterSuccess = {
                                    selectedTab = MainAppTab.Destinations
                                    navController.navigate(MainAppTab.Destinations.route)
                                }
                            )
                        }

                        composable(MainAppTab.Destinations.route) {
                            DestinationsScreen()
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    title: String,
    showBackButton: Boolean,
    onBackClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        windowInsets = TopAppBarDefaults.windowInsets,
        title = { Text(text = title) },
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        }
    )
}
