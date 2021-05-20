package com.example.colornote.model;

import com.example.colornote.dao.CheckListDAO;
import com.example.colornote.dao.ItemCheckListDAO;
import com.example.colornote.mapper.CheckListMapper;
import com.example.colornote.util.Constant;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class CheckList extends Task{
    @Override
    public String showContent() {
        List<ItemCheckList> list = new ArrayList<>();
        list.addAll((new CheckListDAO()).getItemCheckList(id));
        String result = "";
        for(ItemCheckList item : list){
            result += "- "+item.getContent();
            result += "\n";
        }
        return result;
    }

    @Override
    public boolean isComplete() {
        List<ItemCheckList> list = new ArrayList<>();
        list.addAll((new CheckListDAO()).getItemCheckList(id));
        for(ItemCheckList item : list){
            if(item.getStatus() == Constant.NON_COMPLETE)
                return false;
        }
        return true;
    }
}
