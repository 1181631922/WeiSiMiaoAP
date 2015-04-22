package cn.edu.sjzc.fanyafeng.fragment;

import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xj.af.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cn.edu.sjzc.fanyafeng.adapter.ExhibitionAdapter;
import cn.edu.sjzc.fanyafeng.bean.ExhibitionBean;

public class ExhibitionFragment extends Fragment{
    private ImageView exhibition_img,exhibition_state;
    private TextView exhibition_desc,exhibition_time,exhibition_palace;
    private ListView exhibition_listview;
    private ExhibitionAdapter exhibitionAdapter;
    private List<ExhibitionBean> exhibitionBeans;
    private List<Map<String,Object>> exhibitionList = new ArrayList<Map<String, Object>>();
    private ExhibitionBean[] exhibitionBeanArray;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {	
		return inflater.inflate(R.layout.fragment_exhibition, null);
	}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
    }

    private void initView(){
        this.exhibition_img = (ImageView)getActivity().findViewById(R.id.exhibition_img);
        this.exhibition_desc = (TextView)getActivity().findViewById(R.id.exhibition_desc);
        this.exhibition_time = (TextView)getActivity().findViewById(R.id.exhibition_time);
        this.exhibition_palace = (TextView)getActivity().findViewById(R.id.exhibition_palace);
        this.exhibition_state = (ImageView)getActivity().findViewById(R.id.exhibition_state);
        this.exhibition_listview = (ListView)getActivity().findViewById(R.id.exhibition_listview);
    }

    private void initData(){
        exhibitionBeans = new ArrayList<ExhibitionBean>();
        exhibitionBeanArray = new ExhibitionBean[]{
                new ExhibitionBean("http://121.40.142.138:81//ueditor/jsp/upload/image/20150417/1429233271312023017.jpg","这是石家庄的展会","2015/4/11","石家庄",R.drawable.exhibition_back),
                new ExhibitionBean("http://img.pusa123.com/www/uploads/allimg/130602/5424_130602094335_1.jpg","这是唐山的展会","2015/4/11","唐山",R.drawable.exhibition),
                new ExhibitionBean("http://img.pusa123.com/www/uploads/allimg/150403/17422_150403102211_1.jpg","这是秦皇岛的展会","2015/3/11","秦皇岛",R.drawable.exhibition),
                new ExhibitionBean("http://img.pusa123.com/www/uploads/allimg/150403/17422_150403151837_1.jpg","这是衡水的展会","2015/3/12","衡水",R.drawable.exhibition_back)
        };
        Arrays.sort(exhibitionBeanArray);
        exhibitionBeans = Arrays.asList(exhibitionBeanArray);
        exhibitionAdapter = new ExhibitionAdapter(getActivity(),exhibitionBeans);
        exhibition_listview.setAdapter(exhibitionAdapter);
    }


}