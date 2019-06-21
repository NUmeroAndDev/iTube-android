package com.numero.itube

import android.app.Application
import com.numero.itube.di.ApplicationComponent
import com.numero.itube.di.ApplicationModule
import com.numero.itube.di.DaggerApplicationComponent
import com.numero.itube.di.DataSourceModule

class iTubeApplication : Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .dataSourceModule(DataSourceModule(getString(R.string.api_key)))
                .build()
    }
}
