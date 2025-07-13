package org.colman.travelie.di

import org.koin.core.module.Module

import org.colman.travelie.features.destinations.DestinationsViewModel
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val platformModule: Module = module {
    single<HttpClientEngine> { Darwin.create() }

    factoryOf(::DestinationsViewModel)

    //single<SqlDriver> { DatabaseDriverFactory().createDriver() }
}