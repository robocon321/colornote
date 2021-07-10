package com.example.colornote.adapter;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.example.colornote.R;
import com.example.colornote.dao.ColorDAO;
import com.example.colornote.fragment.HomeFragment;
import com.example.colornote.mapper.ColorMapper;
import com.example.colornote.model.Color;
import com.example.colornote.model.Task;
import com.example.colornote.util.Constant;
import com.example.colornote.util.DateConvert;
import com.example.colornote.util.ISeletectedObserver;
import com.example.colornote.util.SelectedObserverService;
import com.example.colornote.viewpager.CustomCardView;
import com.example.colornote.viewpager.CustomViewEmpty;

import java.util.ArrayList;
import java.util.logging.Handler;

public class ViewListAdapter extends ViewAdapter {

    public ViewListAdapter(ArrayList<Task> tasks, Context context){
        super(tasks, context);
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
        this.parent = (GridView) parent;

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


        if(task.isComplete()){
            holder.imgCheck.setImageResource(R.drawable.ic_check);
        }else
            if(task.getStatus()==1){
                holder.imgCheck.setImageResource(R.drawable.ic_trash_can);
            }else{
                holder.imgCheck.setImageResource(0);
            }
        holder.txtTime.setText(new DateConvert(task.getModifiedDate()).showTime());

        Color color = colorDAO.get(new ColorMapper(), task.getColorId());
        holder.cvTask.setBackgroundColor(android.graphics.Color.parseColor(color == null ? Constant.MAIN_COLOR : color.getColorMain()));
        holder.colorSub.setBackgroundColor(android.graphics.Color.parseColor(color == null ? Constant.SUB_COLOR : color.getColorSub()));

        ViewHolder finalHolder = holder;

        holder.cvTask.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(SelectedObserverService.getInstance().getIsSelected()[position] == false){
                    SelectedObserverService.getInstance().selected(position, position+1);
                }else{
                    SelectedObserverService.getInstance().unselected(position, position+1);
                }
                updateBorderView();

                return true;
            }
        });

        holder.cvTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SelectedObserverService.getInstance().hasSelected()){
                    if(SelectedObserverService.getInstance().getIsSelected()[position] == false){
                        SelectedObserverService.getInstance().selected(position, position+1);
                    }else{
                        SelectedObserverService.getInstance().unselected(position, position+1);
                    }
                    updateBorderView();
                }
            }
        });

        updateBorderView();

        return view;
    }


    public class ViewHolder{
        TextView txtTitle, txtTime;
        ImageView imgCheck;
        CustomCardView cvTask;
        View colorSub;
    }

}