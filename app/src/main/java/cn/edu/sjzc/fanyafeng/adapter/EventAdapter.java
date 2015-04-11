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

/**
 * Created by Administrator on 2015/4/2.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
public class EventAdapter extends BaseAdapter {

    private Context context;
    private List<EventBean> eventBeans;

    private final int maxMemory = (int) Runtime.getRuntime().maxMemory();
    private final int cacheSize = maxMemory / 5;
    private android.support.v4.util.LruCache<String, Bitmap> mLruCache = new android.support.v4.util.LruCache<String, Bitmap>(
            cacheSize) {
        @Override
        protected int sizeOf(String key, Bitmap bitmap) {
            return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
        }
    };

    public EventAdapter(Context context, List<EventBean> eventBeans) {
        this.context = context;
        this.eventBeans = eventBeans;
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

        if (view ==null){
            view = LayoutInflater.from(context).inflate(R.layout.item_events,null);

            holder=new ViewHolder();
            view.setTag(holder);

            holder.event_img =(ImageView)view.findViewById(R.id.event_img);
            holder.event_name = (TextView)view.findViewById(R.id.event_name);
            holder.event_detail = (TextView)view.findViewById(R.id.event_detail);
            holder.event_time = (TextView)view.findViewById(R.id.event_time);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }


        holder.event_name.setText(eventBeans.get(position).getEventName());
//        holder.event_detail.setText(eventBeans.get(position).getEventDetail());
        holder.event_time.setText(eventBeans.get(position).getEventTime());

        loadBitmap(eventBeans.get(position).getEventImg(),holder.event_img);
        return view;
    }

    static class ViewHolder {
        ImageView event_img;
        TextView event_name;
        TextView event_detail;
        TextView event_time;
    }

    private void loadBitmap(String urlStr, ImageView image) {

        ListViewImageTaskUtil asyncLoader = new ListViewImageTaskUtil(image, mLruCache);
        Bitmap bitmap = asyncLoader.getBitmapFromMemoryCache(urlStr);
        if (bitmap != null) image.setImageBitmap(bitmap);
        else {
            image.setImageResource(R.drawable.event);
            asyncLoader.execute(urlStr);
        }
    }
}
