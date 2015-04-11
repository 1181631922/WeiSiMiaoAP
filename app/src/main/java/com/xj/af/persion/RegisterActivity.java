package com.xj.af.persion;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xj.af.R;
import com.xj.af.common.BaseActivity;
import com.xj.af.util.StrUtil;
import com.xj.af.util.http.PostUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends BaseActivity {

    private EditText username;
    private EditText password;
    private EditText password2;
    private EditText email;
    private EditText nickName;
    private Button registerBtn;
    private Button backBtn;
    private Handler handler;
    private TextView error;
    private LinearLayout regLL;
    private Button loginBut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        handler = new MyHandler();
        init();
        verifyForm();
    }


    private void init() {
        username=(EditText)this.findViewById(R.id.register_username_editText);
        password=(EditText)this.findViewById(R.id.register_password_editText);
        password2=(EditText)this.findViewById(R.id.register_password2_editText);
        email=(EditText)this.findViewById(R.id.register_email_editText);
        nickName=(EditText)this.findViewById(R.id.register_nickName_editText);
        registerBtn=(Button)this.findViewById(R.id.register_button);
        backBtn = (Button)this.findViewById(R.id.register_back_button);
        error = (TextView) this.findViewById(R.id.register_error_textView);
        regLL = (LinearLayout)this.findViewById(R.id.register_linearLayout);
        loginBut = (Button)this.findViewById(R.id.register_login_button);
    }


    /*校验表单*/
    private boolean verify(){
        String pass1 = password.getText().toString();
        String pass2 = password2.getText().toString();
        String user = username.getText().toString();
        String nick = nickName.getText().toString();
        String emailstr = email.getText().toString();
        if(StrUtil.isBlank(pass1, pass2, user, nick, emailstr)){
            error.setText("注意：表单提示为必填项的都需要填写。");
            return false;
        }
        if(!StrUtil.isEmail(emailstr)){
            error.setText("注意：邮件地址格式错误。");
            return false;
        }
        if(!pass1.equals(pass2) ){
            error.setText("注意：输入的2次密码不一样。");
            return false;
        }
        if(user.length()<3 || (!StrUtil.isNumAndZiMu(user))){
            error.setText("注意：用户名输入有误。");
            return false;
        }
        return true;
    }



    private class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            Bundle bd = msg.getData();
            String imsg = bd.getString("imsg");
            String emsg = bd.getString("emsg");
            if(!StrUtil.isBlank(emsg)){
                //注册失败
                error.setText(emsg);
            }else if(!StrUtil.isBlank(imsg) && imsg.indexOf("success")==0){
                //注册成功
                regLL.setVisibility(View.GONE);
                error.setText("注册成功。");
                loginBut.setVisibility(View.VISIBLE);
                loginBut.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        Intent it = new Intent(RegisterActivity.this,PersionActivity.class);
                        startActivity(it);
                    }
                });
            }else if(!StrUtil.isBlank(imsg) && imsg.indexOf("userAlreadyUsed")==0){
                //用户已存在
                error.setText("用户名已注册，改下再注册吧。");
            }
            else{
                //注册失败
                error.setText("注册失败，请稍后重试");
            }


        }
    }

    private class MyThread extends Thread{
        @Override
        public void run() {
            Message msg = new Message();
            Bundle bd = new Bundle();
            String url = getServerURL()+"/api/account/register/"+getUnitId();
            Map<String,String> map = new HashMap<String,String>();
            map.put("loginName",username.getText().toString());
            map.put("plainPassword",password.getText().toString());
            map.put("name",nickName.getText().toString());
            map.put("email",email.getText().toString());
            map.put("unit.id",getUnitId());
            try {
                String str = PostUtil.postData(url, map);
                bd.putString("imsg", str);
            } catch (IOException e) {
                e.printStackTrace();
                bd.putString("emsg", e.getMessage());
            }
            msg.setData(bd);
            RegisterActivity.this.handler.sendMessage(msg);
        }
    }

    private void verifyForm() {
		/*单位用户注册 /register/checkLoginName  /api/account/register/{unitId}*/
        registerBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                if(verify()){//校验表单
                    MyThread mt = new MyThread();
                    mt.start();
                }
            }
        });
		/*返回注册界面*/
        backBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
		/*检查2次输入的密码是否一致*/
        password2.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                String pass1 = password.getText().toString();
                String pass2 = password2.getText().toString();
                if(!StrUtil.isBlank(pass1,pass2)){
                    if(!pass1.equals(pass2)){
                        error.setText("注意：2次输入的密码不一样");
                    }else{
                        error.setText("");
                    }
                }
            }
        });
		/*检查用户名*/
        username.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                String user = username.getText().toString();
                if(!StrUtil.isBlank(user)){
                    if(!StrUtil.isNumAndZiMu(user)){
                        error.setText("注意：用户名只能为数字字母或下划线");
                    }else if(user.length()<3){
                        error.setText("注意：用户名至少为3个字母");
                    }else{
                        error.setText("");
                    }
                }
            }
        });
		/*检查邮件地址*/
        email.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                String emailstr = email.getText().toString();
                if(!StrUtil.isBlank(emailstr) && (!StrUtil.isEmail(emailstr))){
                    error.setText("注意：邮件地址格式错误。");
                }else{
                    error.setText("");
                }
            }
        });
    }
}
