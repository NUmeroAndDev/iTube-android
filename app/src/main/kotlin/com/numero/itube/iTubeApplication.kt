package com.numero.itube

import android.app.Application
import com.numero.itube.di.*

class iTubeApplication : Application(){

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .repositoryModule(RepositoryModule())
                .apiClientModule(ApiClientModule())
                .dBModule(DBModule())
                .build()
    }
}
