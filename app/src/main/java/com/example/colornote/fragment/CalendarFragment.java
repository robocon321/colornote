package com.example.colornote.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.example.colornote.R;
import com.example.colornote.activity.CheckList_Activity;
import com.example.colornote.activity.MainActivity;
import com.example.colornote.activity.Text_Activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarFragment extends Fragment {
    private ImageButton btnShowDate;

    //  add an icon in calendar when add a note success
    private CalendarView calendarView;
    private List<EventDay> lsEvent;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        addControls(view);
        addEvents();
        return view;
    }

    private void addControls(View view) {
        btnShowDate = (ImageButton) view.findViewById(R.id.btnShowDate);

        calendarView = (CalendarView) view.findViewById(R.id.calCustom);
        lsEvent = new ArrayList<EventDay>();
    }

    private void addEvents() {
        btnShowDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate();
            }
        });

        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                addTask(eventDay);
            }
        });
    }

    private void addTask(EventDay eventDay) {
//        add icon
//        lsEvent.add(new EventDay(eventDay.getCalendar(),R.drawable.ic_calendar));
//        calendarView.setEvents(lsEvent);

//        add task
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_create_task);
        Button button_text, button_checklist;
        button_text = (Button) dialog.findViewById(R.id.btn_textDialog);
        button_checklist = (Button) dialog.findViewById(R.id.btn_checklistDialog);

        button_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Text_Activity.class);
                startActivity(intent);
            }
        });
        button_checklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CheckList_Activity.class);
                startActivity(intent);
            }
        });
        String getDate = new SimpleDateFormat("yyyy-MM-dd").format(eventDay.getCalendar().getTime());
        Toast.makeText(getActivity(), "Toast: " + getDate, Toast.LENGTH_SHORT).show();
//        dialog.show();
    }

    private void showDate() {
        String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
        Toast.makeText(getActivity(), "Now is: " + timeStamp, Toast.LENGTH_LONG).show();
    }
}
