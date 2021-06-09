package com.example.colornote.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.example.colornote.R;
import com.example.colornote.dao.ColorDAO;
import com.example.colornote.model.Task;
import com.example.colornote.util.SelectedObserverService;
import com.example.colornote.viewpager.CustomCardView;
import com.example.colornote.viewpager.CustomViewEmpty;

import java.util.ArrayList;

public abstract class ViewAdapter extends BaseAdapter {
    ArrayList<Task> tasks;
    Context context;
    ColorDAO colorDAO;
    GridView parent;

    public ViewAdapter(ArrayList<Task> tasks, Context context){
        this.tasks = tasks;
        this.context = context;
        colorDAO = ColorDAO.getInstance();
    }
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        SelectedObserverService.getInstance().setSelected(new boolean[tasks.size()]);
    }

    public void changeBorder(View v, boolean isSelected){
        if(isSelected){
            ((CustomCardView) v.findViewById(R.id.cvTask)).addBorder();
            ((CustomViewEmpty) v.findViewById(R.id.colorSub)).addBorder();
        }else{
            ((CustomCardView) v.findViewById(R.id.cvTask)).removeBorder();
            ((CustomViewEmpty) v.findViewById(R.id.colorSub)).removeBorder();
        }
    }

    public void updateBorderView(){
        boolean[] isSelected = SelectedObserverService.getInstance().getIsSelected();
        for (int i=0;i<isSelected.length;i++){
            if(i<=parent.getLastVisiblePosition() && i >= parent.getFirstVisiblePosition()) {
                View view = parent.getChildAt(i - parent.getFirstVisiblePosition());
                changeBorder(view, isSelected[i]);
            }
        }
    }

}
