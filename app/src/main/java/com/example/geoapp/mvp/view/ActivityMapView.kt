package com.example.geoapp.mvp.view

import moxy.MvpView
import moxy.viewstate.strategy.alias.OneExecution

@OneExecution
interface ActivityMapView : MvpView {
    fun displayRoute()
}