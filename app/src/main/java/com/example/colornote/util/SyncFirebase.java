package com.example.colornote.util;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.colornote.R;
import com.example.colornote.activity.MainActivity;
import com.example.colornote.activity.Text_Activity;
import com.example.colornote.dao.CheckListDAO;
import com.example.colornote.dao.ItemCheckListDAO;
import com.example.colornote.dao.ReminderDAO;
import com.example.colornote.dao.TextDAO;
import com.example.colornote.mapper.CheckListMapper;
import com.example.colornote.mapper.ItemCheckListMapper;
import com.example.colornote.mapper.ReminderMapper;
import com.example.colornote.mapper.TextMapper;
import com.example.colornote.model.CheckList;
import com.example.colornote.model.ItemCheckList;
import com.example.colornote.model.Reminder;
import com.example.colornote.model.Text;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.List;

public class SyncFirebase {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static SyncFirebase instance = new SyncFirebase();
    private NotificationManager manager;
    private int processSyncLogin = 1;
    private int processSync = 1;
    public static SyncFirebase getInstance() {
        return instance;
    }

    public void login(String accountId, Context context) {
        String chanelId = "100";
        int id = (int) Calendar.getInstance().getTimeInMillis();
        processSyncLogin = 1;
        NotificationCompat.Builder builder = createNotification(context, chanelId, id);
        addChecklistIfNotFoundInPhone(accountId, builder, id);
        addTextIfNotFoundInPhone(accountId, builder, id);
        addItemChecklistIfNotFoundInPhone(accountId, builder, id);
        addReminderIfNotFoundInPhone(accountId, builder, id);
    }

