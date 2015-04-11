package com.xj.af.persion;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xj.af.R;
import com.xj.af.common.BaseActivity;
import com.xj.af.common.Constant;
import com.xj.af.util.StrUtil;
import com.xj.af.util.http.PostUtil;
import com.xj.af.wenda.QuestionAddActivity;


public class PersionActivity extends BaseActivity implements OnClickListener, OnTouchListener{
    private Button regeditBtn;
    private Button loginBtn;
    private EditText usernameET;
    private EditText passwordET;
    private LinearLayout loginLayout;
    private LinearLayout loginedLayout;
    private MyHandler myHandler;
    private TextView tv;
    private TextView huifuTv;
    private TextView suixiTv;
    private TextView gdlTv;
    private RelativeLayout rtv;
    private RelativeLayout rhuifuTv;
    private RelativeLayout rsuixiTv;
    private RelativeLayout rgdlTv;
    private RelativeLayout newsRL;
    String username = "";
    String password = "";
    private String loginType = "clickLogin"	;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persion);
        //初始化
        regeditBtn =       (Button) this.findViewById(R.id.persion_regedit_button);
        loginBtn =         (Button) this.findViewById(R.id.persion_login_button);
        usernameET =       (EditText)this.findViewById(R.id.persion_username_editText);
        passwordET =       (EditText)this.findViewById(R.id.persion_password_editText);
        tv =               (TextView)this.findViewById(R.id.persion_wenti_textView);
        huifuTv =          (TextView)this.findViewById(R.id.persion_huifu_textView);
        suixiTv =          (TextView)this.findViewById(R.id.persion_suixi_textView);
        gdlTv =            (TextView)this.findViewById(R.id.persion_gdl_textView);
        rtv =              (RelativeLayout)this.findViewById(R.id.persion_wenti_relative);
        rhuifuTv =         (RelativeLayout)this.findViewById(R.id.persion_huifu_relative);
        rsuixiTv =         (RelativeLayout)this.findViewById(R.id.persion_suixi_relative);
        rgdlTv =           (RelativeLayout)this.findViewById(R.id.persion_gdl_relative);
        newsRL =           (RelativeLayout)this.findViewById(R.id.persion_news_relative);
        loginLayout =      (LinearLayout) this.findViewById(R.id.persion_login_linearLayout);
        loginedLayout =    (LinearLayout)this.findViewById(R.id.persion_logined_linearLayout);
        Button buttonOut = (Button)this.findViewById(R.id.persion_loginout_button);
        SharedPreferences sharedata = getSharedPreferences(Constant.LOGIN_DATA, 0);
        username =          sharedata.getString(Constant.LOGIN_DATA_USERNAME, "");
        password =          sharedata.getString(Constant.LOGIN_DATA_PASSWORD, "");
        boolean logined =   sharedata.getBoolean(Constant.LOGIN_DATA_LOGINED, false);
        boolean hasUnitRole = sharedata.getBoolean(Constant.LOGIN_DATA_HASUNITROLE, false);
        myHandler = new MyHandler();
        //检查登录状态，如果已经登陆显示登陆状态，
        //如果已经登录隐藏登录控件
        if(logined){
            loginLayout.setVisibility(View.GONE);
            loginedLayout.setVisibility(View.VISIBLE);
            if(hasUnitRole){
                newsRL.setVisibility(View.VISIBLE);
            }else{
                newsRL.setVisibility(View.GONE);
            }
        }else if(!username.equals("")&& !password.equals("")){
            //如果没有登录，检查用户名和密码是否为空，如果不为空则自动登陆
            loginType = "auto";
            MyThread thread = new MyThread();
            thread.start();
        }
        // 登录按钮
        loginBtn.setOnClickListener(this);
        // 注册按钮
        regeditBtn.setOnClickListener(this);
        // 退出按钮
        buttonOut.setOnClickListener(this);

        // 我的问题
        rtv.setOnClickListener(this);
        rtv.setOnTouchListener(this);
        // 我的回复
        rhuifuTv.setOnClickListener(this);
        rhuifuTv.setOnTouchListener(this);
        //法会随喜
        rsuixiTv.setOnClickListener(this);
        rsuixiTv.setOnTouchListener(this);
        //功德林布施
        rgdlTv.setOnClickListener(this);
        rgdlTv.setOnTouchListener(this);
        //新闻发布
        newsRL.setOnClickListener(this);
        newsRL.setOnTouchListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.persion_wenti_relative:
                Intent i = new Intent(PersionActivity.this, QuestionAddActivity.class);
                i.putExtra("username", username);
                i.putExtra("action","question");
                startActivity(i);
                break;
            case R.id.persion_huifu_relative:
                Intent ihuifu = new Intent(PersionActivity.this,QuestionAddActivity.class);
                ihuifu.putExtra("username", username);
                ihuifu.putExtra("action","huifu");
                startActivity(ihuifu);
                break;
            case R.id.persion_suixi_relative:
                Intent isuixi = new Intent();//(PersionActivity.this,NewsSortActivity.class);
                isuixi.putExtra("url", getServerURL()+"/api/newsreply/myfahui/"+username+"/"+getUnitId());
                isuixi.putExtra("title", "法会信息");
                startActivity(isuixi);
                break;
            case R.id.persion_login_button:
                MyThread threadlogin = new MyThread();
                threadlogin.start();
                break;
            case R.id.persion_gdl_relative:
                Intent igdl = new Intent();//(PersionActivity.this,MyGdlActivity.class);
                startActivity(igdl);
                break;
            case R.id.persion_news_relative:
                Intent inews = new Intent();//(PersionActivity.this,ManagerActivity.class);
                startActivity(inews);
                break;
            case R.id.persion_regedit_button:
                Intent it = new Intent(PersionActivity.this,RegisterActivity.class);
                it.putExtra("url",getServerURL()+ "/register");
                startActivity(it);
                break;
            case R.id.persion_loginout_button:
                loginType = "clickLogin";
                LogoutThread threadout = new LogoutThread();
                threadout.start();
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                v.setBackgroundResource(R.drawable.press_up_concor);
                break;
            case MotionEvent.ACTION_UP:
                v.setBackgroundResource(R.drawable.nor_up_concor);
                break;
        }

        return false;
    }


    @Override
    protected void onResume() {
        super.onResume();
        setTitle("个人中心");
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    //登录/登出 成功后更新界面
    public class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            Bundle b = msg.getData();
            String emsg   = b.getString("emsg");
            String imsg   = b.getString("imsg");
            String action = b.getString("action");
            if(emsg!=null){
                Toast.makeText(PersionActivity.this, emsg, Toast.LENGTH_SHORT).show();
            }else{
                try {
                    JSONObject jo = new JSONObject(imsg);
                    String result = jo.getString("result");
                    if(result.equals("success")){
                        //成功登陆
                        String hasUnitRole = jo.getString("hasUnitRole");
                        Toast.makeText(PersionActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        loginLayout.setVisibility(View.GONE);
                        loginedLayout.setVisibility(View.VISIBLE);
                        SharedPreferences.Editor sharedata = getSharedPreferences(Constant.LOGIN_DATA, 0).edit();
                        sharedata.putString(Constant.LOGIN_DATA_USERNAME,username);
                        sharedata.putString(Constant.LOGIN_DATA_PASSWORD,password);
                        sharedata.putBoolean(Constant.LOGIN_DATA_LOGINED,true);
                        if(StrUtil.isNotBlank(hasUnitRole)&& "true".equals(hasUnitRole)){
                            sharedata.putBoolean(Constant.LOGIN_DATA_HASUNITROLE, true);
                        }
                        sharedata.commit();
                    }else{
                        //登录失败或退出
                        if("login".equals(action)){
                            Toast.makeText(PersionActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                        }
                        loginLayout.setVisibility(View.VISIBLE);
                        loginedLayout.setVisibility(View.GONE);
                        SharedPreferences.Editor sharedata = getSharedPreferences(Constant.LOGIN_DATA, 0).edit();
                        sharedata.putBoolean(Constant.LOGIN_DATA_LOGINED,false);
                        sharedata.putBoolean(Constant.LOGIN_DATA_HASUNITROLE,false);
                        sharedata.putString(Constant.LOGIN_DATA_USERNAME,"");
                        sharedata.putString(Constant.LOGIN_DATA_PASSWORD,"");
                        sharedata.commit();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(PersionActivity.this, "服务器返回数据格式错误", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    //登出线程
    public class LogoutThread extends Thread {
        public void run() {
            Message msg = new Message();
            Bundle bd = new Bundle();

            try {
                String str = PostUtil.postData(getServerURL()
                        + "/api/logout", null);
                bd.putString("imsg", str);
            } catch (Exception e) {
                e.printStackTrace();
                bd.putString("emsg", e.getMessage());
            }
            bd.putString("action", "logout");
            msg.setData(bd);
            PersionActivity.this.myHandler.sendMessage(msg);
        }
    }

    //登录线程
    public class MyThread extends Thread{
        @Override
        public void run() {
            if(loginType.equals("clickLogin")){
                username = usernameET.getText().toString();
                password = passwordET.getText().toString();
            }
            Map<String, String> params = new HashMap<String, String>();
            params.put("username",username);
            params.put("password", password);
            Message msg = new Message();
            Bundle bundle = new Bundle();
            try {
                String str = PostUtil.postData(getServerURL()+"/api/login", params);
                bundle.putString("imsg", str);
            } catch (IOException e) {
                e.printStackTrace();
                bundle.putString("emsg", e.getMessage());
            }
            bundle.putString("action", "login");
            msg.setData(bundle);
            PersionActivity.this.myHandler.sendMessage(msg);
        }
    }
}
