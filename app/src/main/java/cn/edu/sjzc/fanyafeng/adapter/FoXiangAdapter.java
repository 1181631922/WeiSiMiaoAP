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

import cn.edu.sjzc.fanyafeng.bean.FoXiangBean;
import cn.edu.sjzc.fanyafeng.bean.ZhuangShiBean;
import cn.edu.sjzc.fanyafeng.util.ImageLoaderCache;

/**
 * Created by Administrator on 2015/4/2.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
public class FoXiangAdapter extends BaseAdapter {

    private Context context;
    private ImageLoaderCache mImageLoader;
    private List<FoXiangBean> FoXiangBeans;

    public FoXiangAdapter(Context context, List<FoXiangBean> FoXiangBeans) {
        this.context = context;
        this.FoXiangBeans = FoXiangBeans;
        mImageLoader = new ImageLoaderCache(context);
    }

    public ImageLoaderCache getImagerLoader() {
        return mImageLoader;
    }

    @Override
    public int getCount() {
        return FoXiangBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return FoXiangBeans.get(position);
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
            view = LayoutInflater.from(context).inflate(R.layout.item_foxiang, null);

            holder = new ViewHolder();
            view.setTag(holder);

            holder.foxiang_img = (ImageView) view.findViewById(R.id.foxiang_img);
            holder.foxiang_name = (TextView) view.findViewById(R.id.foxiang_name);
            holder.foxiang_detail = (TextView) view.findViewById(R.id.foxiang_detail);
            holder.foxiang_phone = (TextView) view.findViewById(R.id.foxiang_phone);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.foxiang_name.setText(FoXiangBeans.get(position).getFoXiangName());
        holder.foxiang_detail.setText(FoXiangBeans.get(position).getFoXiangDetail());
        holder.foxiang_phone.setText(FoXiangBeans.get(position).getFoXiangPhone());

        mImageLoader.DisplayImage(FoXiangBeans.get(position).getFoXiangImg(), holder.foxiang_img, false);
        String urls = FoXiangBeans.get(position).getFoXiangImg();

        return view;
    }

    static class ViewHolder {
        ImageView foxiang_img;
        TextView foxiang_name;
        TextView foxiang_detail;
        TextView foxiang_phone;
    }


}
