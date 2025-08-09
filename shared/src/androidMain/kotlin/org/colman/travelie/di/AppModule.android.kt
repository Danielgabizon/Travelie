package org.colman.travelie.di

import org.koin.core.module.Module
import org.colman.travelie.features.destinations.DestinationsViewModel
import org.colman.travelie.features.feed.FeedViewModel
import org.colman.travelie.features.uploadPost.UploadPostViewModel
import org.colman.travelie.features.comments.CommentsViewModel
import org.colman.travelie.features.profile.ProfileViewModel
import org.colman.travelie.features.register.RegisterViewModel
import org.colman.travelie.features.login.LoginViewModel
import org.colman.travelie.features.logout.LogoutViewModel

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.colman.travelie.utils.GeoDecoder
import org.colman.travelie.utils.LocationProvider
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


import org.koin.android.ext.koin.androidContext



actual val platformModule: Module = module {
    single<HttpClientEngine> { OkHttp.create() }

    viewModelOf(::DestinationsViewModel)
    viewModelOf(::RegisterViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::LogoutViewModel)
    viewModelOf(::ProfileViewModel)



    viewModelOf(::FeedViewModel)
    viewModelOf(::UploadPostViewModel)
    viewModelOf(::CommentsViewModel)
    viewModel { (postId: String) ->
        CommentsViewModel(
            postId = postId,
            useCases = get(),
            sessionManager = get()
        )
    }
    viewModelOf(::ProfileViewModel)


    single { LocationProvider(androidContext()) }
    single { GeoDecoder(androidContext()) }

    //single<SqlDriver> { DatabaseDriverFactory(get()).createDriver() }
}