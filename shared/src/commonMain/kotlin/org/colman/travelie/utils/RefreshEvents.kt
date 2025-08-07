package org.colman.travelie.utils
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
object RefreshEvents {
    private val _refreshFeed = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val refreshFeed = _refreshFeed.asSharedFlow()

    private val _refreshProfile = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val refreshProfile = _refreshProfile.asSharedFlow()

    fun triggerFeedRefresh() {
        _refreshFeed.tryEmit(Unit)
    }

    fun triggerProfileRefresh() {
        _refreshProfile.tryEmit(Unit)
    }
}