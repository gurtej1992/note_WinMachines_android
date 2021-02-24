package com.tej.note_winmachines_android.Activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.tej.note_winmachines_android.Fragments.MapFragment;
import com.tej.note_winmachines_android.Model.Note;
import com.tej.note_winmachines_android.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmConfiguration;
//430162f7e5f44fcc107bf6e2ee911cadbe2a3aba
public class HomeActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1;
    LocationManager locationManager;
    LocationListener locationListener;
    SharedPreferences sharedpreferences;
    Dialog dialog;
    Button btnnavigate;
    Note notes;


    public Location userLocation;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sharedpreferences = getSharedPreferences("location", Context.MODE_PRIVATE);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = location -> {
            userLocation = location;
//            SharedPreferences.Editor editor = sharedpreferences.edit();
//            editor.putFloat("longitude",(float)location.getLongitude());
//            editor.putFloat("latitude",(float)location.getLatitude());
//            editor.apply();
        };
        if (!hasLocationPermission())
            requestLocationPermission();
        else
            startUpdateLocation();

        btnnavigate = findViewById(R.id.butmap);
        btnnavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new MapFragment(notes));

            }
        });
    }

    private void loadFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.map, fragment);
        fragmentTransaction.disallowAddToBackStack();
        fragmentTransaction.commit();
    }


    private void startUpdateLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    private boolean hasLocationPermission() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (REQUEST_CODE == requestCode) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);
            }
        }
    }

    public void mapdialog(Note note, boolean b) {
        dialog.setContentView(R.layout.maponlong_layout);
        Button btnmap = dialog.findViewById(R.id.btn_navigate);
        ImageView img_move_delete = dialog.findViewById(R.id.img_move_delete);

        btnmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //dialog.show();
                Toast.makeText(HomeActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }
}