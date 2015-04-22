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

import cn.edu.sjzc.fanyafeng.bean.SiMiaoBean;
import cn.edu.sjzc.fanyafeng.bean.XiangLuBean;
import cn.edu.sjzc.fanyafeng.util.ImageLoaderCache;

/**
 * Created by Administrator on 2015/4/2.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
public class SiMiaoAdapter extends BaseAdapter {

    private Context context;
    private ImageLoaderCache mImageLoader;
    private List<SiMiaoBean> SiMiaoBeans;

    public SiMiaoAdapter(Context context, List<SiMiaoBean> SiMiaoBeans) {
        this.context = context;
        this.SiMiaoBeans = SiMiaoBeans;
        mImageLoader = new ImageLoaderCache(context);
    }

    public ImageLoaderCache getImagerLoader() {
        return mImageLoader;
    }

    @Override
    public int getCount() {
        return SiMiaoBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return SiMiaoBeans.get(position);
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
            view = LayoutInflater.from(context).inflate(R.layout.item_simiao2, null);

            holder = new ViewHolder();
            view.setTag(holder);

            holder.simiao_img = (ImageView) view.findViewById(R.id.simiao_img);
            holder.simiao_name = (TextView) view.findViewById(R.id.simiao2_name);
            holder.simiao_detail = (TextView) view.findViewById(R.id.simiao_detail);
            holder.simiao_phone = (TextView) view.findViewById(R.id.simiao_phone);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.simiao_name.setText(SiMiaoBeans.get(position).getSiMiaoName());
        holder.simiao_detail.setText(SiMiaoBeans.get(position).getSiMiaoDetail());
        holder.simiao_phone.setText(SiMiaoBeans.get(position).getSiMiaoPhone());

        mImageLoader.DisplayImage(SiMiaoBeans.get(position).getSiMiaoImg(), holder.simiao_img, false);
        String urls = SiMiaoBeans.get(position).getSiMiaoImg();

        return view;
    }

    static class ViewHolder {
        ImageView simiao_img;
        TextView simiao_name;
        TextView simiao_detail;
        TextView simiao_phone;
    }


}
