package org.colman.travelie.features.comments

sealed class SubmitCommentState {
    data object Idle : SubmitCommentState()
    data object Submitting: SubmitCommentState()
    data class Error(val errorMessage: String) : SubmitCommentState()
}
