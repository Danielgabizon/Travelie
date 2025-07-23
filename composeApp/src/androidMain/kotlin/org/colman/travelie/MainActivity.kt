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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import org.colman.travelie.features.auth.AuthState
import org.colman.travelie.features.auth.AuthViewModel
import org.colman.travelie.features.auth.LoginScreen
import org.colman.travelie.features.auth.RegisterScreen
import org.colman.travelie.features.destinations.DestinationsScreen
import org.colman.travelie.ui.shared_components.BottomNavigationBar
import org.colman.travelie.ui.theme.AppTheme
import org.koin.androidx.compose.koinViewModel

sealed class MainAppTab(val route: String, val title: String) {
    data object Destinations : MainAppTab("destinations", "Destinations")
    data object Logout : MainAppTab("logout", "Logout")

}

class MainActivity() : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        FirebaseApp.initializeApp(this)

        setContent {
            AppTheme {
                val authViewModel: AuthViewModel = koinViewModel()
                val uiState = authViewModel.uiState.collectAsState().value

                val user = (uiState as? AuthState.Loaded)?.user
                val isLoggedIn = user != null

                val navController = rememberNavController()
                var selectedTab by remember { mutableStateOf<MainAppTab?>(null) }

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
                                        selectedTab = null
                                        navController.navigate("login") {
                                            popUpTo(0) { inclusive = true }
                                        }
                                    } else {
                                        selectedTab = tab
                                        navController.navigate(tab.route) {
                                            popUpTo(navController.graph.startDestinationId) {
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
                        startDestination = "login",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("login") {
                            LoginScreen(
                                viewModel = authViewModel,
                                onNavigateToRegister = { navController.navigate("register") },
                                onLoginSuccess = {
                                    selectedTab = MainAppTab.Destinations
                                    navController.navigate(MainAppTab.Destinations.route) {
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }

                        composable("register") {
                            RegisterScreen(
                                viewModel = authViewModel,
                                onNavigateToLogin = { navController.navigate("login") },
                                onRegisterSuccess = {
                                    selectedTab = MainAppTab.Destinations
                                    navController.navigate(MainAppTab.Destinations.route) {
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
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