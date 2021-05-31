package com.example.colornote.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.colornote.R;
import com.example.colornote.adapter.BackupPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class BackupActivity extends AppCompatActivity {
    TabLayout tabLayoutBackup;
    ViewPager viewPagerBackup;
    BackupPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);
        getSupportActionBar().setTitle("Backup");
        init();
    }

    public void init(){
        tabLayoutBackup = findViewById(R.id.tabLayoutBackup);
        viewPagerBackup = findViewById(R.id.viewPagerBackup);

        adapter = new BackupPagerAdapter(getSupportFragmentManager());
        viewPagerBackup.setAdapter(adapter);
        tabLayoutBackup.setupWithViewPager(viewPagerBackup);
    }
}