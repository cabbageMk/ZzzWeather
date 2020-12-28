package com.zzz.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import com.zzz.weather.ui.place.PlaceFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val beginTransaction = supportFragmentManager.beginTransaction()
        val placeFragment = PlaceFragment()
        beginTransaction.add(R.id.fl_parent, placeFragment).commit()
    }
}