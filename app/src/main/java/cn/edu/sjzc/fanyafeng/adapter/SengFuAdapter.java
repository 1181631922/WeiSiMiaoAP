package cn.edu.sjzc.fanyafeng.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xj.af.R;

import java.util.List;

import cn.edu.sjzc.fanyafeng.bean.SengFuBean;
import cn.edu.sjzc.fanyafeng.bean.ZhuangShiBean;
import cn.edu.sjzc.fanyafeng.util.ImageLoaderCache;

/**
 * Created by Administrator on 2015/4/2.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
public class SengFuAdapter extends BaseAdapter {

    private Context context;
    private ImageLoaderCache mImageLoader;
    private List<SengFuBean> SengFuBeans;

    public SengFuAdapter(Context context, List<SengFuBean> SengFuBeans) {
        this.context = context;
        this.SengFuBeans = SengFuBeans;
        mImageLoader = new ImageLoaderCache(context);
    }

    public ImageLoaderCache getImagerLoader() {
        return mImageLoader;
    }

    @Override
    public int getCount() {
        return SengFuBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return SengFuBeans.get(position);
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
            view = LayoutInflater.from(context).inflate(R.layout.item_sengfu, null);

            holder = new ViewHolder();
            view.setTag(holder);

            holder.sengfu_img = (ImageView) view.findViewById(R.id.sengfu_img);
            holder.sengfu_name = (TextView) view.findViewById(R.id.sengfu_name);
            holder.sengfu_detail = (TextView) view.findViewById(R.id.sengfu_detail);
            holder.sengfu_phone = (TextView) view.findViewById(R.id.sengfu_phone);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.sengfu_name.setText(SengFuBeans.get(position).getSengFuName());
        holder.sengfu_detail.setText(SengFuBeans.get(position).getSengFuDetail());
        holder.sengfu_phone.setText(SengFuBeans.get(position).getSengFuPhone());

        mImageLoader.DisplayImage(SengFuBeans.get(position).getSengFuImg(), holder.sengfu_img, false);
        String urls = SengFuBeans.get(position).getSengFuImg();

        return view;
    }

    static class ViewHolder {
        ImageView sengfu_img;
        TextView sengfu_name;
        TextView sengfu_detail;
        TextView sengfu_phone;
    }


}
