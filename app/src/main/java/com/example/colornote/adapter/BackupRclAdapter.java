package com.example.colornote.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.colornote.R;
import com.example.colornote.activity.DetailItemBackupActivity;
import com.example.colornote.dao.CheckListDAO;
import com.example.colornote.dao.TextDAO;
import com.example.colornote.database.Database;
import com.example.colornote.mapper.CheckListMapper;
import com.example.colornote.mapper.TextMapper;
import com.example.colornote.model.BackupInfo;
import com.example.colornote.model.CheckList;
import com.example.colornote.model.Task;
import com.example.colornote.util.DateConvert;
import com.example.colornote.util.MD5Hash;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static java.security.AccessController.getContext;

public class BackupRclAdapter extends RecyclerView.Adapter<BackupRclAdapter.ViewHolder> {
    Context context;
    ArrayList<BackupInfo> infos;

    public BackupRclAdapter(Context context, ArrayList<BackupInfo> infos) {
        this.context = context;
        this.infos = infos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_backup, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BackupInfo info = infos.get(position);
        holder.txtTimeBackup.setText(new DateConvert(info.getDate()).showTime());
        if (info.isType()) {
            holder.txtTypeBackup.setText("Auto backup");
            holder.txtTypeBackup.setTextColor(Color.parseColor("#FAFAFA"));
        } else {
            holder.txtTypeBackup.setText("Manual backup");
            holder.txtTypeBackup.setTextColor(Color.parseColor("#FFC107"));
        }

        Database.getInstance().setSqLiteDatabase(info.getPath());
        int count = TextDAO.getInstance().count() + CheckListDAO.getInstance().count();
        holder.txtCountBackup.setText(count + "");

    }

    @Override
    public int getItemCount() {
        return infos.size();
    }

