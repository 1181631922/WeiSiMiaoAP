package com.xj.af.guide;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.IndicatorViewPager.IndicatorViewPagerAdapter;
import com.xj.af.R;
import com.xj.af.main.TabMainActivity;

public class GuideActivity extends Activity {
    private IndicatorViewPager indicatorViewPager;
    private LayoutInflater inflate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ViewPager viewPager = (ViewPager) findViewById(R.id.guide_viewPager);
        Indicator indicator = (Indicator) findViewById(R.id.guide_indicator);
        indicatorViewPager = new IndicatorViewPager(indicator, viewPager);
        inflate = LayoutInflater.from(getApplicationContext());

        indicatorViewPager.setAdapter(adapter);

    }

    private IndicatorViewPager.IndicatorPagerAdapter adapter = new IndicatorViewPagerAdapter() {
        private int[] images = {R.drawable.p1, R.drawable.p2, R.drawable.p3, R.drawable.p4};

        @Override
        public View getViewForTab(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = inflate.inflate(R.layout.tab_guide, container, false);
            }
            return convertView;
        }

        @Override
        public View getViewForPage(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = new View(getApplicationContext());
                convertView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            }
            if(position==3){
                convertView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Intent it = new Intent(getApplicationContext(), TabMainActivity.class) ;
                        startActivity(it);
                        finish();
                    }
                });
            }else{
                convertView.setOnClickListener(null);
            }
            convertView.setBackgroundResource(images[position]);


            return convertView;
        }

        @Override
        public int getCount() {
            return images.length;
        }
    };



}
