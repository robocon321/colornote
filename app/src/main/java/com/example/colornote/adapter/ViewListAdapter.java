package com.example.colornote.adapter;

import android.content.Context;
import android.util.Log;
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
import com.example.colornote.util.DateConvert;

import java.util.ArrayList;

public class ViewListAdapter extends BaseAdapter {
    ArrayList<Task> tasks;
    Context context;
    ColorDAO colorDAO;

    public ViewListAdapter(ArrayList<Task> tasks, Context context){
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
            view = inflater.inflate(R.layout.layout_view_list, parent, false);

            holder.txtTitle = view.findViewById(R.id.txtTitle);
            holder.txtTime = view.findViewById(R.id.txtTime);
            holder.imgCheck = view.findViewById(R.id.imgCheck);
            holder.cvTask = view.findViewById(R.id.cvTask);
            holder.colorSub = view.findViewById(R.id.colorSub);

            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }

        holder.txtTitle.setText(task.getTitle());
        holder.imgCheck.setImageResource(task.isComplete() ? R.drawable.ic_check : 0);
        holder.txtTime.setText(new DateConvert(task.getModifiedDate()).showTime());

        Color color = colorDAO.get(new ColorMapper(), task.getColorId());
        holder.cvTask.setBackgroundColor(android.graphics.Color.parseColor(color.getColorMain() == null ? Constant.MAIN_COLOR : color.getColorMain()));
        holder.colorSub.setBackgroundColor(android.graphics.Color.parseColor(color.getColorSub() == null ? Constant.SUB_COLOR : color.getColorSub()));

        return view;
    }

    public class ViewHolder{
        TextView txtTitle, txtTime;
        ImageView imgCheck;
        CardView cvTask;
        View colorSub;
    }

}
