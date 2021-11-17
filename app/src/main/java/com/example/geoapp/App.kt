package com.example.geoapp

import android.app.Application
import com.example.geoapp.di.component.AppComponent
import com.example.geoapp.di.component.DaggerAppComponent
import com.example.geoapp.di.module.AppModule

class App : Application() {
    companion object {
        lateinit var instance: App
    }

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        instance = this
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }
}