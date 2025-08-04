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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import org.colman.travelie.features.auth.*
import org.colman.travelie.ui.navigation.authNestedGraph
import org.colman.travelie.ui.navigation.mainAppNestedGraph
import org.colman.travelie.ui.shared_components.BottomNavigationBar
import org.colman.travelie.ui.theme.AppTheme
import org.koin.androidx.compose.koinViewModel
import org.colman.travelie.ui.navigation.Routes


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

                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route


                // top bar title based on current route
                val currentTitle = when (currentRoute) {
                    Routes.FEED -> "Feed"
                    Routes.PROFILE -> "Profile"
                    Routes.DESTINATIONS -> "Destinations"
                    else -> ""
                }

                // show top & bottom bars based on current route
                val showBars = currentRoute in listOf(
                    Routes.FEED,
                    Routes.PROFILE,
                    Routes.DESTINATIONS
                )

                LaunchedEffect(user) {
                    if (user != null) {
                        navController.navigate(Routes.MAIN_GRAPH) {
                            popUpTo(navController.graph.id) { inclusive = true }
                        }
                    } else{
                        navController.navigate(Routes.AUTH_GRAPH) {
                            popUpTo(navController.graph.id) { inclusive = true }
                        }

                    }
                }

                Scaffold(
                    topBar = {
                        if (showBars) {
                            AppBar(
                                title = currentTitle,
                                showBackButton = false,
                                onBackClick = { navController.popBackStack() }
                            )
                        }
                    },
                    bottomBar = {
                        if (showBars) {
                            BottomNavigationBar(
                                currentRoute = currentRoute ?: "",
                                onTabSelected = { route ->
                                    if (route == Routes.LOGOUT) {
                                        authViewModel.logout()
                                    } else {
                                        navController.navigate(route) {
                                            popUpTo(Routes.FEED) {
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
                        mainAppNestedGraph(authViewModel= authViewModel,
                            navController = navController
                        )
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
