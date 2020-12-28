package com.zzz.weather.ui.place

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.zzz.weather.DataStoreUtils
import com.zzz.weather.MainActivity
import com.zzz.weather.R
import com.zzz.weather.logic.model.Place
import com.zzz.weather.ui.weather.WeatherActivity

class PlaceAdapter(private val fragment: Fragment, private val placeList: List<Place>): RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val placeName = view.findViewById<TextView>(R.id.placeName)
        val placeAddress = view.findViewById<TextView>(R.id.placeAddress)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.place_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            val place = placeList[position]
            placeName.text = place.name
            placeAddress.text = place.address
            holder.itemView.setOnClickListener {
                if (fragment.activity is MainActivity) {
                    val intent = Intent(fragment.context, WeatherActivity::class.java).apply {
                        putExtra("location_lng", place.location.lng)
                        putExtra("location_lat", place.location.lat)
                        putExtra("place_name", place.name)
                    }
                    fragment.context?.startActivity(intent)
                    DataStoreUtils.putSyncData("place", Gson().toJson(place))
                    fragment.activity?.finish()
                } else {
                    val weatherActivity = fragment.activity as WeatherActivity
                    weatherActivity.viewModel.lng = place.location.lng
                    weatherActivity.viewModel.lat = place.location.lat
                    weatherActivity.viewModel.placeName = place.name
                    weatherActivity.viewModel.refreshWeather(place.location.lng, place.location.lat)
                    DataStoreUtils.putSyncData("place", Gson().toJson(place))
                    weatherActivity.colseDrawer()
                }
            }
        }
    }

    override fun getItemCount() = placeList.size
}