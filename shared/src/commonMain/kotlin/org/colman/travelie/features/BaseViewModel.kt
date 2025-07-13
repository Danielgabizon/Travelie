package org.colman.travelie.features

import kotlinx.coroutines.CoroutineScope

interface UiState

expect open class BaseViewModel() {
    val scope: CoroutineScope
}