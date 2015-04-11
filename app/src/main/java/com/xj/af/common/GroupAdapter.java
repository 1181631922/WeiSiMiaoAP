package com.xj.af.common;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.xj.af.R;

public class GroupAdapter extends BaseAdapter {

	private Context context;
	 
    private List<String> list;
    private List<Integer> listIco;
 
    public GroupAdapter(Context context, List<String> list,List<Integer> listIco) {
 
        this.context = context;
        this.list = list;
        this.listIco = listIco;
 
    }
 
    @Override
    public int getCount() {
        return list.size();
    }
 
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView==null) {
            convertView=LayoutInflater.from(context).inflate(R.layout.title_menu_item, null);
            holder=new ViewHolder();
            convertView.setTag(holder);
            holder.groupItem=(TextView) convertView.findViewById(R.id.groupItem);
            holder.icoItem = (ImageView)convertView.findViewById(R.id.title_sns_ico);
        }
        else{
            holder=(ViewHolder) convertView.getTag();
        }
        holder.groupItem.setTextColor(Color.WHITE);
        holder.groupItem.setText(list.get(position));
        holder.icoItem.setBackgroundResource(listIco.get(position));
        return convertView;
    }
 
    static class ViewHolder {
        TextView groupItem;
        ImageView icoItem;
    }
 
}

