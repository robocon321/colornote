package com.example.colornote.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.colornote.R;
import com.example.colornote.fragment.ReminderTypeAllDayFragment;
import com.example.colornote.fragment.ReminderTypeNoneFragment;
import com.example.colornote.fragment.ReminderTypePinFragment;
import com.example.colornote.fragment.ReminderTypeTimeAlarmFragment;

public class ReminderActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        switchFragment(0);
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