package com.zzz.weather.ui.place

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.zzz.weather.DataStoreUtils
import com.zzz.weather.MainActivity
import com.zzz.weather.R
import com.zzz.weather.logic.model.Place
import com.zzz.weather.ui.weather.WeatherActivity

class PlaceFragment : Fragment() {
    private lateinit var root: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PlaceAdapter
    private val viewModel by lazy { ViewModelProvider(this).get(PlaceViewModel::class.java) }

    private lateinit var searchPlaceEdit: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_place, container, false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val data = DataStoreUtils.readStringData("place", "")
        if (activity is MainActivity && data.isNotEmpty()) {
            val place = Gson().fromJson(data, Place::class.java)
            val intent = Intent(context, WeatherActivity::class.java).apply {
                putExtra("location_lng", place.location.lng)
                putExtra("location_lat", place.location.lat)
                putExtra("place_name", place.name)
            }
            context?.startActivity(intent)
            activity?.finish()
            return
        }

        recyclerView = root.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = PlaceAdapter(this, viewModel.placeList)
        recyclerView.setHasFixedSize(
            true
        )
        recyclerView.adapter = adapter

        searchPlaceEdit = root.findViewById(R.id.searchPlaceEdit)
        searchPlaceEdit.addTextChangedListener {
            val toString = it.toString()
            if (toString.isNotEmpty()) {
                viewModel.searchPlaces(toString)
            } else {
                viewModel.placeList.clear()
                adapter.notifyDataSetChanged()
            }
        }

        viewModel.placeLiveData.observe(viewLifecycleOwner) {
            val places = it.getOrNull()
            if (places != null) {
                viewModel.placeList.clear()
                viewModel.placeList.addAll(places)
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(context, "未查询到数据", Toast.LENGTH_SHORT).show()
                it.exceptionOrNull()?.printStackTrace()
            }
        }
    }

}