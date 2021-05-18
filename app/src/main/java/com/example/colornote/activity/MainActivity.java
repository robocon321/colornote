package com.example.colornote.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.colornote.R;
import com.example.colornote.adapter.MainPagerAdapter;
import com.example.colornote.database.Database;
import com.example.colornote.fragment.DialogSortFragment;
import com.example.colornote.util.Settings;
import com.google.android.material.bottomnavigation.BottomNavigationView;


// sưa version1

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    ViewPager viewPager;
    MainPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setEvents();
        // Hello world
    }

    public void init(){
        Database.getInstance().createDatabase("database.sqlite", this);
        Settings.getInstance().setSharedPreferences(getSharedPreferences("settings", MODE_PRIVATE));

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.getMenu().getItem(2);

        viewPager = findViewById(R.id.viewPagerMain);
        adapter = new MainPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
    }

    public void setEvents(){
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.mnHome:
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.mnCal:
                                viewPager.setCurrentItem(1);
                                break;
                            case R.id.mnSearch:
                                viewPager.setCurrentItem(2);
                                break;
                            default:
                                viewPager.setCurrentItem(3);
                                break;
                        }
                        return false;
                    }
                });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Toast.makeText(MainActivity.this, position+"", Toast.LENGTH_SHORT).show();
                for(int i=0;i<adapter.getCount();i++){
                    bottomNavigationView.getMenu().getItem(i).setChecked(false);
                }

                bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_home_menu, menu);
        return true;
    }
}