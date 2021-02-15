package com.tej.note_winmachines_android.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tej.note_winmachines_android.Adapters.NotesAdapter;
import com.tej.note_winmachines_android.R;

public class HomeFragment extends Fragment{
    Button btnAdd;
    TextView txtTitle;
    ImageView imgSearch;
    ImageView imgCross;
    RecyclerView notesRecycler;
    String[] s1 = {"Rock","Roll","Britney","Avril","Camlia","Pickashu","Rndo","Lattu",};
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View rootView = inflater.inflate(R.layout.fragment_first, container,false);
        btnAdd = rootView.findViewById(R.id.btnAdd);
        imgSearch = rootView.findViewById(R.id.imgSearch);
        imgCross = rootView.findViewById(R.id.imgCross);
        txtTitle = rootView.findViewById(R.id.txtTitle);
        notesRecycler = rootView.findViewById(R.id.notesRecycler);
        txtTitle.setText(R.string.notes);
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Search",Toast.LENGTH_SHORT).show();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Hello",Toast.LENGTH_SHORT).show();
            }
        });
        NotesAdapter adapter  = new NotesAdapter(getContext(),s1);
        notesRecycler.setAdapter(adapter);
        notesRecycler.setLayoutManager((new LinearLayoutManager(this.getContext())));
        // Inflate the layout for this fragment
        return rootView;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(HomeFragment.this)
                        .navigate(R.id.toNoteDetail);
            }
        });
    }

}