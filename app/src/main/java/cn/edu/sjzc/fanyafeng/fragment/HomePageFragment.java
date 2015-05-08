package cn.edu.sjzc.fanyafeng.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


import com.xj.af.R;
import com.xj.af.util.SingleImageTaskUtil;

import cn.edu.sjzc.fanyafeng.activity.FaQiActivity;
import cn.edu.sjzc.fanyafeng.activity.FoXiangDingZhiActivity;
import cn.edu.sjzc.fanyafeng.activity.GongZhuoActivity;
import cn.edu.sjzc.fanyafeng.activity.GuZhongActivity;
import cn.edu.sjzc.fanyafeng.activity.SengFuActivity;
import cn.edu.sjzc.fanyafeng.activity.SiMiaoActivity;
import cn.edu.sjzc.fanyafeng.activity.XiangLuActivity;
import cn.edu.sjzc.fanyafeng.activity.ZhuangShiActivity;

public class HomePageFragment extends Fragment implements View.OnClickListener {

    int[] images = null;// 图片资源ID
    String[] titles = null;// 标题

    ArrayList<ImageView> imageSource = null;// 图片资源
    ArrayList<View> dots = null;// 点
    TextView title = null;
    ViewPager viewPager;// 用于显示图片
    MyPagerAdapter adapter;// viewPager的适配器
    private int currPage = 0;// 当前显示的页
    private int oldPage = 0;// 上一次显示的页
    private Button home_gongzhuo, home_xianglu, home_guzhong, home_simiao, home_zhuangshi, home_sengfu, home_faqi, home_foxiang;
    private ImageButton home_frag_one_ib, home_frag_two_ib, home_frag_three_ib, home_frag_four_ib;

