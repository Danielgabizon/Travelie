package org.colman.travelie.features.uploadPost

import org.colman.travelie.domain.Post.CreatePost

data class UploadPostUseCases (
    val createPost: CreatePost,
    )