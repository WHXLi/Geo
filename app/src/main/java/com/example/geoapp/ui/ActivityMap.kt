package com.example.geoapp.ui

import android.os.Bundle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.geoapp.App
import com.example.geoapp.R
import com.example.geoapp.databinding.ActivityMapBinding
import com.example.geoapp.mvp.presenter.ActivityMapPresenter
import com.example.geoapp.mvp.view.ActivityMapView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.maps.android.data.geojson.GeoJsonLayer
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter

class ActivityMap : MvpAppCompatActivity(R.layout.activity_map), ActivityMapView,
    OnMapReadyCallback {
    private val binding by viewBinding<ActivityMapBinding>()
    private val presenter by moxyPresenter {
        ActivityMapPresenter().apply {
            App.instance.appComponent.inject(this)
        }
    }
    private lateinit var map: GoogleMap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Загружаем карту
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        presenter.loadGeoJson()
    }

    override fun displayRoute() {
        //Отображаем наш json объект на карте в виде маршрута, и отображаем дистанцию маршрута
        GeoJsonLayer(map, presenter.getGeoJson()).apply {
            this.addLayerToMap()
            binding.distance.text = presenter.calculate(this)
        }
        // Перемещаем камеру
        map.moveCamera(CameraUpdateFactory.newLatLng(presenter.coordinateMoveCamera()))
    }
}