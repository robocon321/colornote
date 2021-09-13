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
        list.addAll(ItemCheckListDAO.getInstance().getByParentId(id));
        String result = "";
        for(ItemCheckList item : list){
            result += "- "+item.getContent();
            result += "\n";
        }
        return result;
    }

    @Override
    public boolean completeAll() {
        List<ItemCheckList> list = new ArrayList<>();
        list.addAll(ItemCheckListDAO.getInstance().getByParentId(id));
        for(ItemCheckList item : list){
            if(!item.isCompleted())
                return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CheckList{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", completed=" + completed +
                ", colorId=" + colorId +
                ", reminderId=" + reminderId +
                ", modifiedDate=" + modifiedDate +
                ", status=" + status +
                ", accountId='" + accountId + '\'' +
                '}';
    }
}
