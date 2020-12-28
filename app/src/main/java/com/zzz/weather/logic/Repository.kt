package com.zzz.weather.logic

import androidx.lifecycle.liveData
import com.zzz.weather.logic.model.Place
import com.zzz.weather.logic.model.Weather
import com.zzz.weather.logic.network.WeatherNetWork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

object Repository {

    fun searchPlaces(query: String) = fire(Dispatchers.IO) {
        val response = WeatherNetWork.searchPlace(query)
        if (response.status == "ok") {
            Result.success(response.places)
        } else {
            Result.failure(RuntimeException("response status is ${response.status}"))
        }
    }

    fun refreshWeather(lng: String, lat: String) = fire(Dispatchers.IO) {
        coroutineScope {
            val deferredRealtime = async {
                WeatherNetWork.queryRealtime(lng, lat)
            }
            val deferredDaily = async {
                WeatherNetWork.queryDaily(lng, lat)
            }
            val realtimeResponse = deferredRealtime.await()
            val dailyResponse = deferredDaily.await()
            if (realtimeResponse.status == "ok" && dailyResponse.status == "ok") {
                val weather =
                    Weather(realtimeResponse.result.realtime, dailyResponse.result.daily)
                Result.success(weather)
            } else {
                Result.failure(RuntimeException("realtimeResponse status is ${realtimeResponse.status} and dailyResponse status is  ${dailyResponse.status}"))
            }
        }
    }

    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
        liveData<Result<T>>(context) {
            val result = try {
                block()
            } catch (e: Exception) {
                Result.failure(e)
            }
            emit(result)
        }
}