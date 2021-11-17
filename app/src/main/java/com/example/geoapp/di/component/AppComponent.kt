package com.example.geoapp.di.component

import com.example.geoapp.di.module.ApiModule
import com.example.geoapp.di.module.AppModule
import com.example.geoapp.mvp.presenter.ActivityMapPresenter

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        ApiModule::class,
    ]
)
interface AppComponent {
    fun inject(activityMapPresenter: ActivityMapPresenter)
}