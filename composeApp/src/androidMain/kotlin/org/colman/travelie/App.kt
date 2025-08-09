

package org.colman.travelie

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import org.colman.travelie.auth.SessionManager
import org.colman.travelie.features.logout.LogoutViewModel
import org.colman.travelie.ui.navigation.authNestedGraph
import org.colman.travelie.ui.navigation.mainAppNestedGraph
import org.colman.travelie.ui.shared_components.BottomNavigationBar
import org.colman.travelie.ui.shared_components.AppBar
import org.colman.travelie.ui.theme.AppTheme
import org.koin.androidx.compose.koinViewModel
import org.colman.travelie.ui.navigation.Routes
import org.koin.compose.koinInject

@Composable
fun App() {
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
        Routes.UPLOAD_POST -> "Upload Post"
        Routes.COMMENTS_WITH_ARG -> "Comments"
        else -> ""
    }

    val showBars = currentRoute in listOf(
        Routes.FEED,
        Routes.UPLOAD_POST,
        Routes.COMMENTS_WITH_ARG,
        Routes.PROFILE,
        Routes.DESTINATIONS
    )

    val showBackArrow = currentRoute in listOf(
        Routes.UPLOAD_POST,
        Routes.COMMENTS_WITH_ARG
    )

    LaunchedEffect(user) {
        val targetRoute = if (user == null) Routes.AUTH_GRAPH else Routes.MAIN_GRAPH
        navController.navigate(targetRoute) {
            popUpTo(0) { inclusive = true }
        }
    }

    AppTheme {
        Scaffold(
            topBar = {
                if (showBars) {
                    AppBar(
                        title = currentTopBarTitle,
                        showBackButton = showBackArrow,
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
                authNestedGraph(navController)
                mainAppNestedGraph(navController)
            }
        }
    }
}
