package cn.edu.sjzc.fanyafeng.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xj.af.R;

import java.util.List;

import cn.edu.sjzc.fanyafeng.bean.ExhibitionBean;
import cn.edu.sjzc.fanyafeng.util.ImageLoaderCache;

/**
 * Created by Administrator on 2015/4/10.
 */
public class ExhibitionAdapter extends BaseAdapter {
    private Context context;
    private List<ExhibitionBean> exhibitionBeans;
    private ImageLoaderCache mImageLoader;
    private boolean isFinish;

    public ExhibitionAdapter(Context context, List<ExhibitionBean> exhibitionBeans) {
        this.context = context;
        this.exhibitionBeans = exhibitionBeans;
        mImageLoader = new ImageLoaderCache(context);
    }

    public ImageLoaderCache getImageLoader() {
        return mImageLoader;
    }

    @Override
    public int getCount() {
        return exhibitionBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return exhibitionBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder = null;
        if (holder == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_listview_exhibition, null);
            holder = new ViewHolder();
            view.setTag(holder);

            holder.exhibition_img = (ImageView) view.findViewById(R.id.exhibition_img);
            holder.exhibition_desc = (TextView) view.findViewById(R.id.exhibition_desc);
            holder.exhibition_time = (TextView) view.findViewById(R.id.exhibition_time);
            holder.exhibition_palace = (TextView) view.findViewById(R.id.exhibition_palace);

            ExhibitionBean exhibitionBean = exhibitionBeans.get(position);
            isFinish = exhibitionBean.isFinished();



        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.exhibition_img.setImageResource(R.drawable.event);
        mImageLoader.DisplayImage(exhibitionBeans.get(position).getExhibitionImg(), holder.exhibition_img, false);
        holder.exhibition_desc.setText(exhibitionBeans.get(position).getExhibitionDesc());
        holder.exhibition_time.setText(exhibitionBeans.get(position).getExhibitionTime());
        holder.exhibition_palace.setText(exhibitionBeans.get(position).getExhibitionPalace());
        return view;
    }

    static class ViewHolder {
        ImageView exhibition_img;
        TextView exhibition_desc;
        TextView exhibition_time;
        TextView exhibition_palace;
        ImageView exhibition_state;
    }
}
