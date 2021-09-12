package com.example.colornote.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Paint;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.preference.PreferenceManager;

import com.example.colornote.R;
import com.example.colornote.activity.CheckList_Activity;
import com.example.colornote.activity.Text_Activity;
import com.example.colornote.dao.TextDAO;
import com.example.colornote.mapper.ColorMapper;
import com.example.colornote.model.CheckList;
import com.example.colornote.model.Color;
import com.example.colornote.model.Task;
import com.example.colornote.model.Text;
import com.example.colornote.util.Constant;
import com.example.colornote.util.DateConvert;
import com.example.colornote.util.SelectedObserverService;
import com.example.colornote.customview.CustomCardView;

import java.util.ArrayList;

public class ViewListAdapter extends ViewAdapter {
    SharedPreferences sharedPreferences;
    String themeName;
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
            holder.task_item = view.findViewById(R.id.task_item);
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
//
        SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
    String font_size =pre.getString("font_size","100dp");

    float size=0;
        switch (font_size){
            case "Tiny":     size=context.getResources().getDimensionPixelSize(R.dimen.font_size_tiny);
                break;
            case "Small":size=context.getResources().getDimensionPixelSize(R.dimen.font_size_small);
                break;
            case "Medium": size=context.getResources().getDimensionPixelSize(R.dimen.font_size_medium);
                break;
            case "Large": size=context.getResources().getDimensionPixelSize(R.dimen.font_size_large);
                break;
            case "Huge": size=context.getResources().getDimensionPixelSize(R.dimen.font_size_huge);
                break;
            default: size=context.getResources().getDimensionPixelSize(R.dimen.font_size);
            break;

        }

        holder.txtTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX,size);
//
        if(task.completeAll()){
            holder.imgCheck.setImageResource(R.drawable.ic_check);
            holder.txtTitle.setPaintFlags(holder.txtTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.txtTitle.setTextColor(android.graphics.Color.parseColor("#737373"));
        }else
            holder.txtTitle.setPaintFlags(holder.txtTitle.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
            holder.txtTitle.setTextColor(android.graphics.Color.parseColor("#000000"));

            if(task.getStatus()==Constant.STATUS.RECYCLE_BIN){
                holder.imgCheck.setImageResource(R.drawable.ic_trash_can);
            }else{
                holder.imgCheck.setImageResource(0);
            }

        holder.txtTime.setText(new DateConvert(task.getModifiedDate()).showTime());

        Color color = colorDAO.get(new ColorMapper(), task.getColorId());

        holder.colorSub.setBackgroundColor(android.graphics.Color.parseColor(color == null ? Constant.SUB_COLOR : color.getColorSub()));

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
                }else{
                    Log.e("EE", task.toString());
                if(task.getClass().equals(Text.class)) {
                    int num = task.getId();
                    TextDAO textDAO = TextDAO.getInstance();
                    Text text = new Text();
                    text = textDAO.getText(num);
                    Intent intent = new Intent(context, Text_Activity.class);
                    context.startActivity(intent);

                }
                if(task.getClass().equals(CheckList.class)){
                    Intent intent = new Intent(context, CheckList_Activity.class);
                    context.startActivity(intent);
                    Toast.makeText(context,"âsdasd",Toast.LENGTH_LONG).show();
                }

                }
            }
        });

        updateBorderView();

        return view;
    }

//public void changeSize(){
//    SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
//    String font_size =pre.getString("font_size","100dp");
//    txtTitle.setTextSize(100);
//    float size=0;
////        switch (font_size){
////            case "Tiny":     size=getResources().getDimension(R.dimen.font_size_tiny);
////                break;
////            case "Small":size=getResources().getDimension(R.dimen.font_size_small);
////                break;
////            case "Medium": size=getResources().getDimension(R.dimen.font_size_medium);
////                break;
////            case "Large": size=getResources().getDimension(R.dimen.font_size_large);
////                break;
////            case "Huge": size=getResources().getDimension(R.dimen.font_size_huge);
////                break;
////            default: size=getResources().getDimension(R.dimen.font_size);
////            break;
////
////        }
//    switch (font_size){
//        case "Tiny":     size=12;
//            break;
//        case "Small":size=14;
//            break;
//        case "Medium": size=17;
//            break;
//        case "Large": size=19;
//            break;
//        case "Huge": size=21;
//            break;
//        default: size=40;
//            break;
//
//    }
//}
    public class ViewHolder{
        TextView txtTitle, txtTime;
        ImageView imgCheck;
        CustomCardView cvTask;
        View colorSub;
        LinearLayout task_item;
    }

}