package com.example.colornote.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.colornote.R;
import com.example.colornote.adapter.MainPagerAdapter;
import com.example.colornote.database.Database;
import com.example.colornote.fragment.HomeFragment;
import com.example.colornote.model.Task;
import com.example.colornote.util.ISeletectedObserver;
import com.example.colornote.util.SelectedObserverService;
import com.example.colornote.util.Settings;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ISeletectedObserver {
    BottomNavigationView bottomNavigationView;
    ViewPager viewPager;
    MainPagerAdapter adapter;
    FloatingActionButton fabAddTask;
    TabLayout tabLayoutOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setEvents();
    }

    public void init(){
        Database.getInstance().createDatabase("database.sqlite", this);
        Settings.getInstance().setSharedPreferences(getSharedPreferences("settings", MODE_PRIVATE));

        fabAddTask = findViewById(R.id.fabAddTask);
        tabLayoutOption = findViewById(R.id.tabLayoutOption);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.getMenu().getItem(2);

        viewPager = findViewById(R.id.viewPagerMain);
        adapter = new MainPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        SelectedObserverService.getInstance().addObserver(this);
    }

    public void setEvents(){
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.mnHome:
                                fabAddTask.setImageResource(R.drawable.ic_add);
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.mnCal:
                                fabAddTask.setImageResource(R.drawable.ic_add);
                                viewPager.setCurrentItem(1);
                                break;
                            case R.id.mnSearch:
                                fabAddTask.setImageResource(R.drawable.ic_close);
                                viewPager.setCurrentItem(2);
                                break;
                            default:
                                fabAddTask.setImageResource(R.drawable.ic_sync);
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
                for(int i=0;i<adapter.getCount();i++){
                    bottomNavigationView.getMenu().getItem(i).setChecked(false);
                }

                bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        fabAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (bottomNavigationView.getSelectedItemId()){
                    case R.id.mnHome:
                        Dialog dialog = new Dialog(MainActivity.this);
                        dialog.setContentView(R.layout.dialog_create_task);
                        Button button_text,button_checklist;
                        button_text = (Button) dialog.findViewById(R.id.btn_textDialog);
                        button_checklist =(Button) dialog.findViewById(R.id.btn_checklistDialog);

                        button_text.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(MainActivity.this,Text_Activity.class);
                                startActivity(intent);
                            }
                        });
                        button_checklist.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(MainActivity.this, CheckList_Activity.class);
                                startActivity(intent);
                            }
                        });
                        dialog.show();

                        break;
                    case R.id.mnCal:
                        break;
                    case R.id.mnSearch:
                        break;
                    case R.id.mnNav:
                        break;
                }
            }
        });
        tabLayoutOption.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 3){
                    onChangeReminderActivitiy();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    public void onChangeReminderActivitiy(){
        boolean[] isSelected = SelectedObserverService.getInstance().getIsSelected();
        if(SelectedObserverService.getInstance().count() == 1) {
            for(int i=0;i<isSelected.length;i++){
                if(isSelected[i]){
                    Intent intent = new Intent(MainActivity.this, ReminderActivity.class);
                    intent.putExtra("task",HomeFragment.tasks.get(i));
                    startActivity(intent);
                    break ;
                }
            }
        }
    }

    @Override
    public void update(boolean[] isSelected) {
        if(fabAddTask.getVisibility() == View.VISIBLE && hasSelected(isSelected)){
            fabAddTask.setVisibility(View.INVISIBLE);
            findViewById(R.id.tabLayoutOption).setVisibility(View.VISIBLE);
        }
        if(fabAddTask.getVisibility() == View.INVISIBLE && !hasSelected(isSelected)){
            fabAddTask.setVisibility(View.VISIBLE);
            findViewById(R.id.tabLayoutOption).setVisibility(View.INVISIBLE);
        }
    }

    public boolean hasSelected(boolean[] isSelected){
        for(int i=0;i<isSelected.length;i++){
            if(isSelected[i]) return true;
        }
        return false;
    }
}