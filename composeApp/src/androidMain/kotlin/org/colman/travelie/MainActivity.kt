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
import org.colman.travelie.auth.SessionManager
import org.colman.travelie.domain.Auth.Logout
import org.colman.travelie.features.feed.FeedViewModel
import org.colman.travelie.features.logout.LogoutViewModel
import org.colman.travelie.features.profile.ProfileViewModel
import org.colman.travelie.ui.navigation.authNestedGraph
import org.colman.travelie.ui.navigation.mainAppNestedGraph
import org.colman.travelie.ui.shared_components.BottomNavigationBar
import org.colman.travelie.ui.theme.AppTheme
import org.koin.androidx.compose.koinViewModel
import org.colman.travelie.ui.navigation.Routes
import org.koin.androidx.compose.get
import org.koin.compose.koinInject


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        FirebaseApp.initializeApp(this)

        setContent {
            AppTheme {
                val sessionManager: SessionManager = koinInject()
                val user = sessionManager.currentUser.collectAsState().value
                val logoutViewModel: LogoutViewModel = koinViewModel()


                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route


                val currentTopBarTitle = when (currentRoute) {
                    Routes.FEED -> "Feed"
                    Routes.PROFILE -> "Profile"
                    Routes.DESTINATIONS -> "Destinations"
                    else -> ""
                }

                val showBars = currentRoute in listOf(
                    Routes.FEED,
                    Routes.PROFILE,
                    Routes.DESTINATIONS
                )

                LaunchedEffect(user) {
                    val targetRoute = if (user == null) Routes.AUTH_GRAPH else Routes.MAIN_GRAPH
                    navController.navigate(targetRoute) {
                        popUpTo(0) { inclusive = true }
                    }
                }

                Scaffold(
                    topBar = {
                        if (showBars) {
                            AppBar(
                                title = currentTopBarTitle,
                                showBackButton = currentRoute != Routes.FEED,
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
                                       logoutViewModel.logoutUser()
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
                            navController = navController
                        )
                        mainAppNestedGraph(
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
