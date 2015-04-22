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
import cn.edu.sjzc.fanyafeng.bean.ZhuangShiBean;
import cn.edu.sjzc.fanyafeng.util.ImageLoaderCache;

/**
 * Created by Administrator on 2015/4/2.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
public class ZhuangShiAdapter extends BaseAdapter {

    private Context context;
    private ImageLoaderCache mImageLoader;
    private List<ZhuangShiBean> ZhuangShiBeans;

    public ZhuangShiAdapter(Context context, List<ZhuangShiBean> ZhuangShiBeans) {
        this.context = context;
        this.ZhuangShiBeans = ZhuangShiBeans;
        mImageLoader = new ImageLoaderCache(context);
    }

    public ImageLoaderCache getImagerLoader() {
        return mImageLoader;
    }

    @Override
    public int getCount() {
        return ZhuangShiBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return ZhuangShiBeans.get(position);
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
            view = LayoutInflater.from(context).inflate(R.layout.item_zhuangshi, null);

            holder = new ViewHolder();
            view.setTag(holder);

            holder.zhuangshi_img = (ImageView) view.findViewById(R.id.zhuangshi_img);
            holder.zhuangshi_name = (TextView) view.findViewById(R.id.zhuangshi_name);
            holder.zhuangshi_detail = (TextView) view.findViewById(R.id.zhuangshi_detail);
            holder.zhuangshi_phone = (TextView) view.findViewById(R.id.zhuangshi_phone);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.zhuangshi_name.setText(ZhuangShiBeans.get(position).getZhuangShiName());
        holder.zhuangshi_detail.setText(ZhuangShiBeans.get(position).getZhuangShiDetail());
        holder.zhuangshi_phone.setText(ZhuangShiBeans.get(position).getZhuangShiPhone());

        mImageLoader.DisplayImage(ZhuangShiBeans.get(position).getZhuangShiImg(), holder.zhuangshi_img, false);
        String urls = ZhuangShiBeans.get(position).getZhuangShiImg();

        return view;
    }

    static class ViewHolder {
        ImageView zhuangshi_img;
        TextView zhuangshi_name;
        TextView zhuangshi_detail;
        TextView zhuangshi_phone;
    }


}
