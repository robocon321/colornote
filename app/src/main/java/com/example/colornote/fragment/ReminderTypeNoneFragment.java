package com.example.colornote.fragment;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.example.colornote.R;
import com.example.colornote.activity.MainActivity;
import com.example.colornote.activity.ReminderActivity;
import com.example.colornote.activity.Text_Activity;
import com.example.colornote.dao.CheckListDAO;
import com.example.colornote.dao.ReminderDAO;
import com.example.colornote.dao.TextDAO;
import com.example.colornote.model.CheckList;
import com.example.colornote.model.Reminder;
import com.example.colornote.model.Task;
import com.example.colornote.model.Text;
import com.example.colornote.util.Constant;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ReminderTypeNoneFragment extends Fragment {
    Spinner spType;
    Button btnToday, btnPin, btn15Minute, btn30Minute;
    int current = 0;
    private String CHANNEL_ID = "AABB";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reminder_type_none, container, false);
        init(view);
        setEvents();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void init(View view){
        spType = view.findViewById(R.id.spType);
        btnToday = view.findViewById(R.id.btnToday);
        btnPin = view.findViewById(R.id.btnPin);
        btn15Minute = view.findViewById(R.id.btn15Minute);
        btn30Minute = view.findViewById(R.id.btn30Minute);

        Intent intent = getActivity().getIntent();
        ArrayList<String> types = new ArrayList<>();
        types.add("None");
        types.add("All day");
        types.add("Time alarm");
        types.add("Pin to status bar");
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.item_spinner_reminder, types);
        spType.setAdapter(adapter);
        spType.setSelection(current);
    }

    public void setEvents(){
        spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != current) ((ReminderActivity) getActivity()).switchFragment(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNotification();
                insertReminder();
            }
        });

        btnToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ReminderActivity) getActivity()).cal = Calendar.getInstance();
                ((ReminderActivity) getActivity()).switchFragment(1);
            }
        });

        btn15Minute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = ((ReminderActivity) getActivity()).cal;
                cal.setTimeInMillis(cal.getTimeInMillis()+ 15 * Constant.TIME_TO_MILL.MINUTE);
                ((ReminderActivity) getActivity()).switchFragment(2);
            }
        });

        btn30Minute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = ((ReminderActivity) getActivity()).cal;
                cal.setTimeInMillis(cal.getTimeInMillis()+ 30 * Constant.TIME_TO_MILL.MINUTE);
                ((ReminderActivity) getActivity()).switchFragment(2);
            }
        });

    }

    public void createNotification(){
        Intent intentText = new Intent(getActivity(), Text_Activity.class);
        intentText.putExtra("task", ((ReminderActivity) getActivity()).task);
        intentText.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intentText, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getActivity(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_text)
                .setContentTitle(((ReminderActivity) getActivity()).task.getTitle())
                .setContentText(((ReminderActivity) getActivity()).task.showContent())
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            notificationManager.notify(((ReminderActivity) getActivity()).task.getId(), mBuilder.build());
        }else{
            NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(((ReminderActivity) getActivity()).task.getId(), mBuilder.build());
        }
    }

    public void insertReminder(){
        Reminder reminder = ((ReminderActivity) getActivity()).reminder;
        reminder.setType(3);
        reminder.setStartDate(new Date());

        if( ((ReminderActivity) getActivity()).task.getReminderId() == 0){
            int idReminder = (int) ReminderDAO.getInstance().insert(reminder);
        }else {
            ReminderDAO.getInstance().update(reminder);
        }


        if(((ReminderActivity) getActivity()).task.getClass().equals(Text.class)){
            Text text = (Text) ((ReminderActivity) getActivity()).task;
            TextDAO.getInstance().update(text);
        }
        if(((ReminderActivity) getActivity()).task.getClass().equals(CheckList.class)){
            CheckList checkList = (CheckList) ((ReminderActivity) getActivity()).task;
            CheckListDAO.getInstance().update(checkList);
        }

        getActivity().onBackPressed();
    }
}
