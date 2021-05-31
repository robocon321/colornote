package com.example.colornote.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.colornote.R;
import com.example.colornote.dao.CheckListDAO;
import com.example.colornote.dao.TextDAO;
import com.example.colornote.database.Database;
import com.example.colornote.model.BackupInfo;
import com.example.colornote.util.DateConvert;

import java.util.ArrayList;

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
        Log.d("HHH", new DateConvert(info.getDate()).showTime());
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
        }
    }
}
