package org.colman.travelie.di

import android.os.Build
import androidx.annotation.RequiresApi
import org.koin.core.module.Module
import org.colman.travelie.features.destinations.DestinationsViewModel
import org.colman.travelie.features.auth.AuthViewModel
import org.colman.travelie.features.user.UserViewModel
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.colman.travelie.utils.GeoDecoder
import org.colman.travelie.utils.LocationProvider
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.koin.android.ext.koin.androidContext
import org.colman.travelie.features.post.PostViewModel


@RequiresApi(Build.VERSION_CODES.TIRAMISU)//i added this
actual val platformModule: Module = module {
    single<HttpClientEngine> { OkHttp.create() }

    viewModelOf(::DestinationsViewModel)
    viewModelOf(::AuthViewModel)
    viewModelOf(::UserViewModel)
    viewModelOf(::PostViewModel)
    single { LocationProvider(androidContext()) }
    single { GeoDecoder(androidContext()) }

    //single<SqlDriver> { DatabaseDriverFactory(get()).createDriver() }
}