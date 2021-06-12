package com.example.colornote.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TimePicker;
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

public class ReminderTypeTimeAlarmFragment extends Fragment {
    Spinner spType, spRepetition;
    int current = 2;
    ArrayList<String> types, repetitions;
    ArrayAdapter adapterTypes, adapterRepetitions;
    Button btnDate, btnToday, btnTime, btnEndDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reminder_type_time_alarm, container, false);
        init(view);
        setEvents();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Calendar cal = ((ReminderActivity) getActivity()).cal;
        ((ReminderActivity) getActivity()).reminder.setType(2);
        ((ReminderActivity) getActivity()).reminder.setStartDate(((ReminderActivity) getActivity()).cal.getTime());
        btnToday.setText(new DateConvert(cal.getTime()).getMinute()+"");
        btnDate.setText(new DateConvert(cal.getTime()).getDate());
        showDistanceTime(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DATE),cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE));
    }

    public void init(View view){
        ((ReminderActivity) getActivity()).reminder.setType(2);
        ((ReminderActivity) getActivity()).reminder.setStartDate(((ReminderActivity) getActivity()).cal.getTime());

        btnDate = view.findViewById(R.id.btnDate);
        btnTime = view.findViewById(R.id.btnTime);
        btnToday = view.findViewById(R.id.btnToday);
        btnEndDate = view.findViewById(R.id.btnEndDate);

        btnToday.setText("5 minutes");
        btnDate.setText(new DateConvert(((ReminderActivity) getActivity()).cal.getTime()).getDate());
        btnTime.setText(new DateConvert(((ReminderActivity) getActivity()).cal.getTime()).getTime());

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
                if(current != position)((ReminderActivity) getActivity()).switchFragment(position);
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

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ReminderActivity) getActivity()).cal = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                        AlertDialog.THEME_DEVICE_DEFAULT_DARK,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                showDistanceTime(year, month, dayOfMonth, 0, 0);
                                ((ReminderActivity) getActivity()).cal.set(year, month, dayOfMonth);
                                ((ReminderActivity) getActivity()).reminder.setStartDate(((ReminderActivity) getActivity()).cal.getTime());

                                btnDate.setText(new DateConvert(((ReminderActivity) getActivity()).cal.getTime()).getDate());
                            }
                        },
                        ((ReminderActivity) getActivity()).cal.get(Calendar.YEAR), ((ReminderActivity) getActivity()).cal.get(Calendar.MONTH), ((ReminderActivity) getActivity()).cal.get(Calendar.DATE));
                dialog.show();
            }
        });

        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] strArr = btnTime.getText().toString().split(":");
                int hour = Integer.parseInt(strArr[0]);
                int minute = Integer.parseInt(strArr[1]);
                TimePickerDialog dialog = new TimePickerDialog(getActivity(),
                        AlertDialog.THEME_DEVICE_DEFAULT_DARK, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar cal = ((ReminderActivity) getActivity()).cal;
                        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), hourOfDay, minute);
                        ((ReminderActivity) getActivity()).reminder.setStartDate(cal.getTime());
                        btnTime.setText(hourOfDay+":"+minute);
                        btnDate.setText(new DateConvert(cal.getTime()).getDate());
                        showDistanceTime(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), hourOfDay, minute);
                    }
                }, hour, minute, true);
                dialog.show();
            }
        });

        spRepetition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((ReminderActivity) getActivity()).reminder.setRepetition(position);
                if (position > 0) {
                    btnEndDate.setVisibility(View.VISIBLE);
                    btnEndDate.setEnabled(true);
                } else {
                    ((ReminderActivity) getActivity()).reminder.setEndDate(null);
                    btnEndDate.setVisibility(View.INVISIBLE);
                    btnEndDate.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(((ReminderActivity) getActivity()).reminder.getEndDate() == null ? cal.getTime() : ((ReminderActivity) getActivity()).reminder.getEndDate());
                DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                        AlertDialog.THEME_DEVICE_DEFAULT_DARK,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                cal.set(year, month, dayOfMonth);
                                ((ReminderActivity) getActivity()).reminder.setEndDate(cal.getTime());

                                btnEndDate.setText(new DateConvert(cal.getTime()).getDate());
                            }
                        },
                        cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
                dialog.show();
            }
        });
    }

    public void showDistanceTime(int year, int month, int dayOfMonth, int hour, int minute){
        Calendar cal = Calendar.getInstance();
        int yearDis = cal.get(Calendar.YEAR) - year;
        int monthDis = cal.get(Calendar.MONTH) - month;
        int dayDis = cal.get(Calendar.DATE) - dayOfMonth;
        int hourDis = cal.get(Calendar.HOUR_OF_DAY) - hour;
        int minuteDis = cal.get(Calendar.MINUTE) - minute;

        if(yearDis > 0){
            btnToday.setText(yearDis +" YEAR"+(yearDis == 1 ? "":"S")+" AGO");
        }else if(yearDis < 0){
            btnToday.setText(-yearDis +" YEAR"+(yearDis == -1 ? "":"S")+" LATER");
        }else {
            if(monthDis > 0){
                btnToday.setText(monthDis +" MONTH"+(monthDis == 1 ? "":"S")+" AGO");
            }else if(monthDis < 0){
                btnToday.setText(-monthDis +" MONTH"+(monthDis == -1 ? "":"S")+" LATER");
            }else {
                if(dayDis > 0){
                    btnToday.setText(dayDis +" DAY"+(dayDis == 1 ? "":"S")+" AGO");
                }else if(dayDis < 0){
                    btnToday.setText(-dayDis +" DAY"+(dayDis == -1 ? "":"S")+" LATER");
                }else {
                    if(hourDis > 0){
                        btnToday.setText(hourDis +" HOUR"+(hourDis == 1 ? "":"S")+" AGO");
                    }else if(hourDis < 0){
                        btnToday.setText(-hourDis +" HOUR"+(hourDis == -1 ? "":"S")+" LATER");
                    }else {
                        if(minuteDis > 0){
                            btnToday.setText(minuteDis +" MINUTE"+(minuteDis == 1 ? "":"S")+" AGO");
                        }else if(minuteDis < 0){
                            btnToday.setText(-minuteDis +" MINUTE"+(minuteDis == -1 ? "":"S")+" LATER");
                        }else {
                            btnToday.setText("Now");
                        }
                    }
                }
            }
        }
    }

    public void showDialogTime(){
        List<String> dates = new ArrayList<>();

        dates.add("5 minutes");
        dates.add("10 minutes");
        dates.add("15 minutes");
        dates.add("20 minutes");
        dates.add("25 minutes");
        dates.add("30 minutes");
        dates.add("45 minutes");
        dates.add("1 hour");
        dates.add("2 hours");
        dates.add("3 hours");
        dates.add("6 hours");
        dates.add("12 hours");
        dates.add("24 hours");

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
                Calendar cal = Calendar.getInstance();

                String[] arrTime = dates.get(position).split(" ");
                int value = Integer.parseInt(arrTime[0]);
                boolean isMinute = arrTime[1].equals("minutes");

                if(isMinute){
                    cal.setTimeInMillis(cal.getTimeInMillis() + value * Constant.MINUTE);
                }else {
                    cal.setTimeInMillis(cal.getTimeInMillis() + value * Constant.HOUR);
                }

                ((ReminderActivity) getActivity()).reminder.setStartDate(cal.getTime());
                btnDate.setText(new DateConvert(cal.getTime()).getDate());
                btnTime.setText(new DateConvert(cal.getTime()).getTime());
                btnToday.setText(dates.get(position));

                dialog.dismiss();
            }
        });
    }

}
