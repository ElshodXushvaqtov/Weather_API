package com.example.weatherapp

data class Forecast(
    val humidity: String, val wind_mps: Double, val pressure_mb:Double, var cloud:Int, val location: String
)