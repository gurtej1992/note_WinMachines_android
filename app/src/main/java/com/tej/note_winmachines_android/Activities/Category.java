package com.tej.note_winmachines_android.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.cazaea.sweetalert.SweetAlertDialog;
import com.tej.note_winmachines_android.Adapters.SubjectsAdapter;
import com.tej.note_winmachines_android.Adapters.onNoteClicked;
import com.tej.note_winmachines_android.DataLayer.DBAccess;
import com.tej.note_winmachines_android.Model.Note;
import com.tej.note_winmachines_android.R;

import io.realm.RealmResults;

public class Category extends AppCompatActivity {
    TextView txtTitle;
    ImageView imgCross;
    Button btnAdd;
    SubjectsAdapter adapter;
    RecyclerView rvSubjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        txtTitle = findViewById(R.id.toolTitle);
        imgCross = findViewById(R.id.leftBarButton);
        btnAdd = findViewById(R.id.btnAdd);
        btnAdd = findViewById(R.id.btnAdd);
        rvSubjects = findViewById(R.id.rvSubjects);
        txtTitle.setText(R.string.subjects);
        setAdapter();

        imgCross.setOnClickListener(view -> {
            finish();
        });
        btnAdd.setOnClickListener(view -> {
            openAddSubjectDialog();
        });
    }

    private void setAdapter() {
        adapter = new SubjectsAdapter(this, DBAccess.fetchSubjects(), new onNoteClicked() {
            @Override
            public void onClickItem(View view, int item) {
                if (getIntent().hasExtra("from")) {
                    Intent intent = new Intent();
                    intent.putExtra("selectedSubjectId", adapter.data.get(item).getSubId());
                    intent.putExtra("selectedSubjectName", adapter.data.get(item).getSubjectName());
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Intent intent = new Intent(Category.this, CategoryNotes.class);
                    intent.putExtra("selectedSubjectId", adapter.data.get(item).getSubId());
                    startActivity(intent);
                }
            }

            @Override
            public void onLongClickItem(View view, int item) {

            }
        });
        rvSubjects.setAdapter(adapter);
    }


    private void openAddSubjectDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_subject);
        Button btAddSubject = dialog.findViewById(R.id.btAddSubject);
        EditText etSubjectName = dialog.findViewById(R.id.etSubjectName);
        ImageView ivDismiss = dialog.findViewById(R.id.ivDismiss);
        ivDismiss.setOnClickListener(view -> dialog.dismiss());
        btAddSubject.setOnClickListener(view -> {
            if (etSubjectName.getText().toString().trim().isEmpty()) {
                Toast.makeText(Category.this, "Please enter subject name", Toast.LENGTH_LONG).show();
            } else {
                saveSubject(etSubjectName.getText().toString().trim(), dialog);
            }
        });

        dialog.show();
    }

    private void saveSubject(String subjectName, Dialog dialog) {
        if( DBAccess.saveSubject(subjectName, this)){
            dialog.dismiss();
            new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Success!!")
                    .setContentText("You successfully added a subject.")
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