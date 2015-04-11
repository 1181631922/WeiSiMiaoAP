package com.xj.af.common;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xj.af.util.Contant;
import com.xj.af.util.StrUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by xiaojia on 2015/1/30.
 */
public class BaseActivity extends Activity {
    private String serverURL;
    private String unitId;
    private String alipayEnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //serverURL = getString(R.string.server_url);
        try {
            Properties assertsPro = new Properties();
            InputStream assertsInputStream = getAssets().open("unit.properties");
            assertsPro.load(assertsInputStream);
            unitId = assertsPro.getProperty("unitId");
            serverURL = assertsPro.getProperty("serverUrl");
            alipayEnable = assertsPro.getProperty("ALIPAY_ENABLE");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public boolean getLogined() {
        SharedPreferences sharedata = getSharedPreferences("data", 0);
        String username = sharedata.getString(Contant.LOGIN_DATA_USERNAME, "");
        String password = sharedata.getString(Contant.LOGIN_DATA_PASSWORD, "");
        boolean logined = sharedata.getBoolean(Contant.LOGIN_DATA_LOGINED,
                false);
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password))
            return false;
        return logined;
    }


    public String getUsername() {
        SharedPreferences sharedata = getSharedPreferences("data", 0);
        if (getLogined())
            return sharedata.getString(Contant.LOGIN_DATA_USERNAME, "");
        else
            return "";
    }

    public String getUrl(String str) {
        return serverURL + str;
    }

    public String getServerURL() {
        return serverURL;
    }

    public String getUnitId() {
        return unitId;
    }

    public String getAlipayEnable() {
        return alipayEnable;
    }

    public TextView findTextView(int id) {
        return (TextView) findViewById(id);
    }

    public EditText findEditText(int id) {
        return (EditText) findViewById(id);
    }

    public LinearLayout findLinearLayout(int id) {
        return (LinearLayout) findViewById(id);
    }

    public Button findButton(int id) {
        return (Button) findViewById(id);
    }

    public void alert(String str) {
        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
    }
}
