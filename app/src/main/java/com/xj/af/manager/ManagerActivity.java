package com.xj.af.manager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.xj.af.R;
import com.xj.af.common.BaseActivity;
import com.xj.af.common.BaseBackActivity;
import com.xj.af.common.Constant;

public class ManagerActivity extends BaseBackActivity implements View.OnClickListener {
    /**每日开始*/
    private Button mrks;
    /**寺院动态*/
    private Button sydt;
    /**法会信息*/
    private Button fhxx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_manager);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.title_bar);
        title="信息发布";
        init();
    }
    private void init(){
        mrks = (Button)findViewById(R.id.manager_mrks_button1);
        sydt = (Button)findViewById(R.id.manager_sydt_button2);
        fhxx = (Button)findViewById(R.id.manager_fhxx_button3);
        mrks.setOnClickListener(this);
        sydt.setOnClickListener(this);
        fhxx.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        Intent it = new Intent(ManagerActivity.this,AddNewsActivity.class);
        switch (view.getId()) {
            case R.id.manager_mrks_button1:
                it.putExtra("enName", Constant.NEWSSORT_ENNAME_jinRiShuoFa);
                break;
            case R.id.manager_fhxx_button3:
                it.putExtra("enName", Constant.NEWSSORT_ENNAME_fahuiInfo);
                break;
            case R.id.manager_sydt_button2:
                it.putExtra("enName", Constant.NEWSSORT_ENNAME_siYuanDongTai);
                break;
        }
        startActivity(it);
    }




}
