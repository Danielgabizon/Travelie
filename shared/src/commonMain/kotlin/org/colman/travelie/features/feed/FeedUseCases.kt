package org.colman.travelie.features.feed

import org.colman.travelie.domain.Post.GetPosts

data class FeedUseCases (
    val getPosts: GetPosts,
)