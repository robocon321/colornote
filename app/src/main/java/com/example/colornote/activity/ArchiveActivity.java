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
import android.widget.ImageButton;
import android.widget.Toolbar;

import com.example.colornote.R;
import com.example.colornote.fragment.MoreFragment;

public class ArchiveActivity extends AppCompatActivity {
    ImageButton btnBackArchive, btnNoteArchive, btnColorArchive;

    AlertDialog.Builder builder;
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
    }

    public void addControls() {

        btnBackArchive = (ImageButton) findViewById(R.id.btnBackArchive);
        btnNoteArchive = (ImageButton) findViewById(R.id.btnNoteArchive);
        btnColorArchive = (ImageButton) findViewById(R.id.btnColorArchive);
    }

}