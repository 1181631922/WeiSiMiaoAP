package com.xj.af.index.jieyuan;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xj.af.R;
import com.xj.af.common.BaseBackActivity;
import com.xj.af.common.Constant;
import com.xj.af.util.StrUtil;
import com.xj.af.util.ThreadUtil;
import com.xj.af.util.http.PostUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 结缘详细
 */
public class JieYuanDetailActivity extends BaseBackActivity implements View.OnClickListener{
    private EditText jieyuan_detail_address;
    private EditText jieyuan_detail_phonenumber;
    private EditText jieyuan_detail_num;
    private Button jieyuan_submit;
    private Button jieyuan_back_btn;
    private TextView jieyuan_errormsg ;
    private TextView jieyuan_result;
    private TextView jieyuan_des;
    private LinearLayout jieyuan_input_layout;
    private Handler handler;
    private long jingShuId;//id
    private String name;//经书
    private String picPath;//图片路径
    private String des;//经书描述

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean flag = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_jie_yuan_detail);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar);
        title = "结缘";
        init();
        SharedPreferences sharedata = getSharedPreferences(Constant.USER_DATA, 0);
        jieyuan_detail_address.setText( sharedata.getString(Constant.USER_DATA_ADDRESS,""));
        jieyuan_detail_phonenumber.setText(sharedata.getString(Constant.USER_DATA_PHONENUM,""));
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.jieyuan_back_btn){
            this.finish();
        }else if(v.getId() == R.id.jieyuan_submit) {
            Message msg = new Message();
            Bundle bd = new Bundle();
            msg.setData(bd);
            String str = checkInput();
            if (StrUtil.isBlank(str)) {
                SharedPreferences.Editor sharedata = getSharedPreferences(Constant.USER_DATA, 0).edit();
                sharedata.putString(Constant.USER_DATA_ADDRESS, jieyuan_detail_address.getText().toString());
                sharedata.putString(Constant.USER_DATA_PHONENUM, jieyuan_detail_phonenumber.getText().toString());
                sharedata.commit();
                Map map = new HashMap();
                map.put("phoneNum", jieyuan_detail_phonenumber.getText().toString());
                map.put("jieYuanJingShu.id", new Long(jingShuId).toString());
                map.put("address", jieyuan_detail_address.getText().toString());
                map.put("num", jieyuan_detail_num.getText().toString());
                map.put("type", "1");
                ThreadUtil tu = new ThreadUtil(handler, getServerURL() + "/api/jieyuan/add/jydingdan", map);
                tu.start();
            } else {
                bd.putString("emsg", str);
                this.handler.sendMessage(msg);
            }
        }
    }
    class MyHandler extends  Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bd = msg.getData();
            String emsg = bd.getString("emsg");
            String imsg = bd.getString("imsg");
            if(StrUtil.isNotBlank(emsg)){
                jieyuan_errormsg.setText(emsg);
                return;
            }else if(StrUtil.isNotBlank(imsg)){
                //保存成功
                if(StrUtil.isNotBlank(imsg))
                    jieyuan_result.setText(imsg);
                jieyuan_result.setVisibility(View.VISIBLE);
                jieyuan_input_layout.setVisibility(View.GONE);
                jieyuan_back_btn.setVisibility(View.VISIBLE);
            }
        }
    }
    private String checkInput(){
        String flag = "";
        if(!StrUtil.minLenth(jieyuan_detail_address.getText().toString(), 5)){
            flag = ("地址长度最少为5");
        }else
        if(!StrUtil.isMobileNO (jieyuan_detail_phonenumber.getText().toString())){
            flag = ("电话填写错误");
        }else
        if((!StrUtil.isNumeric(jieyuan_detail_num.getText().toString())) || StrUtil.isBlank(jieyuan_detail_num.getText().toString())){
            flag = ("数量填写错误");
        }
        return flag;
    }



    private void init(){
        jieyuan_detail_address = (EditText) findViewById(R.id.jieyuan_detail_address);
        jieyuan_detail_phonenumber =  (EditText)findViewById(R.id.jieyuan_detail_phonenumber);
        jieyuan_detail_num =  (EditText)findViewById(R.id.jieyuan_detail_num);
        jieyuan_submit = (Button)findViewById(R.id.jieyuan_submit);
        jieyuan_errormsg = (TextView)findViewById(R.id.jieyuan_errormsg);
        jieyuan_result = (TextView)findViewById(R.id.jieyuan_result);
        jieyuan_input_layout = (LinearLayout)findViewById(R.id.jieyuan_input_layout);
        jieyuan_des = (TextView)findViewById(R.id.jieyuan_des);
        jieyuan_back_btn = findButton(R.id.jieyuan_back_btn);

        Intent it = this.getIntent();
        jingShuId = it.getLongExtra("id",0l);
        name = it.getStringExtra("title");
        des = it.getStringExtra("des");
        picPath = it.getStringExtra("picPath");

        jieyuan_des.setText(name+":"+des);

        handler = new MyHandler();
        jieyuan_submit.setOnClickListener(this);
        jieyuan_back_btn.setOnClickListener(this);
    }



}
