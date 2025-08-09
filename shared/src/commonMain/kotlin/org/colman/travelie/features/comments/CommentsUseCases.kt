package org.colman.travelie.features.comments

import org.colman.travelie.domain.Comment.AddComment
import org.colman.travelie.domain.Comment.GetComments
import org.colman.travelie.domain.Comment.IncrementCommentCount

data class CommentsUseCases (
    val getComments: GetComments,
    val addComment: AddComment,
    val incrementCommentCount: IncrementCommentCount

)