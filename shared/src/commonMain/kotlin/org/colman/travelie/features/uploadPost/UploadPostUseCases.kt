package org.colman.travelie.features.uploadPost

import org.colman.travelie.domain.Post.CreatePost
import org.colman.travelie.domain.Post.UploadPostPicture

data class UploadPostUseCases (
    val createPost: CreatePost,
    val uploadPostPicture: UploadPostPicture
    )