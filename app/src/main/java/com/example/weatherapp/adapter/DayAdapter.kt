package com.example.weatherapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.weatherapp.R

class DayAdapter(var days: MutableList<DayData>) :
    RecyclerView.Adapter<DayAdapter.Holder>() {

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        var day = view.findViewById<TextView>(R.id.date)
        var maxtemp = view.findViewById<TextView>(R.id.day_temp)
        var con_text = view.findViewById<TextView>(R.id.day_condition)
        var day_icon = view.findViewById<ImageView>(R.id.day_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.day, parent, false))
    }

    override fun getItemCount(): Int {
        return days.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val day = days[position]
        holder.day.text = day.day
        holder.maxtemp.text = day.maxtemp_c
        holder.con_text.text = day.status
        holder.day_icon.load(day.statuts_icon)


    }
}