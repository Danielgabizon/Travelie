package org.colman.travelie.features.comments

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.colman.travelie.data.Result
import org.colman.travelie.features.BaseViewModel
import org.colman.travelie.models.Comments
import org.colman.travelie.models.Comment
import org.colman.travelie.auth.SessionManager
import org.colman.travelie.utils.eventBus.Event
import org.colman.travelie.utils.eventBus.EventBus
import kotlin.uuid.Uuid
import kotlin.uuid.ExperimentalUuidApi

class CommentsViewModel(
    private val postId: String,
    private val useCases: CommentsUseCases,
    sessionManager: SessionManager) : BaseViewModel<CommentsState>() {

    private val _uiState: MutableStateFlow<CommentsState> = MutableStateFlow(CommentsState.Loading)
    override val uiState: StateFlow<CommentsState> get() = _uiState

    private val _submitState = MutableStateFlow<SubmitCommentState>(SubmitCommentState.Idle)
    val submitState: StateFlow<SubmitCommentState> get() = _submitState

    val user = sessionManager.currentUser

    init {
        loadComments()
    }

    private fun loadComments() {
        scope.launch {
            _uiState.emit(CommentsState.Loading)
            when (val result = useCases.getComments(postId)) {
                is Result.Success -> {
                    val comments = result.data!!
                    _uiState.emit(CommentsState.Loaded(comments))
                }

                is Result.Failure -> {
                    _uiState.emit(
                        CommentsState.Error(result.error?.message ?: "Failed to load comments")
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    fun addComment(content: String) {
        scope.launch {
            val currentUser = user.value

            if (currentUser == null) {
                _submitState.emit(SubmitCommentState.Error("User not logged in"))
                return@launch
            }

            _submitState.emit(SubmitCommentState.Submitting)


            val comment = Comment(
                commentId = Uuid.random().toString(),
                postId = postId,
                uid = currentUser.uid,
                username = currentUser.username,
                userImageUrl = currentUser.profilePicture,
                content = content
            )

            when (val result = useCases.addComment(comment)) {
                is Result.Success -> {
                    useCases.incrementCommentCount(postId) //
                    _submitState.emit(SubmitCommentState.Idle)
                    loadComments()
                    EventBus.emit(Event.CommentAdded) // notify that the post has been updated
                }

                is Result.Failure -> {
                    _submitState.emit(
                        SubmitCommentState.Error(
                            result.error?.message ?: "Failed to add comment"
                        )
                    )
                }
            }
        }
    }
}





