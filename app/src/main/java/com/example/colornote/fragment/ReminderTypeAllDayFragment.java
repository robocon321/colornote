package com.example.colornote.fragment;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.colornote.R;
import com.example.colornote.activity.ReminderActivity;
import com.example.colornote.model.Reminder;
import com.example.colornote.util.Constant;
import com.example.colornote.util.DateConvert;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ReminderTypeAllDayFragment extends Fragment {
    Spinner spType, spRepetition;
    int current = 1;
    ArrayList<String> types, repetitions;
    ArrayAdapter adapterTypes, adapterRepetitions;
    Button btnToday, btnDate;
    Reminder reminder = new Reminder();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reminder_type_all_day, container, false);
        init(view);
        setEvents();
        return view;
    }
    public void init(View view){
        btnToday = view.findViewById(R.id.btnToday);
        btnDate = view.findViewById(R.id.btnDate);

        spType = view.findViewById(R.id.spType);
        types = new ArrayList<>();
        types.add("None");
        types.add("All day");
        types.add("Time alarm");
        types.add("Pin to status bar");
        adapterTypes = new ArrayAdapter(getActivity(), R.layout.item_spinner_reminder, types);
        spType.setAdapter(adapterTypes);
        spType.setSelection(current);

        spRepetition = view.findViewById(R.id.spRepetition);
        repetitions = new ArrayList<>();
        repetitions.add("One-time event");
        repetitions.add("Daily");
        repetitions.add("Every weekday");
        repetitions.add("Weekly");
        repetitions.add("Bi-weekly");
        repetitions.add("Monthly");
        repetitions.add("Yearly");
        adapterRepetitions = new ArrayAdapter(getActivity(), R.layout.item_spinner_reminder, repetitions);
        spRepetition.setAdapter(adapterRepetitions);
        spRepetition.setSelection(0);
    }

    public void setEvents(){
        spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(current != position) ((ReminderActivity) getActivity()).switchFragment(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogTime();
            }
        });
    }

    public void showDialogTime(){
        List<String> dates = new ArrayList<>();
        int today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        int tomorrow = (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) + 1) % 7;
        dates.add("No date");
        dates.add("Today");
        dates.add("Tomorrow");
        if(today != 1 && tomorrow != 1) dates.add("Chủ Nhật") ;
        if(today != 2 && tomorrow != 2) dates.add("Thứ 2") ;
        if(today != 3 && tomorrow != 3) dates.add("Thứ 3") ;
        if(today != 4 && tomorrow != 4) dates.add("Thứ 4") ;
        if(today != 5 && tomorrow != 5) dates.add("Thứ 5") ;
        if(today != 6 && tomorrow != 6) dates.add("Thứ 6") ;
        if(today != 7 && tomorrow != 7) dates.add("Thứ 7") ;
        dates.add("Specific date");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_DARK);
        builder.setTitle("Time");
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.layout_time_reminder, null);
        ListView lvTime = view.findViewById(R.id.lvTime);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.item_listview_reminder, dates);
        lvTime.setAdapter(adapter);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();
        lvTime.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                btnToday.setText(dates.get(position));
                long dateLong = 0;
                if(position == 0){
                    Toast.makeText(getActivity(), "No date", Toast.LENGTH_SHORT).show();
                    dateLong = -1;
                }else if(position == 1){
                    dateLong = Calendar.getInstance().getTimeInMillis();
                }else if(position == 2){
                    dateLong = Calendar.getInstance().getTimeInMillis()+ Constant.DAY;
                }else if(position == 10){
                    Toast.makeText(getActivity(), "Specific date", Toast.LENGTH_SHORT).show();
                }else {
                    position = position - 3;
                    position += (position >= today - 1) ? 2 : 0;
                    int distance = position >= today - 1? position - (today-1) : 7 - ((today-1) - position);

                    dateLong = Calendar.getInstance().getTimeInMillis() + distance * Constant.DAY;
                }

                reminder.setStartDate(new Date(dateLong));
                btnDate.setText(new DateConvert(reminder.getStartDate()).showTime());
                dialog.dismiss();
            }
        });
    }
}
