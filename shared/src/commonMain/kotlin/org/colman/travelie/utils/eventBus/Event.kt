package org.colman.travelie.utils.eventBus

sealed class Event {
    data object PostUploaded : Event()
    data object CommentAdded : Event()


}