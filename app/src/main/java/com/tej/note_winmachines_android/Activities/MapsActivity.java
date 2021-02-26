package com.tej.note_winmachines_android.Activities;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tej.note_winmachines_android.DataLayer.DBAccess;
import com.tej.note_winmachines_android.Model.Note;
import com.tej.note_winmachines_android.R;

import java.util.ArrayList;

import io.realm.RealmResults;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    public static final String TAG = "MapsActivity";


    private Marker marker1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    private void getIntentData() {
        if (getIntent().hasExtra("allNotes")) {
            RealmResults<Note> realmNotes = DBAccess.fetchNotes();
            for (int i = 0; i < realmNotes.size(); i++) {
                LatLng pos = new LatLng(realmNotes.get(i).getLatitude(), realmNotes.get(i).getLongitude());
                String title = realmNotes.get(i).getNote_title();
                marker1 = mMap.addMarker(new MarkerOptions().position(pos).title(title));
                marker1.setTag(title);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 5));
            }
        }

        if (getIntent().hasExtra("pos")) {
//            Note note = (Note) getIntent().getSerializableExtra("note");
            int pos= getIntent().getIntExtra("pos", -1);
            if(pos!=-1){
                Note note= DBAccess.fetchNotes().get(pos);
                LatLng latLng = new LatLng(note.getLatitude(), note.getLongitude());
                String title = note.getNote_title();
                marker1 = mMap.addMarker(new MarkerOptions().position(latLng).title(title));
                marker1.setTag(title);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
            }


        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        getIntentData();

      /*  for (Marker m : markerList) {
            LatLng latLng = new LatLng(m.getPosition().latitude, m.getPosition().longitude);
            mMap.addMarker(new MarkerOptions().position(latLng));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
        }
*/
    }

}