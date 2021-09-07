package com.example.colornote.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.PagerAdapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.colornote.R;
import com.example.colornote.adapter.ViewAdapter;
import com.example.colornote.adapter.ViewListAdapter;
import com.example.colornote.dao.CheckListDAO;
import com.example.colornote.dao.TextDAO;
import com.example.colornote.database.Database;
import com.example.colornote.fragment.HomeFragment;
import com.example.colornote.mapper.CheckListMapper;
import com.example.colornote.mapper.TextMapper;
import com.example.colornote.model.BackupInfo;
import com.example.colornote.model.CheckList;
import com.example.colornote.model.Task;
import com.example.colornote.model.Text;
import com.example.colornote.util.Constant;
import com.example.colornote.util.DateConvert;
import com.example.colornote.util.ISeletectedObserver;
import com.example.colornote.util.SelectedObserverService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class DetailItemBackupActivity extends AppCompatActivity implements ISeletectedObserver {
    Toolbar toolbarBackup;
    Spinner spTaskBackup, spStatusBackup;
    TextView txtDateBackup;
    GridView gvTaskBackup;
    ArrayAdapter<String> adapterTask, adapterStatus;
    ImageView imgTypeTask, imgRange, imgClose;
    Button btnSortBackup;
    LinearLayout tabRestore;
    RelativeLayout toolbarHidden;
    TextView txtCount;
    BackupInfo info;
    ArrayList<Task> tasks;
    ViewAdapter adapter;
    int typeTask = Constant.TASK_TYPE.ALL_TASK;
    int taskBackup = Constant.BACKUP_TYPE.ALL_TABLE;
    int statusBackup = Constant.BACKUP_STATUS.ALL_STATUS;
    ArrayList<Integer> arrangeFilter = new ArrayList<>();
    int sortBy = Constant.SORT_BY.NO_SORT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_item_backup);
        init();
        setEvents();
    }

    public void init(){
        arrangeFilter.add(0);
        arrangeFilter.add(1);
        arrangeFilter.add(2);
        SelectedObserverService.getInstance().addObserver(this);

        toolbarBackup = findViewById(R.id.toolbarBackup);
        setSupportActionBar(toolbarBackup);
        imgTypeTask = findViewById(R.id.imgTypeTask);

        spTaskBackup = findViewById(R.id.spTaskBackup);
        spStatusBackup = findViewById(R.id.spStatusBackup);
        txtDateBackup = findViewById(R.id.txtDateBackup);
        gvTaskBackup = findViewById(R.id.gvTaskBackup);
        btnSortBackup = findViewById(R.id.btnSortBackup);
        tabRestore = findViewById(R.id.tabRestore);
        toolbarHidden = findViewById(R.id.toolbarHidden);
        imgRange = findViewById(R.id.imgRange);
        txtCount = findViewById(R.id.txtCount);
        imgClose = findViewById(R.id.imgClose);

        List<String> arrTask = new ArrayList<>();
        arrTask.add("All");
        arrTask.add("Notes");
        arrTask.add("Calendar");

        List<String> arrStatus = new ArrayList<>();
        arrStatus.add("All");
        arrStatus.add("Normal");
        arrStatus.add("Archieved");

        adapterTask = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arrTask);
        adapterStatus = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arrStatus);

        spTaskBackup.setAdapter(adapterTask);
        spStatusBackup.setAdapter(adapterStatus);

        info = (BackupInfo) getIntent().getSerializableExtra("info");

        Database.getInstance().setSqLiteDatabase(info.getPath());
        txtDateBackup.setText(new DateConvert(info.getDate()).showTime());
        tasks = new ArrayList<>();
        tasks.addAll(TextDAO.getInstance().getAll(new TextMapper()));
        tasks.addAll(CheckListDAO.getInstance().getAll(new CheckListMapper()));

        adapter = new ViewListAdapter(tasks, this);
        gvTaskBackup.setAdapter(adapter);
    }

    public void setEvents(){
        btnSortBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailItemBackupActivity.this);
                builder.setView(R.layout.fragment_sort_option);
                builder.setTitle("Sort");
                AlertDialog dialog = builder.create();
                dialog.show();

                Button btnSortModified = dialog.findViewById(R.id.btnSortModified);
                Button btnSortAlpha = dialog.findViewById(R.id.btnSortAlpha);
                Button btnSortColor = dialog.findViewById(R.id.btnSortColor);
                Button btnSortReminder = dialog.findViewById(R.id.btnSortReminder);

                btnSortModified.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sortBy = Constant.SORT_BY.MODIFIED_TIME;
                        sortTask();
                        dialog.dismiss();
                    }
                });

                btnSortColor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sortBy = Constant.SORT_BY.COLOR;
                        sortTask();
                        dialog.dismiss();
                    }
                });

                btnSortReminder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sortBy = Constant.SORT_BY.REMINDER;
                        sortTask();
                        dialog.dismiss();
                    }
                });

                btnSortAlpha.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sortBy = Constant.SORT_BY.ALPHABECALLY;
                        sortTask();
                        dialog.dismiss();
                    }
                });
            }
        });
        spStatusBackup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                statusBackup = i;
                changeArrangeFilter(2);
                filter();
                sortTask();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spTaskBackup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                taskBackup = i;
                changeArrangeFilter(1);
                filter();
                sortTask();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        imgTypeTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(DetailItemBackupActivity.this);
                dialog.setContentView(R.layout.layout_task_type);
                dialog.show();
                dialog.findViewById(R.id.btnAllTask).setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(View view) {
                        typeTask = Constant.TASK_TYPE.ALL_TASK;
                        changeArrangeFilter(0);
                        filter();
                        sortTask();
                        dialog.dismiss();
                    }
                });
                dialog.findViewById(R.id.btnTextTask).setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(View view) {
                        typeTask = Constant.TASK_TYPE.TEXT_TASK;
                        changeArrangeFilter(0);
                        filter();
                        sortTask();
                        dialog.dismiss();
                    }
                });
                dialog.findViewById(R.id.btnChecklistTask).setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(View view) {
                        typeTask = Constant.TASK_TYPE.CHECKLIST_TASK;
                        changeArrangeFilter(0);
                        filter();
                        sortTask();
                        dialog.dismiss();
                    }
                });
            }
        });

        tabRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Database.getInstance().createDatabase("database.sqlite", DetailItemBackupActivity.this);

                boolean[] isSelected = SelectedObserverService.getInstance().getIsSelected();

                for(int i = 0; i < isSelected.length ; i ++) {
                    if(isSelected[i]) {
                        Task task = tasks.get(i);
                        if(task.getClass().equals(Text.class)) TextDAO.getInstance().insert((Text) task);
                        else {
                            CheckListDAO.getInstance().insert((CheckList) task);
                        }
                    }
                }

                Database.getInstance().setSqLiteDatabase(info.getPath());
                SelectedObserverService.getInstance().reset();
                adapter.updateBorderView();
            }
        });

        imgRange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectedObserverService.getInstance().selectRange();
                adapter.updateBorderView();
            }
        });

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectedObserverService.getInstance().reset();
                adapter.updateBorderView();
            }
        });

    }

    public void sortTask(){
        switch (sortBy){
            case Constant.SORT_BY.MODIFIED_TIME:
                Collections.sort(tasks, Task.compareByModifiedTime);
                btnSortBackup.setText("Sort by motified time");
                break;
            case Constant.SORT_BY.ALPHABECALLY:
                Collections.sort(tasks, Task.compareByTitle);
                btnSortBackup.setText("Sort by alphabetically");
                break;
            case Constant.SORT_BY.COLOR:
                Collections.sort(tasks, Task.compareByColor);
                btnSortBackup.setText("Sort by color");
                break;
            case Constant.SORT_BY.REMINDER:
                Collections.sort(tasks, Task.compareByReminderTime);
                btnSortBackup.setText("Sort by reminder");
                break;
            default:
                break;
        }
        adapter.notifyDataSetChanged();
    }

    public void changeArrangeFilter(int i) {
        if(arrangeFilter.get(arrangeFilter.size() - 1) != i) {
            arrangeFilter.add(i);
            arrangeFilter.remove(arrangeFilter.indexOf(i));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void filter() {
        tasks.clear();
        if(arrangeFilter.get(0) == 0) {
            if (typeTask == Constant.TASK_TYPE.ALL_TASK) {
                tasks.addAll(TextDAO.getInstance().getAll(new TextMapper()));
                tasks.addAll(CheckListDAO.getInstance().getAll(new CheckListMapper()));
            } else if (typeTask == Constant.TASK_TYPE.TEXT_TASK) {
                tasks.addAll(TextDAO.getInstance().getAll(new TextMapper()));
            } else {
                tasks.addAll(CheckListDAO.getInstance().getAll(new CheckListMapper()));
            }
        } else if (arrangeFilter.get(0) == 1) {
            if(taskBackup == Constant.BACKUP_TYPE.ALL_TABLE) {
                tasks.addAll(TextDAO.getInstance().getAll(new TextMapper()));
                tasks.addAll(CheckListDAO.getInstance().getAll(new CheckListMapper()));
            } else if(taskBackup == Constant.BACKUP_TYPE.CALENDAR_TABLE) {
                tasks.addAll(TextDAO.getInstance().getCalendarText());
                tasks.addAll(CheckListDAO.getInstance().getCalendarCheckList());
            } else {
                tasks.addAll(TextDAO.getInstance().getNoteText());
                tasks.addAll(CheckListDAO.getInstance().getNoteCheckList());
            }
        } else {
            if (statusBackup == Constant.BACKUP_STATUS.ALL_STATUS) {
                tasks.addAll(TextDAO.getInstance().getAll(new TextMapper()));
                tasks.addAll(CheckListDAO.getInstance().getAll(new CheckListMapper()));
            } else if (statusBackup == Constant.BACKUP_STATUS.NORMAL_STATUS) {
                tasks.addAll(TextDAO.getInstance().getByStatus(Constant.STATUS.NORMAL));
                tasks.addAll(CheckListDAO.getInstance().getByStatus(Constant.STATUS.NORMAL));
            } else {
                tasks.addAll(TextDAO.getInstance().getByStatus(Constant.STATUS.ARCHIVE));
                tasks.addAll(CheckListDAO.getInstance().getByStatus(Constant.STATUS.ARCHIVE));
            }
        }

        for (int i = 1; i < arrangeFilter.size() ; i ++) {
            if (arrangeFilter.get(i) == 0) {
                if (typeTask == Constant.TASK_TYPE.ALL_TASK) {
                    // Do nothing
                } else if (typeTask == Constant.TASK_TYPE.TEXT_TASK) {
                    tasks.removeIf(task -> !task.getClass().equals(Text.class));
                } else {
                    tasks.removeIf(task -> !task.getClass().equals(CheckList.class));
                }
            } else if(arrangeFilter.get(i) == 1){
                if(taskBackup == Constant.BACKUP_TYPE.ALL_TABLE) {
                    // Do Nothing
                } else if(taskBackup == Constant.BACKUP_TYPE.CALENDAR_TABLE) {
                    tasks.removeIf(task -> task.getReminderId() == 0);
                } else {
                    tasks.removeIf(task -> task.getReminderId() != 0);
                }
            } else {
                if (statusBackup == Constant.BACKUP_STATUS.ALL_STATUS) {
                    // Do nothing
                } else if (statusBackup == Constant.BACKUP_STATUS.NORMAL_STATUS) {
                    tasks.removeIf(task -> task.getStatus() == Constant.STATUS.ARCHIVE);
                } else {
                    tasks.removeIf(task -> task.getStatus() != Constant.STATUS.ARCHIVE);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SelectedObserverService.getInstance().reset();
        Database.getInstance().createDatabase("database.sqlite", DetailItemBackupActivity.this);
    }

    @Override
    public void update(SelectedObserverService s) {
        if(s.hasSelected()){
            toolbarHidden.setVisibility(View.VISIBLE);
            tabRestore.setVisibility(View.VISIBLE);
        } else {
            toolbarHidden.setVisibility(View.INVISIBLE);
            tabRestore.setVisibility(View.INVISIBLE);
        }

        if(s.hasRange()){
            imgRange.setImageResource(R.drawable.ic_range_active);
            imgRange.setEnabled(true);
        }else{
            imgRange.setImageResource(R.drawable.ic_range_nonactive);
            imgRange.setEnabled(false);
        }

        txtCount.setText(s.getRatio());
    }
}