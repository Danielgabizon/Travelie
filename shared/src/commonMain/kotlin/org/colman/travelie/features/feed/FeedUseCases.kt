package org.colman.travelie.features.feed

import org.colman.travelie.domain.Feed.GetPosts

data class FeedUseCases (
    val getPosts: GetPosts,
)