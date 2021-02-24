package com.tej.note_winmachines_android.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tej.note_winmachines_android.Activities.MapsActivity;
import com.tej.note_winmachines_android.Adapters.NotesAdapter;
import com.tej.note_winmachines_android.Adapters.onNoteClicked;
import com.tej.note_winmachines_android.DataLayer.DBAccess;
import com.tej.note_winmachines_android.Model.Note;
import com.tej.note_winmachines_android.R;

import java.util.ArrayList;

public class MapFragment extends Fragment implements OnMapReadyCallback {


    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Note notes;
    GoogleApiClient mGoogleApiClient;
    Marker mCurrentMarker;

    private Double lat , lng;

    private ArrayList<Marker> markers = new ArrayList<>();

    public MapFragment(Note notes)
    {
        this.notes = notes;
    }

    LatLng currentLocation;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        locationRequest = new LocationRequest();
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(3000);
        locationRequest.setSmallestDisplacement(20);
        locationRequest.setFastestInterval(2000);

        getLocation();

    }

    private void getLocation(){

        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for(Location location : locationResult.getLocations()){

                    setHomeMarker(location);

                }
            }
        };

    }

    private void setHomeMarker(Location location){

        Marker homeMarker;
        LatLng userlocation = new LatLng(location.getLatitude() , location.getLongitude());

        currentLocation = userlocation;

        MarkerOptions options = new MarkerOptions()
                .position(userlocation).title("Your location");


        homeMarker = mMap.addMarker(options);
        markers.add(homeMarker);
        cameraFocus(userlocation);

        if (markers.size() > 1){
            for (Marker marker:markers){
                marker.remove();
            }markers.clear();

        }
        homeMarker = mMap.addMarker(options);
        markers.add(homeMarker);

    }
    private void cameraFocus(LatLng latLng){

        CameraPosition position = CameraPosition.builder()
                .target(latLng)
                .zoom(14)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        // mMap.getUiSettings().setZoomControlsEnabled(true);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());


        } else {
            requestPermissions( new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        }

        lat = notes.getLatitude();
        lng = notes.getLongitude();

        LatLng latLng = new LatLng(lat , lng);
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));


        mMap.addMarker(options);

    }


    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
            }
        } else {
            requestPermissions( new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        }
    }

}