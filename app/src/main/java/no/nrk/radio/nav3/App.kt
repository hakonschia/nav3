package no.nrk.radio.nav3

import android.app.Application
import no.nrk.radio.nav3.screens.SeriesViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(applicationContext)
            modules(
                listOf(
                    module {
                        viewModelOf(::DeepLinkViewModel)
                        viewModelOf(::SeriesViewModel)
                    }
                )
            )
        }
    }
}
