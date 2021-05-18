package com.example.colornote.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.colornote.R;
import com.example.colornote.database.Database;
import com.example.colornote.fragment.DialogSortFragment;
import com.example.colornote.util.Settings;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    Button btnSort;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
        setEvents();
    }

    public void init(){
        Database.getInstance().createDatabase("database.sqlite", this);
        Settings.getInstance().setSharedPreferences(getSharedPreferences("settings", MODE_PRIVATE));

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.getMenu().getItem(2);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnSort = findViewById(R.id.btnSort);
    }

    public void setEvents(){
        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogSortFragment dialog =  new DialogSortFragment();
                dialog.show(getSupportFragmentManager(),"Show");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_home_menu, menu);
        return true;
    }


}