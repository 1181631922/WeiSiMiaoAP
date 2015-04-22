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
import cn.edu.sjzc.fanyafeng.util.ImageLoaderCache;

/**
 * Created by Administrator on 2015/4/2.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
public class GongZhuoAdapter extends BaseAdapter {

    private Context context;
    private ImageLoaderCache mImageLoader;
    private List<GongZhuoBean> GongZhuoBeans;

    public GongZhuoAdapter(Context context, List<GongZhuoBean> GongZhuoBeans) {
        this.context = context;
        this.GongZhuoBeans = GongZhuoBeans;
        mImageLoader = new ImageLoaderCache(context);
    }

    public ImageLoaderCache getImagerLoader() {
        return mImageLoader;
    }

    @Override
    public int getCount() {
        return GongZhuoBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return GongZhuoBeans.get(position);
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
            view = LayoutInflater.from(context).inflate(R.layout.item_gongzhuo, null);

            holder = new ViewHolder();
            view.setTag(holder);

            holder.gongzhuo_img = (ImageView) view.findViewById(R.id.gongzhuo_img);
            holder.gongzhuo_name = (TextView) view.findViewById(R.id.gongzhuo_name);
            holder.gongzhuo_detail = (TextView) view.findViewById(R.id.gongzhuo_detail);
            holder.gongzhuo_phone = (TextView) view.findViewById(R.id.gongzhuo_phone);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.gongzhuo_name.setText(GongZhuoBeans.get(position).getGongZhuoName());
        holder.gongzhuo_detail.setText(GongZhuoBeans.get(position).getGongZhuoDetail());
        holder.gongzhuo_phone.setText(GongZhuoBeans.get(position).getGongZhuoPhone());

        mImageLoader.DisplayImage(GongZhuoBeans.get(position).getGongZhuoImg(), holder.gongzhuo_img, false);
        String urls = GongZhuoBeans.get(position).getGongZhuoImg();

        return view;
    }

    static class ViewHolder {
        ImageView gongzhuo_img;
        TextView gongzhuo_name;
        TextView gongzhuo_detail;
        TextView gongzhuo_phone;
    }


}
