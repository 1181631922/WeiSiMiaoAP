package com.xj.af.index;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.xj.af.R;
import com.xj.af.common.BaseFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import cn.edu.sjzc.fanyafeng.activity.EventsActivity;
import cn.edu.sjzc.fanyafeng.fragment.MainTabActivity;

/**
 * 首页
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link IndexFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link IndexFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IndexFragment extends BaseFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private GridView gridView;
    private ImageView iv;
    private SimpleAdapter saImageItems;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    int[] images = null;// 图片资源ID
    String[] titles = null;// 标题

    ArrayList<ImageView> imageSource = null;// 图片资源
    ArrayList<View> dots = null;// 点
    TextView title = null;
    ViewPager viewPager;// 用于显示图片
    MyPagerAdapter adapter;// viewPager的适配器
    private int currPage = 0;// 当前显示的页
    private int oldPage = 0;// 上一次显示的页

    private OnFragmentInteractionListener mListener;

    private Button index_simiao, index_foshi, index_jiangtang, index_faxian, index_huodong, index_jieyuan;

    public Animation loadAnimation() {
        return AnimationUtils.loadAnimation(getActivity(), R.anim.translate);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IndexFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IndexFragment newInstance(String param1, String param2) {
        IndexFragment fragment = new IndexFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public IndexFragment() {
        // Required empty public constructor
    }

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        super.onCreateView(savedInstanceState);
        setContentView(R.layout.fragment_index);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        gridView = (GridView) getActivity().findViewById(R.id.index_gridView);
        gridView.setAdapter(saImageItems);
        gridView.setOnItemClickListener(new ItemClickListener());
        init();
        initView();
    }

    private void initView() {
        this.index_faxian = (Button) getActivity().findViewById(R.id.index_faxian);
        this.index_faxian.setOnClickListener(this);
        this.index_foshi = (Button) getActivity().findViewById(R.id.index_foshi);
        this.index_foshi.setOnClickListener(this);
        this.index_huodong = (Button) getActivity().findViewById(R.id.index_huodong);
        this.index_huodong.setOnClickListener(this);
        this.index_jiangtang = (Button) getActivity().findViewById(R.id.index_jiangtang);
        this.index_jiangtang.setOnClickListener(this);
        this.index_jieyuan = (Button) getActivity().findViewById(R.id.index_jieyuan);
        this.index_jieyuan.setOnClickListener(this);
        this.index_simiao = (Button) getActivity().findViewById(R.id.index_simiao);
        this.index_simiao.setOnClickListener(this);
    }

    public void init() {
        images = new int[]{R.drawable.a, R.drawable.b_, R.drawable.c_,
                R.drawable.d_, R.drawable.e_};
        titles = new String[]{"", "", "", "", ""};
        imageSource = new ArrayList<ImageView>();
        for (int i = 0; i < images.length; i++) {
            ImageView image = new ImageView(getActivity());
            image.setBackgroundResource(images[i]);
            imageSource.add(image);
        }

        dots = new ArrayList<View>();
        dots.add(getActivity().findViewById(R.id.home_dot1));
        dots.add(getActivity().findViewById(R.id.home_dot2));
        dots.add(getActivity().findViewById(R.id.home_dot3));
        dots.add(getActivity().findViewById(R.id.home_dot4));
        dots.add(getActivity().findViewById(R.id.home_dot5));

        title = (TextView) getActivity().findViewById(R.id.home_title);
        title.setText(titles[0]);

        viewPager = (ViewPager) getActivity().findViewById(R.id.home_vp);
        adapter = new MyPagerAdapter();
        viewPager.setAdapter(adapter);
        MyPageChangeListener listener = new MyPageChangeListener();
        viewPager.setOnPageChangeListener(listener);

        ScheduledExecutorService scheduled = Executors
                .newSingleThreadScheduledExecutor();
        ViewPagerTask pagerTask = new ViewPagerTask();
        scheduled.scheduleAtFixedRate(pagerTask, 4, 4, TimeUnit.SECONDS);
    }


    private class MyPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(imageSource.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(imageSource.get(position));
            return imageSource.get(position);
        }
    }

    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int position) {
            title.setText(titles[position]);
            dots.get(position).setBackgroundResource(R.drawable.dot_focused);
            dots.get(oldPage).setBackgroundResource(R.drawable.dot_normal);
            oldPage = position;
            currPage = position;
        }
    }

    private class ViewPagerTask implements Runnable {
        @Override
        public void run() {
            currPage = (currPage + 1) % images.length;
            handler.sendEmptyMessage(0);
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            viewPager.setCurrentItem(currPage);
        }

        ;
    };

    class ItemClickListener implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            // 在本例中arg2=arg3
            HashMap<String, Object> item = (HashMap<String, Object>) arg0
                    .getItemAtPosition(arg2);
            // 显示所选Item的ItemText
            String txt = (String) item.get("ItemText");
            if ("寺庙".equals(txt)) {
                Intent i = new Intent(getActivity(), TabActivity.class);
                i.putExtra(TabActivity.key, TabActivity.SiMiao);
                startActivity(i);
            } else if ("佛事".equals(txt)) {
                Intent i = new Intent(getActivity(), TabFixActivity.class);
                i.putExtra(TabActivity.key, TabFixActivity.FoShi);
                startActivity(i);
            } else if ("讲堂".equals(txt)) {
                Intent i = new Intent(getActivity(), TabActivity.class);
                i.putExtra(TabActivity.key, TabActivity.JiangTang);
                startActivity(i);
            } else if ("发现".equals(txt)) {
                Intent i = new Intent(getActivity(), MainTabActivity.class);
                startActivity(i);
            } else if ("活动".equals(txt)) {
                Intent i = new Intent(getActivity(), EventsActivity.class);
                startActivity(i);

            } else if ("结缘".equals(txt)) {
                Intent i = new Intent(getActivity(), TabFixActivity.class);
                i.putExtra(TabActivity.key, TabFixActivity.JieYuan);
                startActivity(i);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.index_simiao:
                Intent i = new Intent(getActivity(), TabActivity.class);
                i.putExtra(TabActivity.key, TabActivity.SiMiao);
                startActivity(i);
                break;
            case R.id.index_foshi:
                Intent i1 = new Intent(getActivity(), TabFixActivity.class);
                i1.putExtra(TabActivity.key, TabFixActivity.FoShi);
                startActivity(i1);
                break;
            case R.id.index_jiangtang:
                Intent i2 = new Intent(getActivity(), TabActivity.class);
                i2.putExtra(TabActivity.key, TabActivity.JiangTang);
                startActivity(i2);
                break;
            case R.id.index_faxian:
                Intent i3 = new Intent(getActivity(), MainTabActivity.class);
                startActivity(i3);
                break;
            case R.id.index_huodong:
                Intent i4 = new Intent(getActivity(), EventsActivity.class);
                startActivity(i4);
                break;
            case R.id.index_jieyuan:
                Intent i5 = new Intent(getActivity(), TabFixActivity.class);
                i5.putExtra(TabActivity.key, TabFixActivity.JieYuan);
                startActivity(i5);
                break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String str) {
        if (mListener != null) {
            mListener.onFragmentInteraction(str);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
        String wenzi[] = {"寺庙", "佛事", "讲堂", "发现", "活动", "结缘"};
        int pics[] = {
                R.drawable.index_ico_simiao_,
                R.drawable.index_ico1_,
                R.drawable.index_ico9_,
                R.drawable.index_ico4_,
                R.drawable.index_ico7_,
                R.drawable.index_ico3_,
                R.drawable.index_ico7_,
                R.drawable.index_ico8,
                R.drawable.index_ico9_};
        for (int i = 0; i < 6; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemImage", pics[i]);//添加图像资源的ID
            map.put("ItemText", wenzi[i]);//
            lstImageItem.add(map);
        }
        saImageItems = new SimpleAdapter(getActivity(),
                lstImageItem,
                R.layout.index_item,
                new String[]{"ItemImage", "ItemText"},
                new int[]{R.id.index_ItemImage,
                        R.id.index_ItemText}
        );
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String str);
    }

}
