package com.example.colornote.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.example.colornote.R;
import com.example.colornote.activity.CheckList_Activity;
import com.example.colornote.activity.Text_Activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarFragment extends Fragment {
    private TextView txtCalendar;

    private Button button_text, button_checklist;
    private Dialog dialog;

    private String setDate;
    private Bundle bundle;

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
        txtCalendar = (TextView) view.findViewById(R.id.txtCalendar);

        calendarView = (CalendarView) view.findViewById(R.id.calCustom);
        lsEvent = new ArrayList<>();
    }

    private void addEvents() {
        txtCalendar.setOnClickListener(new View.OnClickListener() {
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
        setDate = new SimpleDateFormat("dd-MM-yyyy").format(eventDay.getCalendar().getTime());

//        add icon
//        lsEvent.add(new EventDay(eventDay.getCalendar(),R.drawable.ic_text));
//        calendarView.setEvents(lsEvent);

//        add task
        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_create_task);

        button_text = (Button) dialog.findViewById(R.id.btn_textDialog);
        button_checklist = (Button) dialog.findViewById(R.id.btn_checklistDialog);

        bundle = new Bundle();

        button_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Text_Activity.class);
                bundle.putString("date", setDate);
                intent.putExtra("bundle", bundle);
                startActivity(intent);
            }
        });

        button_checklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CheckList_Activity.class);
                bundle.putString("date", setDate);
                intent.putExtra("bundle", bundle);
                startActivity(intent);
            }
        });

        dialog.show();
    }

    private void showDate() {
        String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
        Toast.makeText(getActivity(), "Now is: " + timeStamp, Toast.LENGTH_LONG).show();
    }
}
