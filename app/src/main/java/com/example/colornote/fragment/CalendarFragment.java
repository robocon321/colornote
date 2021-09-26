package com.example.colornote.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.example.colornote.R;
import com.example.colornote.activity.Calendar_Text_Activity;
import com.example.colornote.activity.Calendar_Checklist_Activity;
import com.example.colornote.activity.Text_Activity;
import com.example.colornote.adapter.ViewCalendarAdapter;
import com.example.colornote.adapter.ViewListAdapter;
import com.example.colornote.dao.CheckListDAO;
import com.example.colornote.dao.TextDAO;
import com.example.colornote.model.Task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarFragment extends Fragment {
    private TextView txtCalendar, txtDate;

    private Dialog dialogAddCalendar, dialogAddTask;
    private Button button_text, button_checklist, btnAdd;

    // send data to activity
    private Bundle bundle;
    private String setDate;

    //  add an icon in calendar when add a note success
    private CalendarView calendarView;
    private List<EventDay> lsEvent;

    //  get all task in day
    private GridView gvTask;
    private ArrayList<Task> lsTask, lsTaskRemider;
    private ViewCalendarAdapter adapterTask;

    private CheckListDAO checkListDAO;
    private TextDAO textDAO;

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
        dialogAddCalendar = new Dialog(getActivity());
        dialogAddCalendar.setContentView(R.layout.dialog_calendar);
        btnAdd = dialogAddCalendar.findViewById(R.id.btnAdd);

        gvTask = dialogAddCalendar.findViewById(R.id.gvTask);
        lsTask = new ArrayList<>();
        adapterTask = new ViewCalendarAdapter(lsTask, getActivity());
        gvTask.setAdapter(adapterTask);

        checkListDAO = CheckListDAO.getInstance();
        textDAO = TextDAO.getInstance();

        dialogAddTask = new Dialog(getActivity());
        dialogAddTask.setContentView(R.layout.dialog_create_task);
        button_text = (Button) dialogAddTask.findViewById(R.id.btn_textDialog);
        button_checklist = (Button) dialogAddTask.findViewById(R.id.btn_checklistDialog);
        bundle = new Bundle();

        calendarView = (CalendarView) view.findViewById(R.id.calCustom);
        lsEvent = new ArrayList<>();
        lsTaskRemider = new ArrayList<>();

        txtDate = (TextView) dialogAddCalendar.findViewById(R.id.txtDate);
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
        setDate = new SimpleDateFormat("yyyy-MM-dd").format(eventDay.getCalendar().getTime());
        getTaskInday();
        txtDate.setText(setDate);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogAddTask();
            }
        });

        dialogAddCalendar.show();
    }

    //   add new task (text or checklist)
    private void openDialogAddTask() {
        Intent intent = new Intent(getActivity(), Calendar_Text_Activity.class);
        bundle.putString("date", setDate);
        intent.putExtra("bundle", bundle);

        button_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });

        button_checklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Calendar_Checklist_Activity.class);
                bundle.putString("date", setDate);
                intent.putExtra("bundle", bundle);
                startActivity(intent);
            }
        });
        // open add task and close add calendar
        dialogAddCalendar.dismiss();
        dialogAddTask.show();
    }

    private void showDate() {
        String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
        Toast.makeText(getActivity(), "Now is: " + timeStamp, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResume() {
        super.onResume();
//        add icon to note
        lsTaskRemider.addAll(textDAO.getCalendarTextNoTrash());
        lsTaskRemider.addAll(checkListDAO.getCalendarCheckListNoTrash());

        for (int i = 0; i < lsTaskRemider.size(); i++) {
            lsEvent.add(new EventDay(parseDateToCalendar(lsTaskRemider.get(i).getModifiedDate()), R.drawable.ic_note));
        }

        calendarView.setEvents(lsEvent);

        getTaskInday();

    }

    private void getTaskInday() {
        if (setDate != null) {
            lsTask.clear();
            lsTask.addAll(checkListDAO.getCalendarTextByDate(convertTimestamp(setDate)));
            lsTask.addAll(textDAO.getCalendarTextByDate(convertTimestamp(setDate)));
            adapterTask.notifyDataSetChanged();
        }
    }

    public Calendar parseDateToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public String convertTimestamp(String date) {
        String result = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dateTemp = sdf.parse(date);
            long time = dateTemp.getTime();
            result += time;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }
}