    public void copyFile(String src, String dst) {
        File fileSrc = new File(src);
        File fileDst = new File(dst);
        try {
            fileDst.getParentFile().mkdirs();
            fileDst.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        InputStream inStream = null;
        OutputStream outStream = null;

        try {
            inStream = new FileInputStream(src);
            outStream = new FileOutputStream(dst);

            int length;
            byte[] buffer = new byte[1024];

            // copy the file content in bytes
            while ((length = inStream.read(buffer)) > 0) {
                outStream.write(buffer, 0, length);
            }

            inStream.close();
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String buildPathDatabase(Activity activity) {
        String path;
        if (Build.VERSION.SDK_INT >= 17)
            path = activity.getApplicationInfo().dataDir + "/databases/";
        else
            path = "/data/data" + activity.getPackageName() + "/databases/";
        path += "/" + "database.sqlite";
        return path;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTimeBackup, txtTypeBackup, txtCountBackup;
        ImageView imgTypBackup;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTimeBackup = itemView.findViewById(R.id.txtTimeBackup);
            txtTypeBackup = itemView.findViewById(R.id.txtTypeBackup);
            txtCountBackup = itemView.findViewById(R.id.txtCountBackup);
            imgTypBackup = itemView.findViewById(R.id.imgTypeBackup);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public boolean onLongClick(View v) {
                    ContextThemeWrapper ctw = new ContextThemeWrapper(context, R.style.Theme_PopMenu_Dark);
                    PopupMenu popupBackup = new PopupMenu(ctw, itemView);

                    popupBackup.inflate(R.menu.pop_menu_backup);
                    SpannableString s = new SpannableString(new DateConvert(infos.get(getAdapterPosition()).getDate()).showTime());
                    s.setSpan(new ForegroundColorSpan(Color.parseColor("#4DB6AC")), 0, s.length(), 0);
                    popupBackup.getMenu().getItem(0).setTitle(s);
                    popupBackup.getMenu().getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                            View view = inflater.inflate(R.layout.dialog_backup, null);
                            AlertDialog.Builder builder = new AlertDialog.Builder(((Activity) context), R.style.MyDialogTheme);
                            builder.setView(view);
                            builder.setTitle("Check Password");
                            final EditText edtPwd = view.findViewById(R.id.edtPwd);
                            final EditText edtRePwd = view.findViewById(R.id.edtRePwd);
                            final TextView titleRePwd = view.findViewById(R.id.titleRePwd);

                            edtRePwd.setVisibility(View.INVISIBLE);
                            titleRePwd.setVisibility(View.INVISIBLE);

                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.e("EEE", infos.get(getAdapterPosition()).getPassword());
                                    if(infos.get(getAdapterPosition()).getPassword().equals(MD5Hash.MDA5(edtPwd.getText().toString()))) {
                                        Intent intent = new Intent(context, DetailItemBackupActivity.class);
                                        intent.putExtra("info", infos.get(getAdapterPosition()));
                                        context.startActivity(intent);

                                    } else {
                                        Toast.makeText(ctw, "Password is incorrect", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            AlertDialog dialog = builder.create();
                            dialog.show();

                            return true;
                        }
                    });

                    popupBackup.getMenu().getItem(2).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                            View view = inflater.inflate(R.layout.dialog_backup, null);
                            AlertDialog.Builder builder = new AlertDialog.Builder(((Activity) context), R.style.MyDialogTheme);
                            builder.setView(view);
                            builder.setTitle("Check Password");
                            final EditText edtPwd = view.findViewById(R.id.edtPwd);
                            final EditText edtRePwd = view.findViewById(R.id.edtRePwd);
                            final TextView titleRePwd = view.findViewById(R.id.titleRePwd);

                            edtRePwd.setVisibility(View.INVISIBLE);
                            titleRePwd.setVisibility(View.INVISIBLE);

                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(infos.get(getAdapterPosition()).getPassword().equals(MD5Hash.MDA5(edtPwd.getText().toString()) + ".sqlite")) {
                                        AlertDialog.Builder innnerBuilder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
                                        innnerBuilder.setTitle("Restore");
                                        innnerBuilder.setMessage("Are your sure you want to restore the backed up data?");
                                        innnerBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                String des = buildPathDatabase((Activity) context);
                                                String src = infos.get(getAdapterPosition()).getPath();
                                                copyFile(src, des);
                                                Toast.makeText(ctw, "Restored", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        innnerBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface innerDialog, int which) {
                                                innerDialog.dismiss();
                                            }
                                        });
                                        AlertDialog innerDialog = innnerBuilder.create();
                                        innerDialog.show();

                                    } else {
                                        Toast.makeText(ctw, "Password incorrect", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            AlertDialog dialog = builder.create();
                            dialog.show();

                            return true;
                        }
                    });

                    popupBackup.getMenu().getItem(3).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                            View view = inflater.inflate(R.layout.dialog_backup, null);
                            AlertDialog.Builder builder = new AlertDialog.Builder(((Activity) context), R.style.MyDialogTheme);
                            builder.setView(view);
                            builder.setTitle("Check Password");
                            final EditText edtPwd = view.findViewById(R.id.edtPwd);
                            final EditText edtRePwd = view.findViewById(R.id.edtRePwd);
                            final TextView titleRePwd = view.findViewById(R.id.titleRePwd);

                            edtRePwd.setVisibility(View.INVISIBLE);
                            titleRePwd.setVisibility(View.INVISIBLE);

                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(infos.get(getAdapterPosition()).getPassword().equals(MD5Hash.MDA5(edtPwd.getText().toString()) + ".sqlite")) {
                                        AlertDialog.Builder innerBuilder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
                                        innerBuilder.setTitle("Delete");
                                        innerBuilder.setMessage("Are your sure you want to remove the backed up data?");
                                        innerBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                File file = new File(infos.get(getAdapterPosition()).getPath());
                                                file.delete();
                                                infos.remove(getAdapterPosition());
                                                notifyDataSetChanged();
                                            }
                                        });
                                        innerBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface innerDialog, int which) {
                                                innerDialog.dismiss();
                                            }
                                        });
                                        AlertDialog innerDialog = innerBuilder.create();
                                        innerDialog.show();

                                    } else {
                                        Toast.makeText(ctw, "Password incorrect", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            AlertDialog dialog = builder.create();
                            dialog.show();
                            return true;
                        }
                    });

                    popupBackup.getMenu().getItem(4).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                            View view = inflater.inflate(R.layout.dialog_backup, null);
                            AlertDialog.Builder builder = new AlertDialog.Builder(((Activity) context), R.style.MyDialogTheme);
                            builder.setView(view);
                            builder.setTitle("Check Password");
                            final EditText edtPwd = view.findViewById(R.id.edtPwd);
                            final EditText edtRePwd = view.findViewById(R.id.edtRePwd);
                            final TextView titleRePwd = view.findViewById(R.id.titleRePwd);

                            edtRePwd.setVisibility(View.INVISIBLE);
                            titleRePwd.setVisibility(View.INVISIBLE);

                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(infos.get(getAdapterPosition()).getPassword().equals(MD5Hash.MDA5(edtPwd.getText().toString()) + ".sqlite")) {
                                        Database.getInstance().setSqLiteDatabase(infos.get(getAdapterPosition()).getPath());
                                        List<Task> tasks = new ArrayList<>();
                                        tasks.addAll(TextDAO.getInstance().getAll(new TextMapper()));
                                        tasks.addAll(CheckListDAO.getInstance().getAll(new CheckListMapper()));

                                        String data = readData(tasks);

                                        Intent intent = new Intent(Intent.ACTION_SEND);
                                        intent.setType("text/plain");
                                        intent.putExtra(Intent.EXTRA_SUBJECT, data);
                                        context.startActivity(Intent.createChooser(intent, "Share tasks"));

                                    } else {
                                        Toast.makeText(ctw, "Password incorrect", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            AlertDialog dialog = builder.create();
                            dialog.show();

                            return true;
                        }
                    });

                    popupBackup.show();
                    return true;
                }
            });
        }

        public String readData(List<Task> tasks){
            String result = "";
            for(Task task: tasks){
                result += "Task " + task.getTitle()+": \n";
                result += "\t"+task.showContent()+"\n";
            }
            return result;
        }
    }
}