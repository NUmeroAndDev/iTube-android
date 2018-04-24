package com.numero.itube.di

import android.app.Application
import com.numero.itube.iTubeApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    (AndroidSupportInjectionModule::class),
    (ApiClientModule::class),
    (ActivityModule::class)
])
interface ApplicationComponent : AndroidInjector<iTubeApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): ApplicationComponent
    }

    fun inject(application: Application)
}
