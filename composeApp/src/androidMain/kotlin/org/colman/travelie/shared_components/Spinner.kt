package org.colman.travelie.shared_components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


private val Navy = Color(0xFF263238)
private val LightGray = Color(0xFFECEFF1)
private val Lavender = Color(0xFFDAD4DA)
private val Beige = Color(0xFFD7B8A5)
private val Terracotta = Color(0xFFC97C5D)


@Composable
fun Spinner() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp),
            color = Terracotta,
            trackColor = Lavender
        )
    }
}
