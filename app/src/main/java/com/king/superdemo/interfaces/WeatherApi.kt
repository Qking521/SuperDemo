package com.king.superdemo.interfaces

import retrofit2.http.GET

interface WeatherApi {

    @GET("city= London")
    fun getWeather(city: String): Weather
}

data class Weather(val city: String, val temperature: Double, val description: String)
