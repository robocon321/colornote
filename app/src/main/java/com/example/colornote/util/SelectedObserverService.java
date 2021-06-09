package com.example.colornote.util;

import com.example.colornote.activity.MainActivity;
import com.example.colornote.fragment.HomeFragment;

import java.util.ArrayList;
import java.util.List;

public class SelectedObserverService {
    private static SelectedObserverService instance = new SelectedObserverService();
    private static List<ISeletectedObserver> observers = new ArrayList<>();

    public static SelectedObserverService getInstance(){
        return instance;
    }

    private boolean[] isSelected;

    private SelectedObserverService(){}

    public void reset(){
        unselected(0, isSelected.length);
    }

    public int count(){
        int result = 0;
        for(int i=0;i<isSelected.length;i++){
            result += isSelected[i] ? 1 : 0;
        }
        return result;
    }

    public int size(){return isSelected.length;}

    public String getRatio(){
        return count()+"/"+size();
    }

    public void unselected(int start, int end){
        if(end > isSelected.length) end = isSelected.length;
        if(start < 0) start = 0;
        if(start > end) return ;
        for(int i=start; i<end; i++) isSelected[i] = false;
        changeAll();
    }

    public void selected(int start, int end){
        if(end>isSelected.length) end=isSelected.length;
        if(start < 0) start =0;
        if(start > end) return;
        for(int i=start;i<end;i++) isSelected[i] = true;
        changeAll();
    }

    public boolean hasSelected(){
        for(int i=0;i<isSelected.length;i++){
            if(isSelected[i]) return true;
        }
        return false;
    }

    public void changeAll(){
        for(ISeletectedObserver o : observers){
            o.update(isSelected);
        }
    }

    public boolean[] getIsSelected(){
        return isSelected;
    }

    public void setSelected(boolean[] isSelected){
        this.isSelected = isSelected;
        changeAll();
    }

    public void addObserver(ISeletectedObserver o){
        observers.add(o);
    }

    public void removeObserver(ISeletectedObserver o){
        observers.remove(o);
    }
}
