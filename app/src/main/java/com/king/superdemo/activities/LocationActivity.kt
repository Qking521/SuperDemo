package com.king.superdemo.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.king.superdemo.R
import com.king.superdemo.utils.LocationUtil
import java.util.*

class LocationActivity : BaseActivity() {
    var mLocationInfo: TextView? = null
    var mLocationManager: LocationManager? = null
    private var myLocationListener: MyLocationListener? = null
    var mProviders: MutableList<String>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)
        mLocationInfo = findViewById<View>(R.id.location_info) as TextView
        mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        myLocationListener = MyLocationListener()
        if (checkCallingOrSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), 0)
        } else {
            initProviders()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initProviders()
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) ||
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            Toast.makeText(this, "storage should be granted", Toast.LENGTH_SHORT).show()
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
        } else {
            Toast.makeText(this, "settings", Toast.LENGTH_SHORT).show()
        }
    }

    fun initProviders() {
        mProviders = mLocationManager!!.allProviders
        Optional.of(mProviders as MutableList<String>).ifPresent { list: List<String>? -> list!!.stream().forEach { provider: String -> Log.i("wq", "onCreate: provider=$provider") } }
    }

    @SuppressLint("MissingPermission")
    fun gpsLocate(v: View?) {
        if (LocationUtil.isGpsLocationEnabled(this)) {
            if (mProviders!!.contains(LocationManager.GPS_PROVIDER)) {
                mLocationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, myLocationListener)
            }
        } else {
            openSettings()
        }
    }

    @SuppressLint("MissingPermission")
    fun networkLocate(v: View?) {
        if (LocationUtil.isNetworkLocationEnabled(this)) {
            if (mProviders!!.contains(LocationManager.NETWORK_PROVIDER)) {
                mLocationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, myLocationListener)
            }
        }
    }

    fun bestLocate(v: View?) {}
    private fun openSettings() {
        val intent = Intent()
        intent.action = Settings.ACTION_LOCATION_SOURCE_SETTINGS
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        mLocationManager!!.removeUpdates(myLocationListener)
    }

    internal inner class MyLocationListener : LocationListener {
        override fun onLocationChanged(location: Location) {
            mLocationInfo!!.text = "latitude:" + location.latitude + " ,longitude:" + location.longitude
            Log.i("wq", "onLocationChanged: location latitude=" + location.latitude + " ,longitude=" + location.longitude)
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
            Log.i("wq", "onStatusChanged: provider=$provider ,status=$status")
        }

        override fun onProviderEnabled(provider: String) {
            Log.i("wq", "onProviderEnabled: provider=$provider")
        }

        override fun onProviderDisabled(provider: String) {
            Log.i("wq", "onProviderDisabled: provider=$provider")
        }
    }
}