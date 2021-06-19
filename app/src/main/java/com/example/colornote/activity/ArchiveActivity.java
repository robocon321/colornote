package com.example.colornote.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.colornote.R;
import com.example.colornote.fragment.MoreFragment;

public class ArchiveActivity extends AppCompatActivity {
    ImageButton btnBackArchive, btnNoteArchive, btnColorArchive;
    AlertDialog.Builder builder;
    Spinner spnLeft_Archive,spnRight_Archive;
    Button btnSort_Archive;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);
        addControls();
        addEvents(this);
    }

    private void addEvents(Activity activity) {
        btnBackArchive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnNoteArchive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               builder = new AlertDialog.Builder(activity);
                LayoutInflater inflater = activity.getLayoutInflater();
                View view =inflater.inflate(R.layout.dialog_theme,null);
                builder.setView(view).setTitle("Note Type");
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        //spinner
        spnLeft_Archive.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(parent.getContext(), parent.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spnRight_Archive.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(parent.getContext(), parent.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //button sort
        btnSort_Archive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ArchiveActivity.this);
                LayoutInflater inflater = ArchiveActivity.this.getLayoutInflater();
                View view =inflater.inflate(R.layout.dialog_sort_archive,null);
                builder.setView(view).setTitle("Sort");
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    public void addControls() {

        btnBackArchive = (ImageButton) findViewById(R.id.btnBackArchive);
        btnNoteArchive = (ImageButton) findViewById(R.id.btnNoteArchive);
        btnColorArchive = (ImageButton) findViewById(R.id.btnColorArchive);
        spnLeft_Archive = (Spinner) findViewById(R.id.spnLeft_Archive);
        spnRight_Archive = (Spinner) findViewById(R.id.spnRight_Archive);
        btnSort_Archive = (Button) findViewById(R.id.btnSort_Archive);
    }

}