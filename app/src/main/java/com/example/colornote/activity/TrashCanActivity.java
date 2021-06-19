package com.example.colornote.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.colornote.R;

public class TrashCanActivity extends AppCompatActivity {
ImageButton btnBackTrashCan,btnTrashCanTrashCan;
Button btnSort_TrashCan;
    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash_can);
        addControls();
        addEvents(this);
    }

    private void addEvents(Activity activity) {
        btnBackTrashCan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        builder = new AlertDialog.Builder(activity);
        btnTrashCanTrashCan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                builder.setTitle("Delete").setMessage("Are you sure you want to delete all notes in the trash can?").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        })
                        .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        btnSort_TrashCan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TrashCanActivity.this);
                LayoutInflater inflater = TrashCanActivity.this.getLayoutInflater();
                View view =inflater.inflate(R.layout.dialog_sort_trashcan,null);
                builder.setView(view).setTitle("Sort");
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void addControls() {
        btnBackTrashCan = (ImageButton) findViewById(R.id.btnBackTrashCan);
        btnTrashCanTrashCan = (ImageButton) findViewById(R.id.btnTrashCanTrashCan);
        btnSort_TrashCan = (Button) findViewById(R.id.btnSort_TrashCan);
    }

}