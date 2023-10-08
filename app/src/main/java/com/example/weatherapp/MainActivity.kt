package com.example.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.weatherapp.databinding.ActivityMainBinding
import org.json.JSONObject
import java.net.URL
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
    private lateinit var address: String
    private var humidity by Delegates.notNull<Int>()
    private var wind by Delegates.notNull<Double>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        address = binding.address.text.toString()
        val url =
            "http://api.weatherapi.com/v1/current.json?Key=542c36dc84064b1fa22132223230810&q=Tashkent"
        val requestQueue = Volley.newRequestQueue(this)
        val request = JsonObjectRequest(url, object : Response.Listener<JSONObject> {
            override fun onResponse(response: JSONObject?) {
                val main = response?.getJSONObject("main")
                address = main?.getString("name").toString()
                binding.address.text = address

            }
        }, object : Response.ErrorListener {
            override fun onErrorResponse(error: VolleyError?) {
                Log.d("BBB", "onResponse: $error")
            }
        })
        requestQueue.add(request)

    }

}