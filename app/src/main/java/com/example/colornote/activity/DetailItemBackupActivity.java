package com.example.colornote.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.PagerAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.colornote.R;
import com.example.colornote.adapter.ViewListAdapter;
import com.example.colornote.dao.CheckListDAO;
import com.example.colornote.dao.TextDAO;
import com.example.colornote.database.Database;
import com.example.colornote.mapper.CheckListMapper;
import com.example.colornote.mapper.TextMapper;
import com.example.colornote.model.BackupInfo;
import com.example.colornote.model.CheckList;
import com.example.colornote.model.Task;
import com.example.colornote.util.Constant;
import com.example.colornote.util.DateConvert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class DetailItemBackupActivity extends AppCompatActivity {
    Toolbar toolbarBackup;
    Spinner spTaskBackup, spStatusBackup;
    TextView txtDateBackup;
    ImageView imgTypeBackup;
    GridView gvTaskBackup;
    ArrayAdapter<String> adapterTask, adapterStatus;
    Button btnSortBackup;
    BackupInfo info;
    ArrayList<Task> tasks;
    BaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_item_backup);
        init();
        setEvents();
    }

    public void init(){
        toolbarBackup = findViewById(R.id.toolbarBackup);
        setSupportActionBar(toolbarBackup);

        spTaskBackup = findViewById(R.id.spTaskBackup);
        spStatusBackup = findViewById(R.id.spStatusBackup);
        txtDateBackup = findViewById(R.id.txtDateBackup);
        imgTypeBackup = findViewById(R.id.imgTypeBackup);
        gvTaskBackup = findViewById(R.id.gvTaskBackup);
        btnSortBackup = findViewById(R.id.btnSortBackup);

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
                        Collections.sort(tasks, Task.compareByModifiedTime);
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });

                btnSortColor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Collections.sort(tasks, Task.compareByColor);
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });

                btnSortReminder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Collections.sort(tasks, Task.compareByReminderTime);
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });

                btnSortAlpha.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Collections.sort(tasks, Task.compareByTitle);
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
            }
        });
        spTaskBackup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int type = position;
                int status = spStatusBackup.getSelectedItemPosition();
                filterData(type, status);
            }
        });
        spStatusBackup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int type = spTaskBackup.getSelectedItemPosition();
                int status = position;
                filterData(type, status);
            }
        });
    }

    public void filterData(int type, int status){
        tasks.clear();

    }
}