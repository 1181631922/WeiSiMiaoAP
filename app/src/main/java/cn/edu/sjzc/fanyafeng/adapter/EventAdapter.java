package cn.edu.sjzc.fanyafeng.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.util.LruCache;

import com.xj.af.R;
import com.xj.af.util.ListViewImageTaskUtil;

import java.util.List;

import cn.edu.sjzc.fanyafeng.bean.EventBean;
import cn.edu.sjzc.fanyafeng.util.ImageLoaderCache;

/**
 * Created by Administrator on 2015/4/2.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
public class EventAdapter extends BaseAdapter {

    private Context context;
    private ImageLoaderCache mImageLoader;
    private List<EventBean> eventBeans;

    public EventAdapter(Context context, List<EventBean> eventBeans) {
        this.context = context;
        this.eventBeans = eventBeans;
        mImageLoader = new ImageLoaderCache(context);
    }

    public ImageLoaderCache getImagerLoader() {
        return mImageLoader;
    }

    @Override
    public int getCount() {
        return eventBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return eventBeans.get(position);
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
            view = LayoutInflater.from(context).inflate(R.layout.item_events, null);

            holder = new ViewHolder();
            view.setTag(holder);

            holder.event_img = (ImageView) view.findViewById(R.id.event_img);
            holder.event_name = (TextView) view.findViewById(R.id.event_name);
            holder.event_detail = (TextView) view.findViewById(R.id.event_detail);
            holder.event_starttime = (TextView) view.findViewById(R.id.eventstart_time);
            holder.event_endtime = (TextView) view.findViewById(R.id.eventend_time);
            holder.event_money = (TextView) view.findViewById(R.id.event_money);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.event_name.setText(eventBeans.get(position).getEventName());
        holder.event_detail.setText(eventBeans.get(position).getEventDetail());
        holder.event_starttime.setText(eventBeans.get(position).getEventStartName());
        holder.event_endtime.setText(eventBeans.get(position).getEventEndTime());
        holder.event_money.setText(eventBeans.get(position).getEventMoney());

        mImageLoader.DisplayImage(eventBeans.get(position).getEventImg(), holder.event_img, false);
        String urls = eventBeans.get(position).getEventImg();

        return view;
    }

    static class ViewHolder {
        ImageView event_img;
        TextView event_name;
        TextView event_detail;
        TextView event_starttime;
        TextView event_endtime;
        TextView event_money;
    }


}
