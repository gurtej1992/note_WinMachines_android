package com.tej.note_winmachines_android.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.tej.note_winmachines_android.R;

public class NoteAddFragment extends Fragment {
    TextView txtTitle;
    ImageView imgSearch;
    ImageView imgCross;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_second, container, false);
        imgSearch = rootView.findViewById(R.id.imgSearch);
        imgCross = rootView.findViewById(R.id.imgCross);
        txtTitle = rootView.findViewById(R.id.txtTitle);
        txtTitle.setText(R.string.addNote);
        return rootView;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_second).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(NoteAddFragment.this)
                        .navigate(R.id.toHome);
            }
        });
    }
}