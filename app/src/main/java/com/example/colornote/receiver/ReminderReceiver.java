package com.example.colornote.receiver;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.colornote.R;
import com.example.colornote.activity.ReminderActivity;
import com.example.colornote.activity.Text_Activity;
import com.example.colornote.dao.CheckListDAO;
import com.example.colornote.dao.ReminderDAO;
import com.example.colornote.dao.TextDAO;
import com.example.colornote.database.Database;
import com.example.colornote.mapper.TextMapper;
import com.example.colornote.model.CheckList;
import com.example.colornote.model.Reminder;
import com.example.colornote.model.Task;
import com.example.colornote.model.Text;
import com.example.colornote.util.Constant;

import java.io.File;
import java.io.InputStreamReader;
import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

public class ReminderReceiver extends BroadcastReceiver {
    private String CHANNEL_ID = "AABB";
    private Context context;
    private Task task;
    private Reminder reminder;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        Bundle bd = intent.getBundleExtra("bd");
        this.task = (Task) bd.getSerializable("task");
        Database.getInstance().createDatabase("database.sqlite", context);
        createNotification();
        this.reminder = ReminderDAO.getInstance().getReminderById(task.getReminderId());
        alarmNextTime();
    }

    public void alarmNextTime() {
        int countDay = 0;
        switch (reminder.getRepetition()) {
            case 0:
                break;
            case 1:
                addAlarm(reminder.getId(), System.currentTimeMillis() + Constant.TIME_TO_MILL.DAY);
                break;
            case 2:
                if(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 5)
                addAlarm(reminder.getId(), System.currentTimeMillis() + Constant.TIME_TO_MILL.DAY * 3);
                else addAlarm(reminder.getId(),System.currentTimeMillis() + Constant.TIME_TO_MILL.DAY);
                break;
            case 3:
                addAlarm(reminder.getId(), System.currentTimeMillis() + Constant.TIME_TO_MILL.WEEK);
                break;
            case 4:
                if(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) + 1 == reminder.getStartDate().getDay())
                    addAlarm(reminder.getId(), System.currentTimeMillis() + Constant.TIME_TO_MILL.DAY * 2);
                else addAlarm(reminder.getId(), System.currentTimeMillis() + Constant.TIME_TO_MILL.DAY);
                break;
            case 5:
                countDay = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH) - Calendar.getInstance().get(Calendar.DATE);
                countDay += 7 - countDay % 7;
                addAlarm(reminder.getId(), System.currentTimeMillis() + Constant.TIME_TO_MILL.DAY * countDay);
                break;
            case 6:
                countDay = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_YEAR) - Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
                countDay += 7 - countDay % 7;
                addAlarm(reminder.getId(), System.currentTimeMillis() + Constant.TIME_TO_MILL.DAY * countDay);
                break;
        }
        if(reminder.getEndDate() == null || reminder.getEndDate().getTime() <= System.currentTimeMillis()) {
            removeAlarm(reminder.getId());
            ReminderDAO.getInstance().delete(reminder.getId());
            task.setReminderId(0);
            if(task.getClass().equals(Text.class)) TextDAO.getInstance().update((Text) task);
            else CheckListDAO.getInstance().update((CheckList) task);
        }
    }

    public void removeAlarm(int requestCode) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(context, ReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
    }

    public void addAlarm(int requestCode, long start) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(context, ReminderReceiver.class);
        Bundle bd = new Bundle();
        bd.putSerializable("task", task);
        intent.putExtra("bd", bd);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, start, pendingIntent);
    }

    public void createNotification(){
        Intent intentText = new Intent(context, Text_Activity.class);
        intentText.putExtra("task", task);
        intentText.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentText, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_text)
                .setContentTitle(task.getTitle())
                .setContentText(task.showContent())
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            notificationManager.notify(task.getId(), mBuilder.build());
        }else{
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(task.getId(), mBuilder.build());
        }
    }

}
