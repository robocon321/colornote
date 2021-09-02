package com.example.colornote.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.colornote.R;
import com.example.colornote.adapter.MainPagerAdapter;
import com.example.colornote.dao.CheckListDAO;
import com.example.colornote.dao.ColorDAO;
import com.example.colornote.dao.ItemCheckListDAO;
import com.example.colornote.dao.TextDAO;
import com.example.colornote.database.Database;
import com.example.colornote.fragment.HomeFragment;
import com.example.colornote.mapper.ColorMapper;
import com.example.colornote.model.CheckList;
import com.example.colornote.model.Task;
import com.example.colornote.model.Text;
import com.example.colornote.util.ColorTransparentUtils;
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
    LinearLayout tabLayoutOption, tabArchive, tabDelete, tabColor, tabReminder, tabMore;
    TextView txtTitle;
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
        tabArchive = findViewById(R.id.tabArchive);
        tabDelete = findViewById(R.id.tabDelete);
        tabColor = findViewById(R.id.tabColor);
        tabReminder = findViewById(R.id.tabReminder);
        tabMore = findViewById(R.id.tabMore);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.getMenu().getItem(2);

        viewPager = findViewById(R.id.viewPagerMain);
        adapter = new MainPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);

        txtTitle = findViewById(R.id.txtTitle);
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
                SelectedObserverService.getInstance().reset();
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

        tabReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeReminderActivitiy();
            }
        });

        tabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteTask();
            }
        });

        tabColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeColorTask();
            }
        });
    }

    public void changeColorTask() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.layout_choose_color);
        dialog.show();
        GridLayout gvColor = dialog.findViewById(R.id.glColor);
        List<com.example.colornote.model.Color> colors = ColorDAO.getInstance().getAll(new ColorMapper());
        colors.remove(0);
        for(com.example.colornote.model.Color c: colors) {
            Button btn = new Button(this);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
                params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            }
            btn.setLayoutParams(params);
            btn.setBackgroundColor(Color.parseColor(c.getColorMain()));
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean[] isSelected = SelectedObserverService.getInstance().getIsSelected();
                    for(int i = 0; i < isSelected.length ; i ++) {
                        if(isSelected[i]) {
                            Task task = HomeFragment.tasks.get(i);
                            task.setColorId(c.getId());
                            if(task.getClass().equals(Text.class)) TextDAO.getInstance().update((Text) task);
                            else {
                                CheckListDAO.getInstance().update((CheckList) task);
                            }
                        }
                    }
                    HomeFragment.loadTask();
                    dialog.cancel();
                }
            });
            gvColor.addView(btn);
        }
    }

    public void deleteTask() {
        boolean[] isSelected = SelectedObserverService.getInstance().getIsSelected();
        for(int i = 0; i < isSelected.length ; i ++) {
            if(isSelected[i]) {
                Task task = HomeFragment.tasks.get(i);
                if(task.getClass().equals(Text.class)) TextDAO.getInstance().delete(task.getId());
                else {
                    ItemCheckListDAO.getInstance().deleteByIdCheckList(task.getId());
                    CheckListDAO.getInstance().delete(task.getId());
                }
            }
        }
        HomeFragment.loadTask();
    }

    public void changeReminderActivitiy(){
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void update(SelectedObserverService s) {
        if(fabAddTask.getVisibility() == View.VISIBLE && s.hasSelected()){
            fabAddTask.setVisibility(View.INVISIBLE);
            bottomNavigationView.setVisibility(View.INVISIBLE);
            tabLayoutOption.setVisibility(View.VISIBLE);
        }
        if(fabAddTask.getVisibility() == View.INVISIBLE && !s.hasSelected()){
            fabAddTask.setVisibility(View.VISIBLE);
            bottomNavigationView.setVisibility(View.VISIBLE);
            tabLayoutOption.setVisibility(View.INVISIBLE);
        }

        if(s.count() != 1){
            tabReminder.setEnabled(false);
            ((TextView) tabReminder.getChildAt(1)).setTextColor(Color.parseColor("#757575"));
            ((ImageView) tabReminder.getChildAt(0)).setImageResource(R.drawable.ic_reminder_nonactive);

            tabMore.setEnabled(false);
            ((ImageView) tabMore.getChildAt(0)).setImageResource(R.drawable.ic_option_nonactive);
            ((TextView) tabMore.getChildAt(1)).setTextColor(Color.parseColor("#757575"));
        }else{
            tabReminder.setEnabled(true);
            ((TextView) tabReminder.getChildAt(1)).setTextColor(Color.parseColor("#000000"));
            ((ImageView) tabReminder.getChildAt(0)).setImageResource(R.drawable.ic_reminder_active);

            tabMore.setEnabled(true);
            ((ImageView) tabMore.getChildAt(0)).setImageResource(R.drawable.ic_option_active);
            ((TextView) tabMore.getChildAt(1)).setTextColor(Color.parseColor("#000000"));
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SelectedObserverService.getInstance().removeObserver(this);
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
//        String font_size =pre.getString("font_size","100dp");
//        txtTitle.setTextSize(100);
//        float size=0;
////        switch (font_size){
////            case "Tiny":     size=getResources().getDimension(R.dimen.font_size_tiny);
////                break;
////            case "Small":size=getResources().getDimension(R.dimen.font_size_small);
////                break;
////            case "Medium": size=getResources().getDimension(R.dimen.font_size_medium);
////                break;
////            case "Large": size=getResources().getDimension(R.dimen.font_size_large);
////                break;
////            case "Huge": size=getResources().getDimension(R.dimen.font_size_huge);
////                break;
////            default: size=getResources().getDimension(R.dimen.font_size);
////            break;
////
////        }
//        switch (font_size){
//            case "Tiny":     size=12;
//                break;
//            case "Small":size=14;
//                break;
//            case "Medium": size=17;
//                break;
//            case "Large": size=19;
//                break;
//            case "Huge": size=21;
//                break;
//            default: size=40;
//                break;
//
//        }
//
//
//    }
}