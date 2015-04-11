package com.xj.af.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Window;
import android.widget.FrameLayout;

import com.xj.af.R;
import com.xj.af.common.BaseBackFragmentActivity;

public class NewsSortActivity extends BaseBackFragmentActivity implements NewsSortFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean flag = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_news_sort);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar);
        FrameLayout fl = (FrameLayout)findViewById(R.id.news_sort_container);
        FragmentManager fm = getSupportFragmentManager();
        Intent it = this.getIntent();
        title = it.getStringExtra("title");
        String url = getServerURL()+"/api/newssort/news/page/"+it.getLongExtra("id",0l);
        NewsSortFragment nsf = NewsSortFragment.newInstance(url,it.getStringExtra("title"),it.getLongExtra("id",0l),it.getStringExtra("enName"));
        fm.beginTransaction().add(R.id.news_sort_container,nsf).commit();
    }

    @Override
    public void onFragmentInteraction(String id) {

    }
}
