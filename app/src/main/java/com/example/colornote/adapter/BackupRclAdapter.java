package com.example.colornote.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.colornote.R;
import com.example.colornote.dao.CheckListDAO;
import com.example.colornote.dao.TextDAO;
import com.example.colornote.database.Database;
import com.example.colornote.model.BackupInfo;
import com.example.colornote.util.DateConvert;

import java.io.File;
import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class BackupRclAdapter extends RecyclerView.Adapter<BackupRclAdapter.ViewHolder> {
    Context context;
    ArrayList<BackupInfo> infos;

    public BackupRclAdapter(Context context, ArrayList<BackupInfo> infos){
        this.context = context;
        this.infos = infos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view= inflater.inflate(R.layout.item_backup, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BackupInfo info = infos.get(position);
        holder.txtTimeBackup.setText(new DateConvert(info.getDate()).showTime());
        if(info.isType()){
            holder.txtTypeBackup.setText("Auto backup");
            holder.txtTypeBackup.setTextColor(Color.parseColor("#FAFAFA"));
        }else{
            holder.txtTypeBackup.setText("Manual backup");
            holder.txtTypeBackup.setTextColor(Color.parseColor("#FFC107"));
        }

        Database.getInstance().setSqLiteDatabase(info.getPath());
        int count = TextDAO.getInstance().count() + CheckListDAO.getInstance().count();
        holder.txtCountBackup.setText(count+"");

    }

    @Override
    public int getItemCount() {
        return infos.size();
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
                            Toast.makeText(context, "Item 1 click", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    });

                    popupBackup.getMenu().getItem(2).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            Toast.makeText(context, "Item 2 click", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    });

                    popupBackup.getMenu().getItem(3).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
                            builder.setTitle("Delete");
                            builder.setMessage("Are your sure you want to remove the backed up data?");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    File file = new File(infos.get(getAdapterPosition()).getPath());
                                    file.delete();
                                    infos.remove(getAdapterPosition());
                                    notifyDataSetChanged();
                                }
                            });
                            builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
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
                            Toast.makeText(context, "Item 4 click", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    });

                    popupBackup.show();
                    return true;
                }
            });
        }
    }
}
