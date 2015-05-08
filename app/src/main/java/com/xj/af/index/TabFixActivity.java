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
import android.widget.Toast;

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
 * 佛事，结缘的Activity，标签页显示，标签页比较少，需要占满整行
 * 必须传入参数 key
 */
public class TabFixActivity extends BaseBackFragmentActivity implements FoShiFuWuFragment.OnFragmentInteractionListener,
        NewsSortFragment.OnFragmentInteractionListener,QuestionFragment.OnFragmentInteractionListener,NewsFragment.OnFragmentInteractionListener
        ,JieYuanFragment.OnFragmentInteractionListener{
    private IndicatorViewPager indicatorViewPager;
    private LayoutInflater inflate;
    private final static String [] FO_SHI  =     {"法会信息","佛事服务","寺院动态"};
    private final static String [] JIE_YUAN = {"结缘","助印"};
    private String [] tabsName = FO_SHI;
    public final static String key = "tabsName";
    private String value  = "";
    public final static String FoShi = "FO_SHI";
    public final static String JieYuan = "JIE_YUAN";
    private String action;
    private String tab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean flag = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_tab_fix);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar);
        title=getString(R.string.title_activity_tab);
        value = this.getIntent().getStringExtra(key);
        action = this.getIntent().getStringExtra("action");
        tab = this.getIntent().getStringExtra("tab");
        if(StrUtil.isNotBlank(value))
        {
            if (value.equals(FoShi)) {
                tabsName = FO_SHI;
                title = "法讯";
            }else if(value.equals(JieYuan)){
                    tabsName = JIE_YUAN;
                    title = "结缘";
            }
        }
        ViewPager viewPager = (ViewPager) findViewById(R.id.moretab_viewPager);
        FixedIndicatorView indicator = (FixedIndicatorView) findViewById(R.id.moretab_indicator);
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
            if (value.equals(TabFixActivity.FoShi)) {

                if (position == 0) {
                    String url = getServerURL() + "/api/newssort/page/fahuiInfo/" + getUnitId();
                    Log.d("--------------------------------------url",url);
                    return NewsSortFragment.newInstance(url, "", 0, "fahuiInfo");
                } else if (position == 1) {
                    return FoShiFuWuFragment.newInstance(getServerURL(), getUnitId());
                }
                else if (position == 2) {//改动寺院动态
                    String url = getServerURL() + "/api/newssort/page/siYuanDongTai/" + getUnitId();
                    Log.d("--------------------------------------url",url);
                    return NewsSortFragment.newInstance(url, "siYuanDongTai", 0, "siYuanDongTai");
                }
            }else if(value.equals( TabFixActivity.JieYuan)){
                if(position==0){//结缘
                    String url = getServerURL() + "/api/jieyuan/findall/1";
                    return JieYuanFragment.newInstance(url,"结缘");
                }else if(position==1){//助印
                    String url = getServerURL() + "/api/jieyuan/findall/2";
                    return JieYuanFragment.newInstance(url,"助印");
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
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFragmentInteraction(String str, String pas) {
        saveUserNameAndPassWord(str,pas);
    }
}
