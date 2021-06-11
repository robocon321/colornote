package com.example.colornote.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.colornote.R;
import com.example.colornote.dao.CheckListDAO;
import com.example.colornote.dao.ReminderDAO;
import com.example.colornote.dao.TextDAO;
import com.example.colornote.fragment.ReminderTypeAllDayFragment;
import com.example.colornote.fragment.ReminderTypeNoneFragment;
import com.example.colornote.fragment.ReminderTypePinFragment;
import com.example.colornote.fragment.ReminderTypeTimeAlarmFragment;
import com.example.colornote.mapper.ReminderMapper;
import com.example.colornote.model.CheckList;
import com.example.colornote.model.Reminder;
import com.example.colornote.model.Task;
import com.example.colornote.model.Text;

import java.util.Calendar;

public class ReminderActivity extends AppCompatActivity {
    public Reminder reminder;
    public Calendar cal = Calendar.getInstance();
    public Task task;
    Button btnClear, btnCancel, btnDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        init();
        setEvents();
    }

    public void init(){
        btnClear = findViewById(R.id.btnClear);
        btnCancel = findViewById(R.id.btnCancel);
        btnDone = findViewById(R.id.btnDone);

        task = (Task) getIntent().getSerializableExtra("task");
        if(task.getReminderId() == 0){
            reminder = new Reminder();
        }else {
            reminder = ReminderDAO.getInstance().get(new ReminderMapper(), task.getReminderId());
        }
        switchFragment(0);
    }

    public void setEvents(){
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(task.getReminderId() != 0){
                    ReminderDAO.getInstance().delete(task.getReminderId());
                    task.setReminderId(0);
                    if(task.getClass().equals(Text.class)){
                        TextDAO.getInstance().update((Text) task);
                    }
                    if(task.getClass().equals(CheckList.class)){
                        CheckListDAO.getInstance().update((CheckList) task);
                    }
                }

                backToMainActivity();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMainActivity();
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reminder.getType() != 0) {
                    if (task.getReminderId() == 0) {
                        int idReminder = (int) ReminderDAO.getInstance().insert(reminder);
                        task.setReminderId(idReminder);
                        if (task.getClass().equals(Text.class)) {
                            TextDAO.getInstance().update((Text) task);
                        }
                        if (task.getClass().equals(CheckList.class)) {
                            CheckListDAO.getInstance().update((CheckList) task);
                        }
                    } else {
                        ReminderDAO.getInstance().update(reminder);
                    }
                }

                backToMainActivity();
            }
        });
    }

    public void backToMainActivity(){
        Intent intent = new Intent(ReminderActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void switchFragment(int position){
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new ReminderTypeNoneFragment();
                break;
            case 1:
                fragment = new ReminderTypeAllDayFragment();
                break;
            case 2:
                fragment = new ReminderTypeTimeAlarmFragment();
                break;
            case 3:
                fragment = new ReminderTypePinFragment();
                break;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.layoutOption, fragment);
        fragmentTransaction.commit();
    }
}