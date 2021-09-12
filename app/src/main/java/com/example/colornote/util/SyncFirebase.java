package com.example.colornote.util;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

import java.util.List;

public class SyncFirebase {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static SyncFirebase instance = new SyncFirebase();
    public static SyncFirebase getInstance() {
        return instance;
    }

    public boolean login(String accountId) {
        boolean result = true;

        if(!addChecklistIfNotFoundInPhone(accountId)) result = false;
        if(!addItemChecklistIfNotFoundInPhone(accountId)) result = false;
        if(!addReminderIfNotFoundInPhone(accountId)) result = false;
        if(!addTextIfNotFoundInPhone(accountId)) result = false;

        return result;
    }

    private boolean addChecklistIfNotFoundInPhone(String accountId) {
        final boolean[] result = {false};

        db.collection("checklist")
                .whereEqualTo("accountId", accountId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        result[0] = task.isSuccessful();
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
                    }
                });
        return result[0];
    }
    private boolean addTextIfNotFoundInPhone(String accountId) {
        final boolean[] result = {false};

        db.collection("text")
                .whereEqualTo("accountId", accountId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        result[0] = task.isSuccessful();
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
                    }
                });
        return result[0];
    }
    private boolean addReminderIfNotFoundInPhone(String accountId) {
        final boolean[] result = {false};

        db.collection("reminder")
                .whereEqualTo("accountId", accountId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        result[0] = task.isSuccessful();
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
                    }
                });
        return result[0];

    }
    private boolean addItemChecklistIfNotFoundInPhone(String accountId) {
        final boolean[] result = {false};

        db.collection("item-checklist")
                .whereEqualTo("accountId", accountId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        result[0] = task.isSuccessful();
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
                    }
                });
        return result[0];

    }

    public boolean sync(String accountId) {
        boolean result = true;

        if(!syncChecklist(accountId)) result = false;
        if(!syncItemChecklist(accountId)) result = false;
        if(!syncText(accountId)) result = false;
        if(!syncReminder(accountId)) result = false;

        return result;
    }

    private boolean syncChecklist(String accountId) {
        final boolean[] result = {false};

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
                                                result[0] = true;
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                result[0] = false;
                                                Log.e("TAG", "Error adding document", e);
                                            }
                                        });
                            }
                        } else {
                            Log.e("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
        return result[0];
    }
    private boolean syncItemChecklist(String accountId) {
        final boolean[] result = {false};

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
                                                result[0] = true;
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                result[0] = false;
                                                Log.e("TAG", "Error adding document", e);
                                            }
                                        });
                            }
                        } else {
                            Log.e("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
        return result[0];
    }
    private boolean syncText(String accountId) {
        final boolean[] result = {false};

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
                                                result[0] = true;
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                result[0] = false;
                                                Log.e("TAG", "Error adding document", e);
                                            }
                                        });
                            }
                        } else {
                            Log.e("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
        return result[0];
    }
    private boolean syncReminder(String accountId) {
        final boolean[] result = {false};

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
                                                result[0] = true;
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                result[0] = false;
                                                Log.e("TAG", "Error adding document", e);
                                            }
                                        });
                            }
                        } else {
                            Log.e("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
        return result[0];
    }
}
