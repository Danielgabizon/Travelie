package org.colman.travelie

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import org.colman.travelie.features.auth.*
import org.colman.travelie.ui.navigation.authNestedGraph
import org.colman.travelie.ui.navigation.mainAppNestedGraph
import org.colman.travelie.ui.shared_components.BottomNavigationBar
import org.colman.travelie.ui.theme.AppTheme
import org.koin.androidx.compose.koinViewModel
import org.colman.travelie.ui.navigation.Routes

sealed class MainAppTab(val route: String, val title: String) {
    data object Profile : MainAppTab(Routes.PROFILE, "Profile")
    data object Destinations : MainAppTab(Routes.DESTINATIONS, "Destinations")
    data object Logout : MainAppTab(Routes.LOGOUT, "Logout")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        FirebaseApp.initializeApp(this)

        setContent {
            AppTheme {
                val authViewModel: AuthViewModel = koinViewModel()
                val uiState by authViewModel.uiState.collectAsState()
                val user = (uiState as? AuthState.Loaded)?.user
                val isLoggedIn = user != null

                val navController = rememberNavController()
                var selectedTab by remember { mutableStateOf<MainAppTab?>(null) }
                val defaultTab = MainAppTab.Destinations

                LaunchedEffect(user) {
                    if (user != null && selectedTab == null) {
                        selectedTab = defaultTab
                        navController.navigate(Routes.MAIN_GRAPH) {
                            popUpTo(Routes.AUTH_GRAPH) { inclusive = true }
                        }
                    } else if (user == null) {
                        selectedTab = null
                        navController.navigate(Routes.AUTH_GRAPH) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }

                Scaffold(
                    topBar = {
                        if (isLoggedIn && selectedTab != null) {
                            AppBar(
                                title = selectedTab!!.title,
                                showBackButton = false,
                                onBackClick = { navController.popBackStack() }
                            )
                        }
                    },
                    bottomBar = {
                        if (isLoggedIn && selectedTab != null) {
                            BottomNavigationBar(
                                selectedTab = selectedTab!!,
                                onTabSelected = { tab ->
                                    if (tab == MainAppTab.Logout) {
                                        authViewModel.logout()
                                    } else {
                                        selectedTab = tab
                                        navController.navigate(tab.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                }
                            )
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Routes.AUTH_GRAPH,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        authNestedGraph(
                            authViewModel = authViewModel,
                            navController = navController
                        )
                        mainAppNestedGraph(authViewModel= authViewModel)
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
}
