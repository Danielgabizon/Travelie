package org.colman.travelie.di

import org.koin.core.module.Module
import org.colman.travelie.features.destinations.DestinationsViewModel
import org.colman.travelie.features.auth.AuthViewModel
import org.colman.travelie.features.feed.FeedViewModel
import org.colman.travelie.features.uploadPost.UploadPostViewModel
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.colman.travelie.utils.GeoDecoder
import org.colman.travelie.utils.LocationProvider
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

import org.koin.android.ext.koin.androidContext



actual val platformModule: Module = module {
    single<HttpClientEngine> { OkHttp.create() }

    viewModelOf(::DestinationsViewModel)
    viewModelOf(::AuthViewModel)
    viewModelOf(::FeedViewModel)
    viewModelOf(::UploadPostViewModel)


    single { LocationProvider(androidContext()) }
    single { GeoDecoder(androidContext()) }

    //single<SqlDriver> { DatabaseDriverFactory(get()).createDriver() }
}