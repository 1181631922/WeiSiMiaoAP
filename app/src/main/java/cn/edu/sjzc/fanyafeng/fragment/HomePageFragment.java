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
    private ImageButton home_frag_one_ib,home_frag_two_ib,home_frag_three_ib,home_frag_four_ib;

    //自定义viewpager小模块
    private ViewPager viewPagerTab;
    private ImageView imageView;
    private TextView tab1_gongzhuo, tab1_xianglu, tab1_guzhong, tab1_simiao;
    private List<Fragment> fragments;
    private int offset;
    private int currIndex = 0;
    private int bmpW;
    private int selectedColor, unSelectedColor;
    private static final int pageSizeTab = 4;
    private static String TAG = "生命周期";
    private ImageView ceshitupian;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("-------------------第二个-----------------onCreateView", TAG);
        return inflater.inflate(R.layout.fragment_homepage, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("--------------------第四个------------------onActivityCreated", TAG);
        init();
        initView();
        InitTextView();
        InitViewPager();
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
//        //viewpager
//        selectedColor = getResources().getColor(R.color.tab_title_pressed_color);
//        unSelectedColor = getResources().getColor(R.color.tab_title_normal_color);
//        InitImageView();

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("-------------------第三个------------------onViewCreated", TAG);
//        InitTextView();
//        InitViewPager();
    }

    @Override
    public void onAttach(Activity activity) {
        Log.d("-------------------第一个------------------onAttach", TAG);
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
        viewPagerTab.setOnPageChangeListener(new TabOnPageChangeListener());
    }

    /**
     * 初始化头标
     */

    private void InitTextView() {
        //viewpager
        this.selectedColor = getActivity().getResources().getColor(R.color.tab_title_pressed_color);
        this.unSelectedColor = getActivity().getResources().getColor(R.color.tab_title_normal_color);

        tab1_gongzhuo = (TextView) getActivity().findViewById(R.id.tab1_gongzhuo);
        tab1_xianglu = (TextView) getActivity().findViewById(R.id.tab1_xianglu);
        tab1_guzhong = (TextView) getActivity().findViewById(R.id.tab1_guzhong);
        tab1_simiao = (TextView) getActivity().findViewById(R.id.tab1_simiao);

        tab1_gongzhuo.setTextColor(selectedColor);
        tab1_xianglu.setTextColor(unSelectedColor);
        tab1_guzhong.setTextColor(unSelectedColor);
        tab1_simiao.setTextColor(unSelectedColor);

        tab1_gongzhuo.setText("供桌/佛龛");
        tab1_xianglu.setText("香炉/大鼎");
        tab1_guzhong.setText("鼓钟定制");
        tab1_simiao.setText("寺庙/建筑");

        tab1_gongzhuo.setOnClickListener(new tabOnClickListener(0));
        tab1_xianglu.setOnClickListener(new tabOnClickListener(1));
        tab1_guzhong.setOnClickListener(new tabOnClickListener(2));
        tab1_simiao.setOnClickListener(new tabOnClickListener(3));
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
        images = new int[]{R.drawable.a_,
                R.drawable.b_,
                R.drawable.c_,
                R.drawable.d_,
                R.drawable.e_};
        titles = new String[]{"请欣赏寺庙",
                "请欣赏寺庙",
                "请欣赏寺庙",
                "请欣赏寺庙",
                "请欣赏寺庙"};
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
                    break;
                case 1:
                    tab1_gongzhuo.setTextColor(unSelectedColor);
                    tab1_xianglu.setTextColor(selectedColor);
                    tab1_guzhong.setTextColor(unSelectedColor);
                    tab1_simiao.setTextColor(unSelectedColor);
                    break;
                case 2:
                    tab1_gongzhuo.setTextColor(unSelectedColor);
                    tab1_xianglu.setTextColor(unSelectedColor);
                    tab1_guzhong.setTextColor(selectedColor);
                    tab1_simiao.setTextColor(unSelectedColor);
                    break;
                case 3:
                    tab1_gongzhuo.setTextColor(unSelectedColor);
                    tab1_xianglu.setTextColor(unSelectedColor);
                    tab1_guzhong.setTextColor(unSelectedColor);
                    tab1_simiao.setTextColor(selectedColor);
                    break;
            }
            viewPagerTab.setCurrentItem(index);

        }
    }

    public class TabOnPageChangeListener implements OnPageChangeListener {
        int one = offset * 3 + bmpW;//页卡1到页卡2的偏移量
        int two = one * 3;//页卡1到页卡3的偏移量

        public void onPageScrollStateChanged(int index) {
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }


        @Override
        public void onPageSelected(int position) {
            Animation animation = new TranslateAnimation(one * currIndex, one * position, 0, 0);
            currIndex = position;
            animation.setFillAfter(true);
            animation.setDuration(300);

            switch (position) {
                case 0:
                    tab1_gongzhuo.setTextColor(selectedColor);
                    tab1_xianglu.setTextColor(unSelectedColor);
                    tab1_guzhong.setTextColor(unSelectedColor);
                    tab1_simiao.setTextColor(unSelectedColor);
                    break;
                case 1:
                    tab1_gongzhuo.setTextColor(unSelectedColor);
                    tab1_xianglu.setTextColor(selectedColor);
                    tab1_guzhong.setTextColor(unSelectedColor);
                    tab1_simiao.setTextColor(unSelectedColor);
                    break;
                case 2:
                    tab1_gongzhuo.setTextColor(unSelectedColor);
                    tab1_xianglu.setTextColor(unSelectedColor);
                    tab1_guzhong.setTextColor(selectedColor);
                    tab1_simiao.setTextColor(unSelectedColor);
                    break;
                case 3:
                    tab1_gongzhuo.setTextColor(unSelectedColor);
                    tab1_xianglu.setTextColor(unSelectedColor);
                    tab1_guzhong.setTextColor(unSelectedColor);
                    tab1_simiao.setTextColor(selectedColor);
                    break;
            }
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
            if(this.fragmentList != null){
                FragmentTransaction ft = fragmentManager.beginTransaction();
                for(Fragment f:this.fragmentList){
                    ft.remove(f);
                }
                ft.commit();
                ft=null;
                fragmentManager.executePendingTransactions();
            }
            this.fragmentList = fragments;
            notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        /**
         * 得到每个页面
         *
         * @param i
         * @return
         */
        @Override
        public Fragment getItem(int i) {
//            return (fragmentList == null || fragmentList.size() == 0) ? null : fragmentList.get(i);
//HomePageFragment homePageFragment =new HomePageFragment();
//            Fragment gongZhuoFragment = new GongZhuoFragment();
            return fragmentList.get(i);
//            return gongZhuoFragment;
        }

        /**
         * 得到每个title
         *
         * @param position
         * @return
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
//GongZhuoFragment gongZhuoFragment =(GongZhuoFragment)super.instantiateItem(container, position);
//            String title = fragmentList.get(position);
//            gongZhuoFragment.setT
            return super.instantiateItem(container, position);
        }

        /**
         * 页面的总个数
         *
         * @return
         */
        @Override
        public int getCount() {
//            return fragmentList == null ? 0 : fragmentList.size();
            return fragmentList.size();
        }


    }
}
