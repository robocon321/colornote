package com.example.colornote.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.colornote.R;
import com.example.colornote.activity.CheckList_Activity;
import com.example.colornote.dao.ItemCheckListDAO;
import com.example.colornote.model.ItemCheckList;
import com.example.colornote.util.Constant;
import com.google.android.material.resources.TextAppearance;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class CheckListAdapter extends RecyclerView.Adapter<CheckListAdapter.ViewHolder> implements Filterable {
    private ArrayList<ItemCheckList> listitem;
    private ArrayList<ItemCheckList> filterList;
    private Context context;
    float sizeContent=0;
    String queryText = "";
    ItemCheckList itemCheckList;
    public CheckListAdapter(ArrayList<ItemCheckList> listitem,Context context){
        this.listitem = listitem;
        this.context = context;
        this.filterList = listitem;

    }
    public void setSizeContent(TextView textView){
        SharedPreferences pre = androidx.preference.PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        String font_size =pre.getString("font_size","100dp");
        switch (font_size){
            case "Tiny":    sizeContent=context.getResources().getDimensionPixelSize(R.dimen.font_size_tiny);
                break;
            case "Small":sizeContent=context.getResources().getDimensionPixelSize(R.dimen.font_size_small);
                break;
            case "Medium": sizeContent=context.getResources().getDimensionPixelSize(R.dimen.font_size_medium);
                break;
            case "Large": sizeContent=context.getResources().getDimensionPixelSize(R.dimen.font_size_large);
                break;
            case "Huge": sizeContent=context.getResources().getDimensionPixelSize(R.dimen.font_size_huge);
                break;
            default: sizeContent=context.getResources().getDimensionPixelSize(R.dimen.font_size);
                break;

        }

        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,sizeContent);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_checklist,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(Constant.num_edit==0){
            holder.button.setVisibility(View.GONE);
        }else{
            holder.button.setVisibility(View.VISIBLE);
        }
        //nhan gach chan vao database
        itemCheckList = listitem.get(position);
        if(itemCheckList.isCompleted()==false){
           holder.textView.setPaintFlags(0);
        }else{
            holder.textView.setPaintFlags(holder.textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        String dataText = filterList.get(position).getContent();
        if(queryText!=null && !queryText.isEmpty()){
            int startsPos = dataText.toLowerCase().indexOf(queryText.toLowerCase());
            int endPos = startsPos+queryText.length();
            if(startsPos!=-1){
                Spannable spannable = new SpannableString(dataText);
                ColorStateList colorStateList = new ColorStateList(new int[][]{new int[]{}},new int[]{Color.BLUE});
                TextAppearanceSpan textAppearance = new TextAppearanceSpan(null, Typeface.BOLD,-1,colorStateList,null);
                spannable.setSpan(textAppearance,startsPos,endPos, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.textView.setText(spannable);
            }else{
                holder.textView.setText(dataText);
            }

        }else{
            holder.textView.setText(dataText);

        }
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }
    public void removeItem(int position){
        listitem.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if(constraint!=null && !constraint.equals("")){
                queryText = constraint.toString();
                List<ItemCheckList> newList = new ArrayList<>();
                for(int i = 0;i<listitem.size();i++){
                    if(listitem.get(i).getContent().toLowerCase().contains(constraint.toString().toLowerCase())){
                        newList.add(listitem.get(i));
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.count = newList.size();
                filterResults.values = newList;
                return filterResults;
            }

            return null;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if(results!=null && results.count>0){
                filterList = (ArrayList<ItemCheckList>) results.values;
                notifyDataSetChanged();
            }
        }
    };
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        Button button;
        public ViewHolder( View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text_itemchecklist);
            button = (Button) itemView.findViewById(R.id.button_deleteitem);
            textView.setBackgroundDrawable(itemView.getBackground());
            button.setBackgroundDrawable(itemView.getBackground());
            setSizeContent(textView);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeItem(getAdapterPosition());
                }
            });
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if(listitem.get(getAdapterPosition()).isCompleted()==false) {
                            ItemCheckList itemCheckList = listitem.get(getAdapterPosition());
                            itemCheckList.setCompleted(true);
                            ItemCheckListDAO itemCheckListDAO = ItemCheckListDAO.getInstance();
                            itemCheckListDAO.update(itemCheckList);
                            textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        }else{
                            ItemCheckList itemCheckList = listitem.get(getAdapterPosition());
                            itemCheckList.setCompleted(false);
                            ItemCheckListDAO itemCheckListDAO = ItemCheckListDAO.getInstance();
                            itemCheckListDAO.update(itemCheckList);
                            textView.setPaintFlags(0);
                        }
                        return false;
                    }
                });

                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (Constant.num_edit == 1) {
                                Toast.makeText(context.getApplicationContext(), Constant.num_edit + "", Toast.LENGTH_LONG).show();
                                Dialog dialog = new Dialog(v.getContext());
                                dialog.setContentView(R.layout.dialog_additem_checklist);
                                TextView textViewtitle;
                                EditText editTextitemDialog;
                                Button button_ok, button_exit;
                                textViewtitle = (TextView) dialog.findViewById(R.id.textViewTitle);
                                editTextitemDialog = (EditText) dialog.findViewById(R.id.edtext_item);
                                button_ok = (Button) dialog.findViewById(R.id.btn_ok);
                                editTextitemDialog.setText(textView.getText());
                                //  button_exit = (Button) dialog.findViewById(R.id.btn_exit);
                                textViewtitle.setText("Edit Item");
                                button_ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String text = editTextitemDialog.getText().toString();
                                        textView.setText(text);
                                        ItemCheckList item = listitem.get(getAdapterPosition());
                                        item.setContent(text);
                                        listitem.set(getAdapterPosition(), item);
                                        dialog.dismiss();

                                    }
                                });
                                dialog.show();
                            }else{
                                Toast.makeText(context.getApplicationContext(), Constant.num_edit + "", Toast.LENGTH_LONG).show();
                            }
                        }
                    });




        }
    }
}

