package com.king.superdemo.activities

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.king.superdemo.interfaces.WeatherApi
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val retrofit = Retrofit.Builder().baseUrl("").addConverterFactory(GsonConverterFactory.create()).build()
//        val weatherApi = retrofit.create(WeatherApi::class.java)
//        lifecycleScope.launch {
//            weatherApi.getWeather("")
//        }
    }
}