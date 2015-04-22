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

import cn.edu.sjzc.fanyafeng.bean.GongZhuoBean;
import cn.edu.sjzc.fanyafeng.bean.GuZhongBean;
import cn.edu.sjzc.fanyafeng.util.ImageLoaderCache;

/**
 * Created by Administrator on 2015/4/2.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
public class GuZhongAdapter extends BaseAdapter {

    private Context context;
    private ImageLoaderCache mImageLoader;
    private List<GuZhongBean> GuZhongBeans;

    public GuZhongAdapter(Context context, List<GuZhongBean> GuZhongBeans) {
        this.context = context;
        this.GuZhongBeans = GuZhongBeans;
        mImageLoader = new ImageLoaderCache(context);
    }

    public ImageLoaderCache getImagerLoader() {
        return mImageLoader;
    }

    @Override
    public int getCount() {
        return GuZhongBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return GuZhongBeans.get(position);
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
            view = LayoutInflater.from(context).inflate(R.layout.item_guzhong, null);

            holder = new ViewHolder();
            view.setTag(holder);

            holder.guzhong_img = (ImageView) view.findViewById(R.id.guzhong_img);
            holder.guzhong_name = (TextView) view.findViewById(R.id.guzhong_name);
            holder.guzhong_detail = (TextView) view.findViewById(R.id.guzhong_detail);
            holder.guzhong_phone = (TextView) view.findViewById(R.id.guzhong_phone);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.guzhong_name.setText(GuZhongBeans.get(position).getGuZhongName());
        holder.guzhong_detail.setText(GuZhongBeans.get(position).getGuZhongDetail());
        holder.guzhong_phone.setText(GuZhongBeans.get(position).getGuZhongPhone());

        mImageLoader.DisplayImage(GuZhongBeans.get(position).getGuZhongImg(), holder.guzhong_img, false);
        String urls = GuZhongBeans.get(position).getGuZhongImg();

        return view;
    }

    static class ViewHolder {
        ImageView guzhong_img;
        TextView guzhong_name;
        TextView guzhong_detail;
        TextView guzhong_phone;
    }


}
