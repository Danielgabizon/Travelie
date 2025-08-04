package org.colman.travelie.di

import org.colman.travelie.data.destinations.DestinationsRepository
import org.colman.travelie.data.firebase.RemoteFirebaseRepository
import org.colman.travelie.data.destinations.RemoteDestinationsRepository
import org.colman.travelie.domain.Destinations.GetDestinations
import org.colman.travelie.domain.Auth.Login
import org.colman.travelie.domain.Auth.Register
import org.colman.travelie.domain.Auth.Logout
import org.colman.travelie.domain.User.SaveUser
import org.colman.travelie.domain.User.GetUser
import org.colman.travelie.domain.Post.GetPosts
import org.colman.travelie.domain.Post.CreatePost
import org.colman.travelie.features.destinations.DestinationsUseCases
import org.colman.travelie.features.auth.AuthUseCases
import org.colman.travelie.features.feed.FeedUseCases
import org.colman.travelie.features.profile.ProfileUseCases
import org.colman.travelie.features.uploadPost.UploadPostUseCases
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.colman.travelie.data.firebase.FirebaseRepository
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.bind
import org.koin.dsl.module



fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(appModules())
    }
}

fun initKoin() {
    initKoin(null)
}


fun appModules() = listOf(commonModule, platformModule, domainModule)

expect val platformModule: Module

val domainModule = module {

    factoryOf(::GetDestinations)

    factoryOf(::Register)
    factoryOf(::Login)
    factoryOf(::Logout)

    factoryOf(::SaveUser)
    factoryOf(::GetUser)

    factoryOf(::GetPosts)
    factoryOf(::CreatePost)

    factoryOf(::DestinationsUseCases)
    factoryOf(::AuthUseCases)
    factoryOf(::FeedUseCases)
    factoryOf(::UploadPostUseCases)
    factoryOf(::ProfileUseCases)

}

val commonModule = module {
    singleOf(::createJson)
    singleOf(::RemoteFirebaseRepository).bind<FirebaseRepository>()
    singleOf(::RemoteDestinationsRepository).bind<DestinationsRepository>()

    single { createHttpClient(get(), get()) }

}

fun createJson(): Json {
    return Json {
        ignoreUnknownKeys = true
        prettyPrint = true
        isLenient = true
    }
}

fun createHttpClient(clientEngine: HttpClientEngine, json: Json) = HttpClient(clientEngine) {
    install(Logging) {
        level = LogLevel.ALL
        logger = Logger.DEFAULT
    }
    install(ContentNegotiation) {
        json(json)
    }
}