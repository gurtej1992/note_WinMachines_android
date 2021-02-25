package com.tej.note_winmachines_android.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.tej.note_winmachines_android.Activities.HomeActivity;
import com.tej.note_winmachines_android.Activities.MapsActivity;
import com.tej.note_winmachines_android.Adapters.NotesAdapter;
import com.tej.note_winmachines_android.Adapters.onNoteClicked;
import com.tej.note_winmachines_android.DataLayer.DBAccess;
import com.tej.note_winmachines_android.Model.Note;
import com.tej.note_winmachines_android.R;

import java.io.Serializable;
import java.util.ArrayList;

import io.realm.RealmResults;

public class HomeFragment extends Fragment implements onNoteClicked {
    Button btnAdd,btnMap;
    TextView txtTitle;
    ImageView imgSearch;
    ImageView imgCross;
    RecyclerView notesRecycler;
    NotesAdapter adapter;
    SearchView etsearch;

    Note notes;

    private  static int noteid = 0;

    //RealmResults<Note> notesList = new RealmResults<Note>();


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        View rootView = inflater.inflate(R.layout.fragment_first, container,false);
        btnAdd = rootView.findViewById(R.id.btnAdd);
        btnMap = rootView.findViewById(R.id.btnmap);
        imgSearch = rootView.findViewById(R.id.rightBarButton);
        imgCross = rootView.findViewById(R.id.leftBarButton);
        txtTitle = rootView.findViewById(R.id.toolTitle);
        notesRecycler = rootView.findViewById(R.id.notesRecycler);
        etsearch = rootView.findViewById(R.id.etsearch);
        txtTitle.setText(R.string.notes);

        etsearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String queryString) {
                adapter.getFilter().filter(queryString);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String queryString) {
                adapter.getFilter().filter(queryString);
                return false;
            }
        });


        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getContext(),"Search",Toast.LENGTH_SHORT).show();
                etsearch.setVisibility(View.VISIBLE);
            }
        });
        btnAdd.setOnClickListener(v -> Toast.makeText(getContext(),"Hello",Toast.LENGTH_SHORT).show());

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Note n = new Note();

                Uri location = Uri.parse("geo:"+n.getLatitude() +","+n.getLongitude());

               // Toast.makeText(getContext(),"Map Clicked",Toast.LENGTH_SHORT).show();
              //  Intent i = new Intent(getContext(), MapsActivity.class);
              //  i.put("pos",DBAccess.fetchNotes());
              //  startActivity(i);

//                Intent intent = new Intent(getContext(), MapsActivity.class);
//                intent.setData(location);
//
//                Log.d("HomeFRagment", "onClick: "+location);
//
//                Bundle args = new Bundle();
//               // args.putSerializable("ARRAYLIST",(Serializable));
//                intent.putExtra("BUNDLE",args);
//                startActivity(intent);


            }
        });

        adapter  = new NotesAdapter(getContext(), DBAccess.fetchNotes(),this);
        notesRecycler.setAdapter(adapter);
        notesRecycler.setLayoutManager((new LinearLayoutManager(this.getContext())));
        // Inflate the layout for this fragment
        return rootView;
    }




/*
    private void filter(String text) {

        //RealmResults<Note> filterdata = new ArrayList<Note>();

        for (int i = 0; i < notesList.size(); i++) {

            if (notesList.get(i).getNote_title().toLowerCase().contains(text.toLowerCase()) || notesList.get(i).getNote_title().toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdata.add(notesList.get(i));
            }
        }

        notesAdapter = new NotesAdapter(getContext(), filterdata,this);
        notesRecycler.setAdapter(notesAdapter);

    }
*/


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnAdd.setOnClickListener(v -> NavHostFragment.findNavController(HomeFragment.this)
                .navigate(R.id.toNoteDetail));
    }

    @Override
    public void onClickItem(View view, int item) {
        Toast.makeText(this.getContext(),"Bye at"+item,Toast.LENGTH_SHORT).show();
//        if(isSelectionOn){
//
//        }
//        else{
            Bundle bundle = new Bundle();
            bundle.putString("item",""+ item);
            NavHostFragment.findNavController(HomeFragment.this)
                    .navigate(R.id.toNoteDetail,bundle);
//        }

    }

    @Override
    public void onLongClickItem(View view, int item) {
        Toast.makeText(this.getContext(),"Hello at"+item,Toast.LENGTH_SHORT).show();

    }
}