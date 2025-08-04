package org.colman.travelie.features.profile

import org.colman.travelie.domain.Post.CreatePost
import org.colman.travelie.domain.Post.GetPosts


data class ProfileUseCases (
    val getPosts: GetPosts,
)