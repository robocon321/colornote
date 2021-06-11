package com.example.colornote.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.colornote.R;
import com.example.colornote.activity.ReminderActivity;

import java.util.ArrayList;

public class ReminderTypeTimeAlarmFragment extends Fragment {
    Spinner spType, spRepetition;
    int current = 2;
    ArrayList<String> types, repetitions;
    ArrayAdapter adapterTypes, adapterRepetitions;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reminder_type_time_alarm, container, false);
        init(view);
        setEvents();
        return view;
    }
    public void init(View view){
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
    }
}
