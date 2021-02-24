package com.tej.note_winmachines_android.Model;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class LocationUtil {
    public static FusedLocationProviderClient fusedLocationClient;

    public static void getLastLocation(Context context, OnLoc onLoc) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null)
                onLoc.onLoc(location);
        });
    }


}
