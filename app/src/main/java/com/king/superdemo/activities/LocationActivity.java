package com.king.superdemo.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.king.superdemo.R;
import com.king.superdemo.utils.LocationUtil;

import java.util.List;
import java.util.Optional;

public class LocationActivity extends BaseActivity {

    TextView mLocationInfo;
    LocationManager mLocationManager;
    private MyLocationListener myLocationListener;
    List<String> mProviders;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        mLocationInfo = (TextView) findViewById(R.id.location_info);
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        myLocationListener = new MyLocationListener();
        if (checkCallingOrSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        && checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        } else {
            initProviders();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initProviders();
        }else  if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) ||
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION) ) {
            Toast.makeText(this, "storage should be granted", Toast.LENGTH_SHORT).show();
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        } else {
            Toast.makeText(this, "settings", Toast.LENGTH_SHORT).show();
        }
    }

    public void initProviders() {
        mProviders = mLocationManager.getAllProviders();
        Optional.of(mProviders).ifPresent(list -> list.stream().forEach(provider -> Log.i("wq", "onCreate: provider="+ provider)));
    }

    @SuppressLint("MissingPermission")
    public void gpsLocate(View v) {
        if (LocationUtil.isGpsLocationEnabled(this)) {
            if (mProviders.contains(LocationManager.GPS_PROVIDER)) {
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, myLocationListener);
            }
        } else {
            openSettings();
        }
    }

    @SuppressLint("MissingPermission")
    public void networkLocate(View v) {
        if (LocationUtil.isNetworkLocationEnabled(this)) {
            if (mProviders.contains(LocationManager.NETWORK_PROVIDER)) {
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, myLocationListener);
            }
        }
    }

    public void bestLocate(View v) {
    }

    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationManager.removeUpdates(myLocationListener);
    }

    class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            mLocationInfo.setText("latitude:" + location.getLatitude() + " ,longitude:"+ location.getLongitude());
            Log.i("wq", "onLocationChanged: location latitude="+ location.getLatitude() + " ,longitude="+ location.getLongitude());
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.i("wq", "onStatusChanged: provider="+ provider + " ,status="+ status);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.i("wq", "onProviderEnabled: provider="+ provider);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.i("wq", "onProviderDisabled: provider="+ provider);

        }
    }
}

