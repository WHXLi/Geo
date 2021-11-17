package com.example.geoapp.di.module

import com.example.geoapp.App
import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler

@Module
class AppModule(private val app: App) {
    @Provides
    fun app(): App = app

    @Provides
    fun uiScheduler(): Scheduler = AndroidSchedulers.mainThread()

}