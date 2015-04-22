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

import cn.edu.sjzc.fanyafeng.bean.FaQiBean;
import cn.edu.sjzc.fanyafeng.bean.ZhuangShiBean;
import cn.edu.sjzc.fanyafeng.util.ImageLoaderCache;

/**
 * Created by Administrator on 2015/4/2.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
public class FaQiAdapter extends BaseAdapter {

    private Context context;
    private ImageLoaderCache mImageLoader;
    private List<FaQiBean> FaQiBeans;

    public FaQiAdapter(Context context, List<FaQiBean> FaQiBeans) {
        this.context = context;
        this.FaQiBeans = FaQiBeans;
        mImageLoader = new ImageLoaderCache(context);
    }

    public ImageLoaderCache getImagerLoader() {
        return mImageLoader;
    }

    @Override
    public int getCount() {
        return FaQiBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return FaQiBeans.get(position);
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
            view = LayoutInflater.from(context).inflate(R.layout.item_faqi, null);

            holder = new ViewHolder();
            view.setTag(holder);

            holder.faqi_img = (ImageView) view.findViewById(R.id.faqi_img);
            holder.faqi_name = (TextView) view.findViewById(R.id.faqi_name);
            holder.faqi_detail = (TextView) view.findViewById(R.id.faqi_detail);
            holder.faqi_phone = (TextView) view.findViewById(R.id.faqi_phone);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.faqi_name.setText(FaQiBeans.get(position).getFaQiName());
        holder.faqi_detail.setText(FaQiBeans.get(position).getFaQiDetail());
        holder.faqi_phone.setText(FaQiBeans.get(position).getFaQiPhone());

        mImageLoader.DisplayImage(FaQiBeans.get(position).getFaQiImg(), holder.faqi_img, false);
        String urls = FaQiBeans.get(position).getFaQiImg();

        return view;
    }

    static class ViewHolder {
        ImageView faqi_img;
        TextView faqi_name;
        TextView faqi_detail;
        TextView faqi_phone;
    }


}
