package com.example.colornote.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
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
import com.example.colornote.fragment.MoreFragment;
import com.example.colornote.mapper.ColorMapper;
import com.example.colornote.mapper.ItemCheckListMapper;
import com.example.colornote.mapper.TextMapper;
import com.example.colornote.model.CheckList;
import com.example.colornote.model.ItemCheckList;
import com.example.colornote.model.Task;
import com.example.colornote.model.Text;
import com.example.colornote.util.Constant;
import com.example.colornote.util.ISeletectedObserver;
import com.example.colornote.util.SelectedObserverService;
import com.example.colornote.util.Settings;
import com.example.colornote.util.SyncFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements ISeletectedObserver {
    BottomNavigationView bottomNavigationView;
    ViewPager viewPager;
    MainPagerAdapter adapter;
    FloatingActionButton fabAddTask;
    LinearLayout tabLayoutOption, tabArchive, tabDelete, tabColor, tabReminder, tabMore;
    TextView txtTitle;
    AlertDialog dialog;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences("Theme", Context.MODE_PRIVATE);
        String themeName = sharedPreferences.getString("ThemeName", "Default");
        if(themeName.equalsIgnoreCase("Dark")){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        setEvents();

//        getDefauleActivity();

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
                        Toast.makeText(MainActivity.this, "Cal", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.mnSearch:
                        Toast.makeText(MainActivity.this, "Search", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.mnNav:
                        String accountId = getSharedPreferences("account", MODE_PRIVATE).getString("account_id", "");
                        if(accountId.length() > 0) {
                            if(checkAvailableInternet()){
                                SyncFirebase.getInstance().sync(accountId, MainActivity.this);
                                getSharedPreferences("account", Context.MODE_PRIVATE).edit().putLong("last_sync", Calendar.getInstance().getTimeInMillis()).commit();
                            }
                            else Toast.makeText(MainActivity.this, "Internet không có sẵn", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                            startActivity(intent);
                        }
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

        tabArchive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                archiveTask();
            }
        });

        tabMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(MainActivity.this, view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.home_more_option, popup.getMenu());

                boolean[] isSelected = SelectedObserverService.getInstance().getIsSelected();
                for(int i = 0; i < isSelected.length ; i ++) {
                    if(isSelected[i]) {
                        Task task = HomeFragment.tasks.get(i);
                        String check = task.completeAll() ? "Uncheck" : "Check";
                        popup.getMenu().findItem(R.id.mnCheck).setTitle(check);
                        break;
                    }
                }

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.mnCheck:
                                checkTask();
                                break;
                            case R.id.mnSend:
                                sendTask();
                                break;
                            case R.id.mnLock:
                                Toast.makeText(MainActivity.this, "Lock", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(MainActivity.this, item.getTitle(), Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return true;                    }
                });

                popup.show();
            }
        });
    }

    public void checkTask () {
        boolean[] isSelected = SelectedObserverService.getInstance().getIsSelected();
        for(int i = 0; i < isSelected.length ; i ++) {
            if(isSelected[i]) {
                Task task = HomeFragment.tasks.get(i);
                boolean isCompleted = !task.completeAll();
                if(task.getClass().equals(Text.class)) {
                    TextDAO.getInstance().changeCompleted(task.getId(), isCompleted);
                    HomeFragment.tasks.get(i).setCompleted(isCompleted);
                    HomeFragment.adapter.notifyDataSetChanged();
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    if(!task.completeAll()) {
                        builder.setTitle("Check all items");
                        builder.setMessage("Are you sure you want to check all items?");
                        int finalI1 = i;
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int index) {
                                List<ItemCheckList> items = ItemCheckListDAO.getInstance().getByParentId(task.getId());
                                CheckListDAO.getInstance().changeCompleted(task.getId(), isCompleted);
                                for (ItemCheckList item : items) {
                                    ItemCheckListDAO.getInstance().changeCompleted(item.getId(), isCompleted);
                                }
                                HomeFragment.tasks.get(finalI1).setCompleted(isCompleted);
                                HomeFragment.adapter.notifyDataSetChanged();
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int index) {
                                dialog.dismiss();
                            }
                        });
                    } else {
                        builder.setTitle("Uncheck all items");
                        builder.setMessage("Are you sure you want to uncheck all items?");
                        int finalI = i;
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int index) {
                                List<ItemCheckList> items = ItemCheckListDAO.getInstance().getByParentId(task.getId());
                                CheckListDAO.getInstance().changeCompleted(task.getId(), isCompleted);
                                for (ItemCheckList item : items) {
                                    ItemCheckListDAO.getInstance().changeCompleted(item.getId(), isCompleted);
                                }
                                HomeFragment.tasks.get(finalI).setCompleted(isCompleted);
                                HomeFragment.adapter.notifyDataSetChanged();
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int index) {
                                dialog.dismiss();
                            }
                        });
                    }
                    dialog = builder.create();
                    dialog.show();
                };
                break;
            }
        }
    }

    public void sendTask() {
        boolean[] isSelected = SelectedObserverService.getInstance().getIsSelected();
        for(int i = 0; i < isSelected.length ; i ++) {
            if(isSelected[i]) {
                Task task = HomeFragment.tasks.get(i);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, task.getTitle());
                this.startActivity(Intent.createChooser(intent, "Share tasks"));
                break;
            }
        }
    }

    public void archiveTask() {
        boolean[] isSelected = SelectedObserverService.getInstance().getIsSelected();
        ArrayList<Task> taskRemoves = new ArrayList<>();
        for(int i = 0; i < isSelected.length ; i ++) {
            if(isSelected[i]) {
                Task task = HomeFragment.tasks.get(i);
                taskRemoves.add(task);
                if(task.getClass().equals(Text.class)) TextDAO.getInstance().changeStatus(task.getId(), Constant.STATUS.ARCHIVE);
                else {
                    CheckListDAO.getInstance().changeStatus(task.getId(), Constant.STATUS.ARCHIVE);
                }
            }
        }
        for (Task t : taskRemoves) {
            HomeFragment.tasks.remove(t);
        }
        HomeFragment.adapter.notifyDataSetChanged();
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
        ArrayList<Task> taskRemoves = new ArrayList<>();
        for(int i = 0; i < isSelected.length ; i ++) {
            if(isSelected[i]) {
                Task task = HomeFragment.tasks.get(i);
                taskRemoves.add(task);
                if(task.getClass().equals(Text.class)) TextDAO.getInstance().changeStatus(task.getId(), Constant.STATUS.RECYCLE_BIN);
                else {
                    ItemCheckListDAO.getInstance().changeStatus(task.getId(), Constant.STATUS.RECYCLE_BIN);
                    CheckListDAO.getInstance().changeStatus(task.getId(), Constant.STATUS.RECYCLE_BIN);
                }
            }
        }
        for (Task t : taskRemoves) {
            HomeFragment.tasks.remove(t);
        }
        HomeFragment.adapter.notifyDataSetChanged();
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
         //   ((TextView) tabReminder.getChildAt(1)).setTextColor(Color.parseColor("#000000"));
            ((ImageView) tabReminder.getChildAt(0)).setImageResource(R.drawable.ic_reminder_active);

            tabMore.setEnabled(true);
            ((ImageView) tabMore.getChildAt(0)).setImageResource(R.drawable.ic_option_active);
           // ((TextView) tabMore.getChildAt(1)).setTextColor(Color.parseColor("#000000"));
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

//      public void getDefauleActivity(){
//          sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//          String defaultActivity =sharedPreferences.getString("default_activity","Notes");
//          if(!defaultActivity.equals("Notes")){
//             viewPager.setCurrentItem(1);
//          }
//      }

    public boolean checkAvailableInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        }
        else
            return false;
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