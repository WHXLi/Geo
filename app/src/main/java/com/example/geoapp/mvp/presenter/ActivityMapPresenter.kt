package com.example.geoapp.mvp.presenter

import com.example.geoapp.mvp.view.ActivityMapView
import com.example.geoapp.network.GeoApi
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import com.google.maps.android.data.geojson.GeoJsonLayer
import com.google.maps.android.data.geojson.GeoJsonMultiPolygon
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import moxy.MvpPresenter
import org.json.JSONObject
import java.math.BigDecimal
import java.text.DecimalFormat
import javax.inject.Inject

class ActivityMapPresenter : MvpPresenter<ActivityMapView>() {
    @Inject
    lateinit var api: GeoApi

    @Inject
    lateinit var uiScheduler: Scheduler

    private lateinit var geoJson: JSONObject

    //Координата для движения камеры
    private lateinit var latLngCam: LatLng

    // Переменные для сохранения позиций между которыми будет посчитано расстояние
    private lateinit var latLng1: LatLng
    private lateinit var latLng2: LatLng

    // Загружаем параллельным потоком json файл
    fun loadGeoJson() {
        api.getRussia()
            .observeOn(uiScheduler)
            .subscribeOn(Schedulers.io())
            .subscribe({
                // Схраняем в виде объекта для дальнейшей отрисовки на картах
                geoJson = JSONObject(it.string())
                viewState.displayRoute()
            }, {
                it.printStackTrace()
            })
    }

    fun calculate(route: GeoJsonLayer): String {
        var distance = 0.0
        // Переданный объект перебираем пока не доберемся до координат
        route.features.map { features ->
            val geometry = features.geometry
            if (geometry is GeoJsonMultiPolygon) {
                geometry.polygons.map { polygons ->
                    polygons.coordinates.map { coordinates ->
                        // Координата для перемещения камеры
                        latLngCam = coordinates[0]
                        for (i in coordinates.indices) {
                            // Чтобы не выйти за пределы делаем проверку на последний индекс
                            if (i != coordinates.lastIndex) {
                                latLng1 = coordinates[i]
                                latLng2 = coordinates[i + 1]
                                //Рассчитываем дистанцию между двумя точками
                                distance += SphericalUtil.computeDistanceBetween(latLng1, latLng2)
                            }
                        }

                    }
                }
            }
        }
        // Форматируем огромное число в строку
        return "${DecimalFormat("#.##").format(BigDecimal(distance))} метров"
    }

    fun coordinateMoveCamera() = latLngCam

    fun getGeoJson() = geoJson
}