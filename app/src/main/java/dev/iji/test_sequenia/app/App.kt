package dev.iji.test_sequenia.app

import android.app.Application
import dev.iji.test_sequenia.di.presentationModule
import dev.iji.test_sequenia.di.dataModule
import dev.iji.test_sequenia.di.domainModule
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(
                listOf(
                    presentationModule,
                    dataModule,
                    domainModule
                )
            )
        }
    }
}