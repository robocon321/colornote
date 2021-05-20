package com.example.colornote.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.colornote.R;
import com.example.colornote.model.Task;

import java.util.ArrayList;

public class ViewDetailsAdapter extends BaseAdapter {
    ArrayList<Task> tasks;
    Context context;

    public ViewDetailsAdapter(ArrayList<Task> tasks, Context context){
        this.tasks = tasks;
        this.context = context;
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public Object getItem(int position) {
        return tasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View  view, ViewGroup parent) {
        ViewHolder holder = null;
        Task task = tasks.get(position);
        if(view == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.layout_view_details, parent, false);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }

        holder.txtTitle.setText(task.getTitle());

        return view;
    }

    public class ViewHolder{
        TextView txtTitle, txtContent, txtTime;
        ImageView imgCheck;
    }
}
