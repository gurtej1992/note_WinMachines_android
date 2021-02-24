package com.tej.note_winmachines_android.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cazaea.sweetalert.SweetAlertDialog;
import com.tej.note_winmachines_android.Adapters.NotesAdapter;
import com.tej.note_winmachines_android.Adapters.onNoteClicked;
import com.tej.note_winmachines_android.DataLayer.DBAccess;
import com.tej.note_winmachines_android.Model.Note;
import com.tej.note_winmachines_android.R;

public class CategoryNotes extends AppCompatActivity {

    Button btnAdd;
    TextView txtTitle;
    ImageView imgSearch;
    ImageView imgCross;
    RecyclerView notesRecycler;
    NotesAdapter adapter;
    Dialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.category_notes);
        btnAdd = findViewById(R.id.btnAdd);
        imgSearch = findViewById(R.id.rightBarButton);
        imgCross = findViewById(R.id.leftBarButton);
        txtTitle = findViewById(R.id.toolTitle);
        notesRecycler = findViewById(R.id.notesRecycler);
        txtTitle.setText(R.string.notes);


        btnAdd.setOnClickListener(v -> Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show());


        setAdapter();
    }


    private void setAdapter() {
        adapter = new NotesAdapter(this, DBAccess.fetchNotesWhereSubId(getIntent().getLongExtra("selectedSubjectId", 0l)), new onNoteClicked() {
            @Override
            public void onClickItem(View view, int item) {

            }

            @Override
            public void onLongClickItem(View view, int item) {
                mapDialog(adapter.getData().get(item), item);
            }
        });

        notesRecycler.setAdapter(adapter);
        notesRecycler.setLayoutManager((new LinearLayoutManager(this)));

    }

    Note note;
    public void mapDialog(Note note2, int pos) {
        note=note2;
        dialog = new Dialog((this));
        dialog.setContentView(R.layout.maponlong_layout);
        Button btnmap = dialog.findViewById(R.id.btn_navigate);
        Button btnMove = dialog.findViewById(R.id.btnMove);
        ImageView img_move_delete = dialog.findViewById(R.id.img_move_delete);

        btnmap.setOnClickListener(v -> {
            Intent intent = new Intent(this, MapsActivity.class);
//            intent.putExtra("note", note);
            intent.putExtra("pos", pos);
            startActivity(intent);
            dialog.dismiss();
        });
        btnMove.setOnClickListener(v -> {

            startActivityForResult(new Intent(CategoryNotes.this, Category.class).putExtra("from", "addNote"), 200);
            dialog.dismiss();
        });
        img_move_delete.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == RESULT_OK) {
//            note.setSubId(data.getLongExtra("selectedSubjectId", -1L));
            DBAccess.updateNote(note.getNote_id(),data.getLongExtra("selectedSubjectId", -1L));
            new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Success!!")
                    .setContentText("You successfully moved a note.")
                    .showCancelButton(true)
                    .setConfirmText("Yes")
                    .setConfirmClickListener(sweetAlertDialog -> {
                        setAdapter();
                        sweetAlertDialog.dismissWithAnimation();
                    })
                    .show();
        }
    }
}