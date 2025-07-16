package org.colman.travelie

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.google.firebase.FirebaseApp
import org.colman.travelie.features.auth.LoginScreen
import org.colman.travelie.features.auth.RegisterScreen
import org.colman.travelie.features.destinations.DestinationsContent
import org.colman.travelie.features.destinations.DestinationsScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        setContent {
            MaterialTheme {
                DestinationsScreen()
            }
        }
    }
}

