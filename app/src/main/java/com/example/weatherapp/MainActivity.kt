package com.example.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import coil.load
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.weatherapp.adapter.DayAdapter
import com.example.weatherapp.adapter.HourAdapter
import com.example.weatherapp.adapter.DayData
import com.example.weatherapp.databinding.ActivityMainBinding
import org.json.JSONObject
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
    private lateinit var city: String
    private var humidity by Delegates.notNull<Int>()
    private lateinit var wind: String
    private var temp by Delegates.notNull<Double>()
    private var minTemp_c by Delegates.notNull<Double>()
    private var maxTemp_c by Delegates.notNull<Double>()
    private lateinit var last_updated: String
    private lateinit var local_time: String
    private lateinit var status: String
    private var pressure_mb by Delegates.notNull<Double>()
    private lateinit var sunrise: String
    private lateinit var sunset: String
    private lateinit var weatherImg: String
    private lateinit var hoursList: MutableList<DayData>
    private lateinit var adapter: HourAdapter
    private lateinit var weekAdapter: DayAdapter
    private lateinit var dayList: MutableList<DayData>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hoursList = mutableListOf()
        dayList = mutableListOf()
        adapter = HourAdapter(hoursList)
        weekAdapter = DayAdapter(dayList)
        binding.hoursRv.adapter = adapter
        binding.futureForecastRv.adapter = weekAdapter

        val requestQueue = Volley.newRequestQueue(this)
        binding.searchBtn.setOnClickListener {
            val url =
                "http://api.weatherapi.com/v1/forecast.json?Key=542c36dc84064b1fa22132223230810&q=${binding.address.text}&days=7"
            val request = JsonObjectRequest(url, object : Response.Listener<JSONObject> {
                override fun onResponse(response: JSONObject?) {


                    val location = response!!.getJSONObject("location")
                    val forecast = response.getJSONObject("current")
                    val weather_condition =
                        response.getJSONObject("current").getJSONObject("condition")
                    city = location.getString("name")
                    val week = response.getJSONObject("forecast").getJSONArray("forecastday")
                    val hours = response.getJSONObject("forecast").getJSONArray("forecastday")
                        .getJSONObject(0).getJSONArray("hour")
                    val sun_info = response.getJSONObject("forecast").getJSONArray("forecastday")
                        .getJSONObject(0).getJSONObject("astro")
                    val tempInfo = response.getJSONObject("forecast").getJSONArray("forecastday")
                        .getJSONObject(0).getJSONObject("day")
                    var day_info: JSONObject
                    var week_info: JSONObject
                    for (i in 0 until hours.length()) {

                        day_info = hours.getJSONObject(i)

                        val date = day_info.getString("time")
                        val wind_mph =
                            day_info.getString("wind_mph").toString() + " m/s"
                        val feels_like =
                            day_info.getString("feelslike_c").toString() + "° C"
                        val condition = day_info.getJSONObject("condition")
                        val con_text = condition.getString("text")
                        val day_icon = "https:" + condition.getString("icon")
                        hoursList.add(DayData(date, wind_mph, feels_like, con_text, day_icon))

                        adapter.notifyDataSetChanged()

                    }
                    for (i in 0 until week.length()) {

                        week_info = week.getJSONObject(i)

                        val date = week_info.getString("date")
                        val temp = week_info.getJSONObject("day").getString("avgtemp_c") + "° C"
                        val condition = week_info.getJSONObject("day").getJSONObject("condition")
                        val con_text = condition.getString("text")
                        val day_icon = "https:" + condition.getString("icon")
                        val humidity = week_info.getJSONObject("day").getString("avghumidity")
                        dayList.add(DayData(date, temp, humidity, con_text, day_icon))
                        weekAdapter.notifyDataSetChanged()

                    }
                    weatherImg = weather_condition.getString("icon")
                    humidity = forecast.getInt("humidity")
                    temp = forecast.getDouble("temp_c")
                    local_time = location.getString("localtime")
                    last_updated = forecast.getString("last_updated")
                    minTemp_c = tempInfo.getDouble("mintemp_c")
                    maxTemp_c = tempInfo.getDouble("maxtemp_c")
                    status = weather_condition.getString("text")
                    wind = forecast.getString("wind_mph")
                    pressure_mb = forecast.getDouble("pressure_mb")

                    var h24_rise = sun_info.getString("sunrise").subSequence(0, 2)
                    var m24_rise = sun_info.getString("sunrise").subSequence(3, 5)
                    sunrise = "$h24_rise:$m24_rise"

                    var h24_set =
                        sun_info.getString("sunset").subSequence(0, 2).toString().toInt() + 12
                    var m24_set = sun_info.getString("sunset").subSequence(3, 5)
                    sunset = "$h24_set:$m24_set"

                    binding.weatherImg.load("https:${weather_condition.getString("icon")}")
                    binding.lastUpdated.text = last_updated
                    binding.localDate.text = local_time
                    binding.temp.text = temp.toString()
                    binding.status.text = status
                    binding.humidity.text = humidity.toString()
                    binding.wind.text = wind
                    binding.sunrise.text = sunrise
                    binding.pressure.text = pressure_mb.toString()
                    binding.sunset.text = sunset
                    binding.tempMax.text = maxTemp_c.toString()
                    binding.tempMin.text = minTemp_c.toString()
                }
            }, object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError?) {
                    Log.d("BBB", "onResponse: $error")
                }
            })

            requestQueue.add(request)

        }
    }
}