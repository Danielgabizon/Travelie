package org.colman.travelie.di

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.dsl.module
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf

import org.colman.travelie.features.destinations.DestinationsViewModel
import org.colman.travelie.features.feed.FeedViewModel
import org.colman.travelie.features.uploadPost.UploadPostViewModel
import org.colman.travelie.features.comments.CommentsViewModel
import org.colman.travelie.features.profile.ProfileViewModel
import org.colman.travelie.features.register.RegisterViewModel
import org.colman.travelie.features.login.LoginViewModel
import org.colman.travelie.features.logout.LogoutViewModel
import org.colman.travelie.utils.GeoDecoder
import org.colman.travelie.utils.LocationProvider

actual val platformModule: Module = module {
    single<HttpClientEngine> { Darwin.create() }


    factoryOf(::DestinationsViewModel)
    factoryOf(::RegisterViewModel)
    factoryOf(::LoginViewModel)
    factoryOf(::LogoutViewModel)
    factoryOf(::ProfileViewModel)
    factoryOf(::FeedViewModel)
    factoryOf(::UploadPostViewModel)

    factory { (postId: String) ->
        CommentsViewModel(
            postId = postId,
            useCases = get(),
            sessionManager = get()
        )
    }

    single { LocationProvider() }
    single { GeoDecoder() }
}