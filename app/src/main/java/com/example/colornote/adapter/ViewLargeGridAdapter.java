package com.example.colornote.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.colornote.R;
import com.example.colornote.dao.ColorDAO;
import com.example.colornote.mapper.ColorMapper;
import com.example.colornote.model.Color;
import com.example.colornote.model.Task;
import com.example.colornote.util.Constant;

import java.util.ArrayList;

public class ViewLargeGridAdapter extends BaseAdapter {
    ArrayList<Task> tasks;
    Context context;
    ColorDAO colorDAO;

    public ViewLargeGridAdapter(ArrayList<Task> tasks, Context context){
        this.tasks = tasks;
        this.context = context;
        colorDAO = new ColorDAO();
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
            view = inflater.inflate(R.layout.layout_view_large_grid, parent, false);
            holder.txtTitle = view.findViewById(R.id.txtTitle);
            holder.txtContent = view.findViewById(R.id.txtContent);
            holder.imgCheck = view.findViewById(R.id.imgCheck);
            holder.cvTask = view.findViewById(R.id.cvTask);
            holder.colorSub = view.findViewById(R.id.colorSub);

            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }

        holder.txtTitle.setText(task.getTitle());
        holder.txtContent.setText(task.showContent());
        holder.imgCheck.setImageResource(task.isComplete() ? R.drawable.ic_check : 0);

        Color color = colorDAO.get(new ColorMapper(), task.getColorId());
        holder.cvTask.setBackgroundColor(android.graphics.Color.parseColor(color.getColorMain() == null ? Constant.MAIN_COLOR : color.getColorMain()));
        holder.colorSub.setBackgroundColor(android.graphics.Color.parseColor(color.getColorSub() == null ? Constant.SUB_COLOR : color.getColorSub()));

        return view;
    }

    public class ViewHolder{
        TextView txtTitle, txtContent;
        ImageView imgCheck;
        CardView cvTask;
        View colorSub;
    }

}
