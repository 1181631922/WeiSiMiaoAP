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

import cn.edu.sjzc.fanyafeng.bean.XiangLuBean;
import cn.edu.sjzc.fanyafeng.util.ImageLoaderCache;

/**
 * Created by Administrator on 2015/4/2.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
public class XiangLuAdapter extends BaseAdapter {

    private Context context;
    private ImageLoaderCache mImageLoader;
    private List<XiangLuBean> XiangLuBeans;

    public XiangLuAdapter(Context context, List<XiangLuBean> XiangLuBeans) {
        this.context = context;
        this.XiangLuBeans = XiangLuBeans;
        mImageLoader = new ImageLoaderCache(context);
    }

    public ImageLoaderCache getImagerLoader() {
        return mImageLoader;
    }

    @Override
    public int getCount() {
        return XiangLuBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return XiangLuBeans.get(position);
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
            view = LayoutInflater.from(context).inflate(R.layout.item_xianglu, null);

            holder = new ViewHolder();
            view.setTag(holder);

            holder.XiangLu_img = (ImageView) view.findViewById(R.id.xianglu_img);
            holder.XiangLu_name = (TextView) view.findViewById(R.id.xianglu_name);
            holder.XiangLu_detail = (TextView) view.findViewById(R.id.xianglu_detail);
            holder.XiangLu_phone = (TextView) view.findViewById(R.id.xianglu_phone);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.XiangLu_name.setText(XiangLuBeans.get(position).getXiangLuName());
        holder.XiangLu_detail.setText(XiangLuBeans.get(position).getXiangLuDetail());
        holder.XiangLu_phone.setText(XiangLuBeans.get(position).getXiangLuPhone());

        mImageLoader.DisplayImage(XiangLuBeans.get(position).getXiangLuImg(), holder.XiangLu_img, false);
        String urls = XiangLuBeans.get(position).getXiangLuImg();

        return view;
    }

    static class ViewHolder {
        ImageView XiangLu_img;
        TextView XiangLu_name;
        TextView XiangLu_detail;
        TextView XiangLu_phone;
    }


}