    //自定义viewpager小模块
    private ViewPager viewPagerTab, viewPagerTab2;
    private TextView tab1_gongzhuo, tab1_xianglu, tab1_guzhong, tab1_simiao, tab2_zhuangshi, tab2_sengfu, tab2_faqi, tab2_fuxiang;
    private ImageView tab1_gongzhuo_iv, tab1_xianglu_iv, tab1_guzhong_iv, tab1_simiao_iv, tab2_zhuangshi_iv, tab2_sengfu_iv, tab2_faqi_iv, tab2_fuxiang_iv;
    private List<Fragment> fragments;
    private int selectedColor, unSelectedColor, tabSelectedColor, tabUnSelectedColor;
    private static String TAG = "生命周期";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_homepage, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        initView();
        InitTextView();
        InitViewPager();
        InitTextView2();
        InitViewPager2();
    }


    private void initView() {
        this.home_gongzhuo = (Button) getActivity().findViewById(R.id.home_gongzhuo);
        this.home_gongzhuo.setOnClickListener(this);
        this.home_xianglu = (Button) getActivity().findViewById(R.id.home_xianglu);
        this.home_xianglu.setOnClickListener(this);
        this.home_guzhong = (Button) getActivity().findViewById(R.id.home_guzhong);
        this.home_guzhong.setOnClickListener(this);
        this.home_simiao = (Button) getActivity().findViewById(R.id.home_simiao);
        this.home_simiao.setOnClickListener(this);
        this.home_zhuangshi = (Button) getActivity().findViewById(R.id.home_zhuangshi);
        this.home_zhuangshi.setOnClickListener(this);
        this.home_sengfu = (Button) getActivity().findViewById(R.id.home_sengfu);
        this.home_sengfu.setOnClickListener(this);
        this.home_faqi = (Button) getActivity().findViewById(R.id.home_faqi);
        this.home_faqi.setOnClickListener(this);
        this.home_foxiang = (Button) getActivity().findViewById(R.id.home_foxiang);
        this.home_foxiang.setOnClickListener(this);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        InitTextView();
//        InitViewPager();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    /**
     * 初始化viewpager页面
     *
     * @param
     */
    private void InitViewPager() {
        viewPagerTab = (ViewPager) getActivity().findViewById(R.id.viewPagerTab);
        Fragment gongZhuoFragment = new GongZhuoFragment();
        Fragment xiangLuFragment = new XiangLuFragment();
        Fragment guZhongFragment = new GuZhongFragment();
        Fragment siMiaoFragment = new SiMiaoFragment();

        fragments = new ArrayList<Fragment>();

        fragments.add(gongZhuoFragment);
        fragments.add(xiangLuFragment);
        fragments.add(guZhongFragment);
        fragments.add(siMiaoFragment);

        viewPagerTab.setAdapter(new tabPagerAdapter(getChildFragmentManager(), fragments));
        viewPagerTab.setCurrentItem(0);
        viewPagerTab.setCanScroll(false);
    }

    /**
     * 初始化头标
     */

    private void InitTextView() {
        //viewpager
        this.selectedColor = getActivity().getResources().getColor(R.color.tab_title_pressed_color);
        this.unSelectedColor = getActivity().getResources().getColor(R.color.tab_title_normal_color);
        this.tabSelectedColor = getActivity().getResources().getColor(R.color.tab_select);
        this.tabUnSelectedColor = getActivity().getResources().getColor(R.color.tab_unselect);

        tab1_gongzhuo = (TextView) getActivity().findViewById(R.id.tab1_gongzhuo);
        tab1_xianglu = (TextView) getActivity().findViewById(R.id.tab1_xianglu);
        tab1_guzhong = (TextView) getActivity().findViewById(R.id.tab1_guzhong);
        tab1_simiao = (TextView) getActivity().findViewById(R.id.tab1_simiao);

        tab1_gongzhuo_iv = (ImageView) getActivity().findViewById(R.id.tab1_gongzhuo_iv);
        tab1_xianglu_iv = (ImageView) getActivity().findViewById(R.id.tab1_xianglu_iv);
        tab1_guzhong_iv = (ImageView) getActivity().findViewById(R.id.tab1_guzhong_iv);
        tab1_simiao_iv = (ImageView) getActivity().findViewById(R.id.tab1_simiao_iv);

        tab1_gongzhuo.setTextColor(selectedColor);
        tab1_xianglu.setTextColor(unSelectedColor);
        tab1_guzhong.setTextColor(unSelectedColor);
        tab1_simiao.setTextColor(unSelectedColor);

        tab1_gongzhuo_iv.setBackgroundColor(tabSelectedColor);
        tab1_xianglu_iv.setBackgroundColor(tabUnSelectedColor);
        tab1_guzhong_iv.setBackgroundColor(tabUnSelectedColor);
        tab1_simiao_iv.setBackgroundColor(tabUnSelectedColor);

        tab1_gongzhuo.setText("供桌/佛龛");
        tab1_xianglu.setText("香炉/大鼎");
        tab1_guzhong.setText("鼓钟定制");
        tab1_simiao.setText("寺庙/建筑");

        tab1_gongzhuo.setOnClickListener(new tabOnClickListener(0));
        tab1_xianglu.setOnClickListener(new tabOnClickListener(1));
        tab1_guzhong.setOnClickListener(new tabOnClickListener(2));
        tab1_simiao.setOnClickListener(new tabOnClickListener(3));

        tab1_gongzhuo_iv.setOnClickListener(new tabOnClickListener(0));
        tab1_xianglu_iv.setOnClickListener(new tabOnClickListener(1));
        tab1_guzhong_iv.setOnClickListener(new tabOnClickListener(2));
        tab1_simiao_iv.setOnClickListener(new tabOnClickListener(3));
    }


    /**
     * 初始化viewpager页面
     *
     * @param
     */
    private void InitViewPager2() {
        viewPagerTab2 = (ViewPager) getActivity().findViewById(R.id.viewPagerTab2);
        Fragment zhuangShiFragment = new ZhuangShiFragment();
        Fragment sengFuFragment = new SengFuFragment();
        Fragment faQiFragment = new FaQiFragment();
        Fragment foXiangFragment = new FoXiangFragment();

        fragments = new ArrayList<Fragment>();

        fragments.add(zhuangShiFragment);
        fragments.add(sengFuFragment);
        fragments.add(faQiFragment);
        fragments.add(foXiangFragment);

        viewPagerTab2.setAdapter(new tabPagerAdapter2(getChildFragmentManager(), fragments));
        viewPagerTab2.setCurrentItem(0);
        viewPagerTab2.setCanScroll(false);
    }

    /**
     * 初始化头标
     */
    private void InitTextView2() {
        //viewpager
        this.selectedColor = getActivity().getResources().getColor(R.color.tab_title_pressed_color);
        this.unSelectedColor = getActivity().getResources().getColor(R.color.tab_title_normal_color);
        this.tabSelectedColor = getActivity().getResources().getColor(R.color.tab_select);
        this.tabUnSelectedColor = getActivity().getResources().getColor(R.color.tab_unselect);

        tab2_zhuangshi = (TextView) getActivity().findViewById(R.id.tab2_zhuangshi);
        tab2_sengfu = (TextView) getActivity().findViewById(R.id.tab2_sengfu);
        tab2_faqi = (TextView) getActivity().findViewById(R.id.tab2_faqi);
        tab2_fuxiang = (TextView) getActivity().findViewById(R.id.tab2_fuxiang);

        tab2_zhuangshi_iv = (ImageView) getActivity().findViewById(R.id.tab2_zhuangshi_iv);
        tab2_sengfu_iv = (ImageView) getActivity().findViewById(R.id.tab2_sengfu_iv);
        tab2_faqi_iv = (ImageView) getActivity().findViewById(R.id.tab2_faqi_iv);
        tab2_fuxiang_iv = (ImageView) getActivity().findViewById(R.id.tab2_fuxiang_iv);

        tab2_zhuangshi.setTextColor(selectedColor);
        tab2_sengfu.setTextColor(unSelectedColor);
        tab2_faqi.setTextColor(unSelectedColor);
        tab2_fuxiang.setTextColor(unSelectedColor);

        tab2_zhuangshi_iv.setBackgroundColor(tabSelectedColor);
        tab2_sengfu_iv.setBackgroundColor(tabUnSelectedColor);
        tab2_faqi_iv.setBackgroundColor(tabUnSelectedColor);
        tab2_fuxiang_iv.setBackgroundColor(tabUnSelectedColor);

        tab2_zhuangshi.setText("寺庙装饰");
        tab2_sengfu.setText("僧服/绣品");
        tab2_faqi.setText("法器/法物");
        tab2_fuxiang.setText("佛像定制");

        tab2_zhuangshi.setOnClickListener(new tabOnClickListener2(0));
        tab2_sengfu.setOnClickListener(new tabOnClickListener2(1));
        tab2_faqi.setOnClickListener(new tabOnClickListener2(2));
        tab2_fuxiang.setOnClickListener(new tabOnClickListener2(3));

        tab2_zhuangshi_iv.setOnClickListener(new tabOnClickListener2(0));
        tab2_sengfu_iv.setOnClickListener(new tabOnClickListener2(1));
        tab2_faqi_iv.setOnClickListener(new tabOnClickListener2(2));
        tab2_fuxiang_iv.setOnClickListener(new tabOnClickListener2(3));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_gongzhuo:
                Intent it_gongzhuo = new Intent(getActivity(), GongZhuoActivity.class);
                startActivity(it_gongzhuo);
                break;
            case R.id.home_xianglu:
                Intent it_xianglu = new Intent(getActivity(), XiangLuActivity.class);
                startActivity(it_xianglu);
                break;
            case R.id.home_guzhong:
                Intent it_guzhong = new Intent(getActivity(), GuZhongActivity.class);
                startActivity(it_guzhong);
                break;
            case R.id.home_simiao:
                Intent it_simiao = new Intent(getActivity(), SiMiaoActivity.class);
                startActivity(it_simiao);
                break;
            case R.id.home_zhuangshi:
                Intent it_zhuangshi = new Intent(getActivity(), ZhuangShiActivity.class);
                startActivity(it_zhuangshi);
                break;
            case R.id.home_sengfu:
                Intent it_sengfu = new Intent(getActivity(), SengFuActivity.class);
                startActivity(it_sengfu);
                break;
            case R.id.home_faqi:
                Intent it_faqi = new Intent(getActivity(), FaQiActivity.class);
                startActivity(it_faqi);
                break;
            case R.id.home_foxiang:
                Intent it_foxiang = new Intent(getActivity(), FoXiangDingZhiActivity.class);
                startActivity(it_foxiang);
                break;

        }
    }

    public void init() {
        images = new int[]{R.drawable.a,
                R.drawable.b_,
                R.drawable.c_,
                R.drawable.d_,
                R.drawable.e_};
        titles = new String[]{"",
                "",
                "",
                "",
                ""};
        imageSource = new ArrayList<ImageView>();
        for (int i = 0; i < images.length; i++) {
            ImageView image = new ImageView(getActivity());
            image.setBackgroundResource(images[i]);
            imageSource.add(image);
        }

        dots = new ArrayList<View>();
        dots.add(getActivity().findViewById(R.id.dot1));
        dots.add(getActivity().findViewById(R.id.dot2));
        dots.add(getActivity().findViewById(R.id.dot3));
        dots.add(getActivity().findViewById(R.id.dot4));
        dots.add(getActivity().findViewById(R.id.dot5));

        title = (TextView) getActivity().findViewById(R.id.title);
        title.setText(titles[0]);

        viewPager = (ViewPager) getActivity().findViewById(R.id.vp);
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

    private class MyPageChangeListener implements OnPageChangeListener {
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

    };

    /**
     * 头标点击监听
     */
    private class tabOnClickListener implements View.OnClickListener {
        private int index = 0;

        private tabOnClickListener(int index) {
            this.index = index;
        }

        @Override
        public void onClick(View v) {
            switch (index) {
                case 0:
                    tab1_gongzhuo.setTextColor(selectedColor);
                    tab1_xianglu.setTextColor(unSelectedColor);
                    tab1_guzhong.setTextColor(unSelectedColor);
                    tab1_simiao.setTextColor(unSelectedColor);

                    tab1_gongzhuo_iv.setBackgroundColor(tabSelectedColor);
                    tab1_xianglu_iv.setBackgroundColor(tabUnSelectedColor);
                    tab1_guzhong_iv.setBackgroundColor(tabUnSelectedColor);
                    tab1_simiao_iv.setBackgroundColor(tabUnSelectedColor);

                    break;
                case 1:
                    tab1_gongzhuo.setTextColor(unSelectedColor);
                    tab1_xianglu.setTextColor(selectedColor);
                    tab1_guzhong.setTextColor(unSelectedColor);
                    tab1_simiao.setTextColor(unSelectedColor);

                    tab1_gongzhuo_iv.setBackgroundColor(tabUnSelectedColor);
                    tab1_xianglu_iv.setBackgroundColor(tabSelectedColor);
                    tab1_guzhong_iv.setBackgroundColor(tabUnSelectedColor);
                    tab1_simiao_iv.setBackgroundColor(tabUnSelectedColor);
                    break;
                case 2:
                    tab1_gongzhuo.setTextColor(unSelectedColor);
                    tab1_xianglu.setTextColor(unSelectedColor);
                    tab1_guzhong.setTextColor(selectedColor);
                    tab1_simiao.setTextColor(unSelectedColor);

                    tab1_gongzhuo_iv.setBackgroundColor(tabUnSelectedColor);
                    tab1_xianglu_iv.setBackgroundColor(tabUnSelectedColor);
                    tab1_guzhong_iv.setBackgroundColor(tabSelectedColor);
                    tab1_simiao_iv.setBackgroundColor(tabUnSelectedColor);
                    break;
                case 3:
                    tab1_gongzhuo.setTextColor(unSelectedColor);
                    tab1_xianglu.setTextColor(unSelectedColor);
                    tab1_guzhong.setTextColor(unSelectedColor);
                    tab1_simiao.setTextColor(selectedColor);

                    tab1_gongzhuo_iv.setBackgroundColor(tabUnSelectedColor);
                    tab1_xianglu_iv.setBackgroundColor(tabUnSelectedColor);
                    tab1_guzhong_iv.setBackgroundColor(tabUnSelectedColor);
                    tab1_simiao_iv.setBackgroundColor(tabSelectedColor);
                    break;
            }
            viewPagerTab.setCurrentItem(index);

        }
    }

    private class tabOnClickListener2 implements View.OnClickListener {
        private int index = 0;

        private tabOnClickListener2(int index) {
            this.index = index;
        }

        @Override
        public void onClick(View v) {
            switch (index) {
                case 0:
                    tab2_zhuangshi.setTextColor(selectedColor);
                    tab2_sengfu.setTextColor(unSelectedColor);
                    tab2_faqi.setTextColor(unSelectedColor);
                    tab2_fuxiang.setTextColor(unSelectedColor);

                    tab2_zhuangshi_iv.setBackgroundColor(tabSelectedColor);
                    tab2_sengfu_iv.setBackgroundColor(tabUnSelectedColor);
                    tab2_faqi_iv.setBackgroundColor(tabUnSelectedColor);
                    tab2_fuxiang_iv.setBackgroundColor(tabUnSelectedColor);

                    break;
                case 1:
                    tab2_zhuangshi.setTextColor(unSelectedColor);
                    tab2_sengfu.setTextColor(selectedColor);
                    tab2_faqi.setTextColor(unSelectedColor);
                    tab2_fuxiang.setTextColor(unSelectedColor);

                    tab2_zhuangshi_iv.setBackgroundColor(tabUnSelectedColor);
                    tab2_sengfu_iv.setBackgroundColor(tabSelectedColor);
                    tab2_faqi_iv.setBackgroundColor(tabUnSelectedColor);
                    tab2_fuxiang_iv.setBackgroundColor(tabUnSelectedColor);
                    break;
                case 2:
                    tab2_zhuangshi.setTextColor(unSelectedColor);
                    tab2_sengfu.setTextColor(unSelectedColor);
                    tab2_faqi.setTextColor(selectedColor);
                    tab2_fuxiang.setTextColor(unSelectedColor);

                    tab2_zhuangshi_iv.setBackgroundColor(tabUnSelectedColor);
                    tab2_sengfu_iv.setBackgroundColor(tabUnSelectedColor);
                    tab2_faqi_iv.setBackgroundColor(tabSelectedColor);
                    tab2_fuxiang_iv.setBackgroundColor(tabUnSelectedColor);
                    break;
                case 3:
                    tab2_zhuangshi.setTextColor(unSelectedColor);
                    tab2_sengfu.setTextColor(unSelectedColor);
                    tab2_faqi.setTextColor(unSelectedColor);
                    tab2_fuxiang.setTextColor(selectedColor);

                    tab2_zhuangshi_iv.setBackgroundColor(tabUnSelectedColor);
                    tab2_sengfu_iv.setBackgroundColor(tabUnSelectedColor);
                    tab2_faqi_iv.setBackgroundColor(tabUnSelectedColor);
                    tab2_fuxiang_iv.setBackgroundColor(tabSelectedColor);
                    break;
            }
            viewPagerTab2.setCurrentItem(index);

        }
    }

    /**
     * 定义适配器
     */
    class tabPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragmentList;
        private FragmentManager fragmentManager;

        /**
         * 这个构造方法是必须写的
         * 而且必须要重写getitem方法
         *
         * @param fragmentManager
         * @param fragmentList
         */
        public tabPagerAdapter(FragmentManager fragmentManager, List<Fragment> fragmentList) {
            super(fragmentManager);
            this.fragmentManager = fragmentManager;
            this.fragmentList = fragmentList;
        }

        public void setFragments(ArrayList<Fragment> fragments) {
            if (this.fragmentList != null) {
                FragmentTransaction ft = fragmentManager.beginTransaction();
                for (Fragment f : this.fragmentList) {
                    ft.remove(f);
                }
                ft.commit();
                ft = null;
                fragmentManager.executePendingTransactions();
            }
            this.fragmentList = fragments;
            notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int i) {
            return fragmentList.get(i);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }


    }

    /**
     * 定义适配器
     */
    class tabPagerAdapter2 extends FragmentPagerAdapter {
        private List<Fragment> fragmentList;
        private FragmentManager fragmentManager;

        /**
         * 这个构造方法是必须写的
         * 而且必须要重写getitem方法
         *
         * @param fragmentManager
         * @param fragmentList
         */
        public tabPagerAdapter2(FragmentManager fragmentManager, List<Fragment> fragmentList) {
            super(fragmentManager);
            this.fragmentManager = fragmentManager;
            this.fragmentList = fragmentList;
        }

        public void setFragments(ArrayList<Fragment> fragments) {
            if (this.fragmentList != null) {
                FragmentTransaction ft = fragmentManager.beginTransaction();
                for (Fragment f : this.fragmentList) {
                    ft.remove(f);
                }
                ft.commit();
                ft = null;
                fragmentManager.executePendingTransactions();
            }
            this.fragmentList = fragments;
            notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int i) {
            return fragmentList.get(i);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }


    }
}