    private void addChecklistIfNotFoundInPhone(String accountId, NotificationCompat.Builder builder, int id) {
        db.collection("checklist")
                .whereEqualTo("accountId", accountId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Long id = document.getLong("id");
                                if (id != null) {
                                    CheckList c = CheckListDAO.getInstance().get(new CheckListMapper(), id.intValue());
                                    if (c == null) {
                                        c = document.toObject(CheckList.class);
                                        CheckListDAO.getInstance().insert(c);
                                    }
                                }
                            }
                        } else {
                            Log.e("TAG", "Error getting documents.", task.getException());
                        }
                        processSyncLogin ++;
                        builder.setProgress(100, processSyncLogin * 20, false);
                        if(processSyncLogin >= 5) {
                            processSyncLogin = 1;
                            builder.setContentText("Completed!");
                        }
                        manager.notify(id, builder.build());
                    }
                });
    }
    private void addTextIfNotFoundInPhone(String accountId, NotificationCompat.Builder builder, int id) {
        db.collection("text")
                .whereEqualTo("accountId", accountId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Long id = document.getLong("id");
                                if (id != null) {
                                    Text t = TextDAO.getInstance().get(new TextMapper(), id.intValue());
                                    if (t == null) {
                                        t = document.toObject(Text.class);
                                        TextDAO.getInstance().insert(t);
                                    }
                                }
                            }
                        } else {
                            Log.e("TAG", "Error getting documents.", task.getException());
                        }
                        processSyncLogin ++;
                        builder.setProgress(100, processSyncLogin * 20, false);
                        if(processSyncLogin >= 5) {
                            processSyncLogin = 1;
                            builder.setContentText("Completed!");
                        }
                        manager.notify(id, builder.build());
                    }
                });
    }
    private void addReminderIfNotFoundInPhone(String accountId, NotificationCompat.Builder builder, int id) {
        db.collection("reminder")
                .whereEqualTo("accountId", accountId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Long id = document.getLong("id");
                                if (id != null) {
                                    Reminder r = ReminderDAO.getInstance().get(new ReminderMapper(), id.intValue());
                                    if (r == null) {
                                        r = document.toObject(Reminder.class);
                                        ReminderDAO.getInstance().insert(r);
                                    }
                                }
                            }
                        } else {
                            Log.e("TAG", "Error getting documents.", task.getException());
                        }
                        processSyncLogin ++;
                        builder.setProgress(100, processSyncLogin * 20, false);
                        if(processSyncLogin >= 5) {
                            processSyncLogin = 1;
                            builder.setContentText("Completed!");
                        }
                        manager.notify(id, builder.build());
                    }
                });
    }
    private void addItemChecklistIfNotFoundInPhone(String accountId, NotificationCompat.Builder builder, int id) {
        db.collection("item-checklist")
                .whereEqualTo("accountId", accountId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Long id = document.getLong("id");
                                if (id != null) {
                                    ItemCheckList i = ItemCheckListDAO.getInstance().get(new ItemCheckListMapper(), id.intValue());

                                    if (i == null) {
                                        i = document.toObject(ItemCheckList.class);
                                        ItemCheckListDAO.getInstance().insert(i);
                                    }
                                }
                            }
                        } else {
                            Log.e("TAG", "Error getting documents.", task.getException());
                        }
                        processSyncLogin ++;
                        builder.setProgress(100, processSyncLogin * 20, false);
                        if(processSyncLogin >= 5) {
                            processSyncLogin = 1;
                            builder.setContentText("Completed!");
                        }
                        manager.notify(id, builder.build());
                    }
                });
    }

    public void sync(String accountId, Context context) {
        String chanelId = "200";
        int id = (int) Calendar.getInstance().getTimeInMillis();
        processSync = 1;
        NotificationCompat.Builder builder = createNotification(context, chanelId, id);
        syncChecklist(accountId, builder, id);
        syncItemChecklist(accountId, builder, id);
        syncText(accountId, builder, id);
        syncReminder(accountId, builder, id);
    }

    private void syncChecklist(String accountId, NotificationCompat.Builder builder, int id) {
        db.collection("checklist")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getString("accountId") != null)
                                if(document.getString("accountId").equals(accountId)) document.getReference().delete();
                            }
                            List<CheckList> list = CheckListDAO.getInstance().getAll(new CheckListMapper());
                            for(CheckList item : list) {
                                item.setAccountId(accountId);
                                db.collection("checklist")
                                        .add(item)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e("TAG", "Error adding document", e);
                                            }
                                        });
                            }
                        } else {
                            Log.e("TAG", "Error getting documents.", task.getException());
                        }
                        processSync ++;
                        builder.setProgress(100, processSync * 20, false);
                        if(processSync >= 5) {
                            processSync = 1;
                            builder.setContentText("Completed!").setOngoing(false);
                        }
                        manager.notify(id, builder.build());
                    }
                });
    }
    private void syncItemChecklist(String accountId, NotificationCompat.Builder builder, int id) {
        db.collection("item-checklist")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getString("accountId") != null)
                                if(document.getString("accountId").equals(accountId)) document.getReference().delete();
                            }
                            List<ItemCheckList> list = ItemCheckListDAO.getInstance().getAll(new ItemCheckListMapper());
                            for(ItemCheckList item : list) {
                                item.setAccountId(accountId);
                                db.collection("item-checklist")
                                        .add(item)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e("TAG", "Error adding document", e);
                                            }
                                        });
                            }
                        } else {
                            Log.e("TAG", "Error getting documents.", task.getException());
                        }
                        processSync ++;
                        builder.setProgress(100, processSync * 20, false);
                        if(processSync >= 5) {
                            processSync = 1;
                            builder.setContentText("Completed!").setOngoing(false);
                        }
                        manager.notify(id, builder.build());
                    }
                });
    }
    private void syncText(String accountId, NotificationCompat.Builder builder, int id) {
        db.collection("text")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getString("accountId") != null)
                                    if(document.getString("accountId").equals(accountId)) document.getReference().delete();
                            }
                            List<Text> list = TextDAO.getInstance().getAll(new TextMapper());
                            for(Text item : list) {
                                item.setAccountId(accountId);
                                db.collection("text")
                                        .add(item)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e("TAG", "Error adding document", e);
                                            }
                                        });
                            }
                        } else {
                            Log.e("TAG", "Error getting documents.", task.getException());
                        }
                        processSync ++;
                        builder.setProgress(100, processSync * 20, false);
                        if(processSync >= 5) {
                            processSync = 1;
                            builder.setContentText("Completed!").setOngoing(false);
                        }
                        manager.notify(id, builder.build());
                    }
                });
    }
    private void syncReminder(String accountId, NotificationCompat.Builder builder, int id) {
        db.collection("reminder")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getString("accountId") != null)
                                    if(document.getString("accountId").equals(accountId)) document.getReference().delete();
                            }
                            List<Reminder> list = ReminderDAO.getInstance().getAll(new ReminderMapper());
                            for(Reminder item : list) {
                                item.setAccountId(accountId);
                                db.collection("reminder")
                                        .add(item)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e("TAG", "Error adding document", e);
                                            }
                                        });
                            }
                        } else {
                            Log.e("TAG", "Error getting documents.", task.getException());
                        }
                        processSync ++;
                        builder.setProgress(100, processSync * 20, false);
                        if(processSync >= 5) {
                            processSync = 1;
                            builder.setContentText("Completed!").setOngoing(false);
                        }
                        manager.notify(id, builder.build());
                    }
                });
    }

    public NotificationCompat.Builder createNotification(Context context, String CHANNEL_ID, int ID){
        Intent intentText = new Intent(context, MainActivity.class);
        intentText.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentText, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.black_sync)
                .setContentTitle("Sync")
                .setContentText("Syncing...")
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setProgress(100, 20, false)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
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
            manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
            manager.notify(ID, mBuilder.build());
        }else{
            manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(ID, mBuilder.build());
        }


        return mBuilder;
    }

}
