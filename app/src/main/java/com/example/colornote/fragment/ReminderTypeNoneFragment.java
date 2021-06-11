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
import com.example.colornote.activity.ReminderActivity;
import com.example.colornote.activity.Text_Activity;
import com.example.colornote.dao.CheckListDAO;
import com.example.colornote.dao.ReminderDAO;
import com.example.colornote.dao.TextDAO;
import com.example.colornote.model.CheckList;
import com.example.colornote.model.Reminder;
import com.example.colornote.model.Task;
import com.example.colornote.model.Text;

import java.util.ArrayList;
import java.util.Date;

public class ReminderTypeNoneFragment extends Fragment {
    Spinner spType;
    Button btnToday, btnPin, btn15Minute, btn30Minute;
    int current = 0;
    Task task;
    private String CHANNEL_ID = "AABB";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reminder_type_none, container, false);
        init(view);
        setEvents();
        return view;
    }
    public void init(View view){
        spType = view.findViewById(R.id.spType);
        btnToday = view.findViewById(R.id.btnToday);
        btnPin = view.findViewById(R.id.btnPin);
        btn15Minute = view.findViewById(R.id.btn15Minute);
        btn30Minute = view.findViewById(R.id.btn30Minute);

        Intent intent = getActivity().getIntent();
        task = (Task) intent.getSerializableExtra("task");

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
    }

    public void createNotification(){
        Intent intentText = new Intent(getActivity(), Text_Activity.class);
        intentText.putExtra("task", task);
        intentText.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intentText, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getActivity(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_text)
                .setContentTitle(task.getTitle())
                .setContentText(task.showContent())
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
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
            notificationManager.notify(100, mBuilder.build());
        }else{
            NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(100, mBuilder.build());
        }
    }

    public void insertReminder(){
        Reminder reminder = ((ReminderActivity) getActivity()).reminder;
        reminder.setType(3);
        reminder.setStartDate(new Date());
        int idReminder = (int) ReminderDAO.getInstance().insert(reminder);

        task.setReminderId(idReminder);

        if(task.getClass().equals(Text.class)){
            Text text = (Text) task;
            TextDAO.getInstance().update(text);
        }
        if(task.getClass().equals(CheckList.class)){
            CheckList checkList = (CheckList) task;
            CheckListDAO.getInstance().update(checkList);
        }
    }
}
