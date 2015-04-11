package cn.edu.sjzc.fanyafeng.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xj.af.R;

import java.util.List;

import cn.edu.sjzc.fanyafeng.bean.BuddhistServicesBean;

/**
 * Created by Administrator on 2015/4/2.
 */
public class BuddhistServicesAdapter extends BaseAdapter {

    private Context context;
    private List<BuddhistServicesBean> buddhistServicesBeans;

    public BuddhistServicesAdapter(Context context, List<BuddhistServicesBean> buddhistServicesBeans) {
        this.context = context;
        this.buddhistServicesBeans = buddhistServicesBeans;
    }

    @Override
    public int getCount() {
        return buddhistServicesBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return buddhistServicesBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_listview_buddhistservices, null);

            holder = new ViewHolder();
            view.setTag(holder);

            holder.bs_title = (TextView) view.findViewById(R.id.bs_title);
            holder.bs_detial = (TextView) view.findViewById(R.id.bs_detail);
            holder.bs_first = (TextView) view.findViewById(R.id.bs_first);
            holder.bs_second = (TextView) view.findViewById(R.id.bs_second);
            holder.bs_third = (TextView) view.findViewById(R.id.bs_third);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.bs_title.setText(buddhistServicesBeans.get(position).getBSTitle());
        holder.bs_detial.setText(buddhistServicesBeans.get(position).getBSDetail());
        holder.bs_first.setText(buddhistServicesBeans.get(position).getBSFirst());
        holder.bs_second.setText(buddhistServicesBeans.get(position).getBSSecond());
        holder.bs_third.setText(buddhistServicesBeans.get(position).getBSThird());


        return view;
    }

    static class ViewHolder {
        TextView bs_title;
        TextView bs_detial;
        TextView bs_first;
        TextView bs_second;
        TextView bs_third;
    }
}
