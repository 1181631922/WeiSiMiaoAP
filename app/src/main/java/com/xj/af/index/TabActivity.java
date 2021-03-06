package com.xj.af.index;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.shizhefei.view.indicator.FixedIndicatorView;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.ScrollIndicatorView;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;
import com.xj.af.R;
import com.xj.af.common.BaseBackFragmentActivity;
import com.xj.af.common.Constant;
import com.xj.af.index.foshi.FoShiFuWuFragment;
import com.xj.af.index.jiangtang.QuestionFragment;
import com.xj.af.index.jieyuan.JieYuanFragment;
import com.xj.af.index.simiao.AbuoutFragment;
import com.xj.af.news.NewsFragment;
import com.xj.af.news.NewsSortFragment;
import com.xj.af.persion.PersionFragment;
import com.xj.af.slide.SlideFragment;
import com.xj.af.util.Contant;
import com.xj.af.util.StrUtil;

/**
 * 寺庙，佛事，讲堂  的Activity，标签页显示
 * 必须传入参数 key
 */
public class TabActivity extends BaseBackFragmentActivity implements FoShiFuWuFragment.OnFragmentInteractionListener,
        NewsSortFragment.OnFragmentInteractionListener,QuestionFragment.OnFragmentInteractionListener,NewsFragment.OnFragmentInteractionListener
        ,JieYuanFragment.OnFragmentInteractionListener{
    private IndicatorViewPager indicatorViewPager;
    private LayoutInflater inflate;
    private final static String [] HUO_DONG  =     {"",""};
    private final static String [] SI_MIAO =     {"住持","寺院概况","高僧大德","联系我们"};
    private final static String [] JIANG_TANG =  {"每日开示","讲经说法","答疑解惑","佛学基础"};
    private final static String [] FA_XIAN = {"",""};
    private String [] tabsName = HUO_DONG;
    public final static String key = "tabsName";
    private String value  = "";
    public final static String HuoDong = "HUO_DONG";
    public final static String SiMiao = "SI_MIAO";
    public final static String JiangTang = "JIANG_TANG";
    public final static String FaXian = "FA_XIAN";
    private String action;
    private String tab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean flag = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_tab);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar);
        title=getString(R.string.title_activity_tab);
        value = this.getIntent().getStringExtra(key);
        action = this.getIntent().getStringExtra("action");
        tab = this.getIntent().getStringExtra("tab");
        if(StrUtil.isNotBlank(value))
        {
            if (value.equals(HuoDong)) {
                tabsName = HUO_DONG;
                title = "活动";
            }else if (value.equals(SiMiao)) {

                tabsName = SI_MIAO;
                title = "寺庙";
            }else if(value.equals(JiangTang)) {

                tabsName = JIANG_TANG;
                title = "讲堂";
            }else if(value.equals(FaXian)) {
                    tabsName = FA_XIAN;
                    title = "发现";
            }

        }
        ViewPager viewPager = (ViewPager) findViewById(R.id.moretab_viewPager);
        ScrollIndicatorView indicator = (ScrollIndicatorView) findViewById(R.id.moretab_indicator);
        indicator.setScrollBar(new ColorBar(this, Color.RED, 6));

        // 设置滚动监听
        int selectColorId = R.color.tab_top_text_2;
        int unSelectColorId = R.color.tab_top_text_1;
        indicator.setOnTransitionListener(new OnTransitionTextListener().setColorId(this, selectColorId, unSelectColorId));
        viewPager.setOffscreenPageLimit(5);
        indicatorViewPager = new IndicatorViewPager(indicator, viewPager);
        inflate = LayoutInflater.from(getApplicationContext());
        indicatorViewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        if(StrUtil.isNotBlank(tab)){
            if(tab.equals(PersionFragment.MY_QUESTION)){
                indicatorViewPager.setCurrentItem(2,true);
            }else if(tab.equals(PersionFragment.MY_ANSWER)){
                indicatorViewPager.setCurrentItem(2,true);
            }else if(tab.equals(PersionFragment.MY_SUIXI)){
                indicatorViewPager.setCurrentItem(1,true);
            }
        }

    }
    private class MyAdapter extends IndicatorViewPager.IndicatorFragmentPagerAdapter {

        public MyAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return  tabsName.length;
        }

        @Override
        public View getViewForTab(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = inflate.inflate(R.layout.tab_top, container, false);
            }
            TextView textView = (TextView) convertView;
            textView.setText(tabsName[position]);
            textView.setPadding(30, 0, 30, 0);
            return convertView;
        }



        @Override
        public android.support.v4.app.Fragment getFragmentForPage(int position) {
            if (value.equals(HuoDong)) {

                if (position == 0) {
                    String url = getServerURL() + "/api/newssort/page/fahuiInfo/" + getUnitId();
                    return NewsSortFragment.newInstance(url, "", 0, "fahuiInfo");
                } else if (position == 1) {
                    return FoShiFuWuFragment.newInstance(getServerURL(), getUnitId());
                }
            }else if(value.equals(SiMiao)) {
                if (position == 0) {
                    String url = getServerURL() + "/m/news/newsDetail/enName/dangJiaZhuChi/" + getUnitId();
                    return NewsFragment.newInstance(url, "当家主持", 0);
                } else if (position == 1) {
                    String url = getServerURL() + "/m/news/newsDetail/enName/siYuanGaiKuang/" + getUnitId();
                    return NewsFragment.newInstance(url, "寺院概况", 0);
                } else if (position == 2) {
                    String url = getServerURL() + "/api/newssort/page/gaoSengDaDe/" + getUnitId();
                    return NewsSortFragment.newInstance(url, "gaoSengDaDe", 0, "gaoSengDaDe");
                }
//                else if (position == 3) {
//                    String url = getServerURL() + "/api/newssort/page/siYuanDongTai/" + getUnitId();
//                    Log.d("--------------------------------------url", url);
//                    return NewsSortFragment.newInstance(url, "siYuanDongTai", 0, "siYuanDongTai");
//                }
                else if (position == 3) {
                    return new AbuoutFragment();
                }
            }else if(value.equals(JiangTang)) {
                if (position == 0) {//每日开示，今日说法
                    String url = getServerURL() + "/api/newssort/page/jinRiShuoFa/" + getUnitId();
                    return NewsSortFragment.newInstance(url, "jinRiShuoFa", 0, "jinRiShuoFa");
                } else if (position == 1) {//讲经说法
                    String url = getIntent().getStringExtra("url");
                    if (StrUtil.isBlank(url))
                        url = getServerURL() + "/api/newssort/getSortByEnName/jingDianJieDu/" + getUnitId();
                    return NewsSortFragment.newInstance(url, "jingDianJieDu", 0, Constant.NEWSSORT_ENNAME_jingDianJieDu);
                } else if (position == 2) {//答疑解惑
                    return new QuestionFragment();
                } else if (position == 3) {//佛学基础
                    String url = getServerURL() + "/api/newssort/page/findByEnName/global_foXueJiChu";
                    return NewsSortFragment.newInstance(url, "global_foXueJiChu", 0, Constant.NEWSSORT_ENNAME_global_foXueJiChu);
                }
            }else if(value.equals(FaXian)){
                    if(position==0){//发现
                        return JieYuanFragment.newInstance("","");
                    }else if(position==1){//助印
                        return JieYuanFragment.newInstance("","");
                    }
            }
            MoreFragment fragment = new MoreFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(SlideFragment.INTENT_INT_INDEX, position);
            fragment.setArguments(bundle);

            return fragment;
        }

    };

    public void saveUserNameAndPassWord(String username,String password){
        SharedPreferences.Editor sharedata = getSharedPreferences(Contant.LOGIN_DATA, 0).edit();
        sharedata.putString(Contant.LOGIN_DATA_USERNAME,username);
        sharedata.putString(Contant.LOGIN_DATA_PASSWORD,password);
        sharedata.commit();
    }


    public void onFragmentInteraction(Uri uri) {

    }


    public void onFragmentInteraction(String str) {

    }

    @Override
    public void onFragmentInteraction(String str, String pas) {
        saveUserNameAndPassWord(str,pas);
    }
}
