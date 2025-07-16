package org.colman.travelie.di

import org.koin.core.module.Module

import org.colman.travelie.features.destinations.DestinationsViewModel
import org.colman.travelie.features.auth.AuthViewModel
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module


actual val platformModule: Module = module {
    single<HttpClientEngine> { OkHttp.create() }

    viewModelOf(::DestinationsViewModel)
    viewModelOf(::AuthViewModel)

    //single<SqlDriver> { DatabaseDriverFactory(get()).createDriver() }
}