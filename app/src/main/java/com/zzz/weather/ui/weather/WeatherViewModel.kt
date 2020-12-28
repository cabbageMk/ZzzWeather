package com.zzz.weather.ui.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.zzz.weather.logic.Repository
import com.zzz.weather.logic.model.Location

class WeatherViewModel: ViewModel() {

    private val locationLiveData = MutableLiveData<Location>()

    var lng: String = ""

    var lat: String = ""

    var placeName:String = ""

    val weatherLiveData = Transformations.switchMap(locationLiveData) {
        Repository.refreshWeather(it.lng, it.lat)
    }

    fun refreshWeather(lng: String, lat: String) {
        locationLiveData.value = Location(lng, lat)
    }
}