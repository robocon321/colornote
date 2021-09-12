package com.example.colornote.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.colornote.R;
import com.example.colornote.customview.CustomCardView;
import com.example.colornote.mapper.ColorMapper;
import com.example.colornote.model.Color;
import com.example.colornote.model.Task;
import com.example.colornote.util.Constant;
import com.example.colornote.util.DateConvert;
import com.example.colornote.util.SelectedObserverService;

import java.util.ArrayList;

public class ViewDetailsAdapter extends ViewAdapter {
    SharedPreferences sharedPreferences;
    String themeName;
    public ViewDetailsAdapter(ArrayList<Task> tasks, Context context){
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
            view = inflater.inflate(R.layout.layout_view_details, parent, false);

            holder.txtTitle = view.findViewById(R.id.txtTitle);
            holder.txtContent = view.findViewById(R.id.txtContent);
            holder.txtTime = view.findViewById(R.id.txtTime);
            holder.imgCheck = view.findViewById(R.id.imgCheck);
            holder.cvTask = view.findViewById(R.id.cvTask);
            holder.colorSub = view.findViewById(R.id.colorSub);

            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }

        holder.txtTitle.setText(task.getTitle());
        holder.txtContent.setText(task.showContent());
        if(task.completeAll()) {
            holder.txtTitle.setPaintFlags(holder.txtTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
          //  holder.txtTitle.setTextColor(android.graphics.Color.parseColor("#737373"));
            holder.imgCheck.setImageResource(R.drawable.ic_check);
        }else{
            holder.txtTitle.setPaintFlags(holder.txtTitle.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
         //   holder.txtTitle.setTextColor(android.graphics.Color.parseColor("#000000"));
            holder.imgCheck.setImageResource(0);
        }
        holder.txtTime.setText(new DateConvert(task.getModifiedDate()).showTime());

        Color color = colorDAO.get(new ColorMapper(), task.getColorId());

        sharedPreferences = context.getSharedPreferences("Theme", Context.MODE_PRIVATE);
        themeName = sharedPreferences.getString("ThemeName", "Default");
        if(themeName.equalsIgnoreCase("Dark")){
            holder.cvTask.setBackgroundColor(android.graphics.Color.parseColor( "#000000"));

        }else{
            holder.cvTask.setBackgroundColor(android.graphics.Color.parseColor(color == null ? Constant.MAIN_COLOR : color.getColorMain()));
        }

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
        TextView txtTitle, txtContent, txtTime;
        ImageView imgCheck;
        CustomCardView cvTask;
        View colorSub;
    }
}
