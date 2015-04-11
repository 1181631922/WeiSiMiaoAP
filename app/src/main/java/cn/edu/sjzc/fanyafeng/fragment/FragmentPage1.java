package cn.edu.sjzc.fanyafeng.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import java.util.concurrent.ScheduledExecutorService;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xj.af.R;

import java.util.ArrayList;

public class FragmentPage1 extends Fragment {

    int[] images = null;// 图片资源ID
    String[] titles = null;// 标题

    ArrayList<ImageView> imageSource = null;// 图片资源
    ArrayList<View> dots = null;// 点
    TextView title = null;
    ViewPager viewPager;// 用于显示图片
    MyPagerAdapter adapter;// viewPager的适配器
    private int currPage = 0;// 当前显示的页
    private int oldPage = 0;// 上一次显示的页

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_1, null);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    public void init() {
        images = new int[]{R.drawable.a, R.drawable.b, R.drawable.c,
                R.drawable.d, R.drawable.e};
        titles = new String[]{"这是第1张图片", "这是第2张图片", "这是第3张图片", "这是第4张图片",
                "这是第5张图片"};
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
        scheduled.scheduleAtFixedRate(pagerTask, 2, 2, TimeUnit.SECONDS);
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

        ;
    };


}
