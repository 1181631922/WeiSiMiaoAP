package com.xj.af.main;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.IndicatorViewPager.IndicatorFragmentPagerAdapter;
import com.xj.af.R;
import com.xj.af.common.BaseFragmentActivity;
import com.xj.af.gdl.GdlItemFragment;
import com.xj.af.index.IndexFragment;
import com.xj.af.persion.LoginFragment;
import com.xj.af.persion.PersionFragment;
import com.xj.af.util.Contant;
import com.xj.af.util.DataUtils;
import com.xj.af.util.StrUtil;
import com.xj.af.util.http.PostUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class TabMainActivity extends BaseFragmentActivity implements IndexFragment.OnFragmentInteractionListener,
        GdlItemFragment.OnFragmentInteractionListener, LoginFragment.OnFragmentInteractionListener,
        PersionFragment.OnFragmentInteractionListener {
    private IndicatorViewPager indicatorViewPager;
    private Button loadpic;
    private final static String ALBUM_PATH = Environment.getExternalStorageDirectory()
            + "/fanyafeng/";
    private ImageView mImageView;
    private ProgressDialog mSaveDialog = null;
    private Bitmap mBitmap;
    private String mFileName;
    private String mSaveMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_main);

        new Thread(connectNet).start();

        if (isFirstStart()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new Thread(saveFileRunnable).start();
                }
            }, 3000);
            setTarget();
        } else {
        }


        ViewPager viewPager = (ViewPager) findViewById(R.id.tabmain_viewPager);
        Indicator indicator = (Indicator) findViewById(R.id.tabmain_indicator);
        indicatorViewPager = new IndicatorViewPager(indicator, viewPager);
        fragment = getFragment();
        indicatorViewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        // 禁止viewpager的滑动事件
        viewPager.setCanScroll(true);
        // 设置viewpager保留界面不重新加载的页面数量
        viewPager.setOffscreenPageLimit(4);
        // 默认是1,，自动预加载左右两边的界面。设置viewpager预加载数为0。只加载加载当前界面。
        viewPager.setPrepareNumber(1);
        myHandler = new MyHandler();
    }

    private boolean isFirstStart() {
        SharedPreferences share = getSharedPreferences("fs", MODE_PRIVATE);
        String target = share.getString("isfs", "0");
        if (target.equals("0")) {
            return true;
        } else {
            return false;
        }
    }

    private void setTarget() {
        SharedPreferences share = getSharedPreferences("fs", MODE_PRIVATE);
        SharedPreferences.Editor editor = share.edit();
        editor.putString("isfs", "no");
        editor.commit();
    }

    public byte[] getImage(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5 * 1000);
        conn.setRequestMethod("GET");
        InputStream inStream = conn.getInputStream();
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return readStream(inStream);
        }
        return null;
    }

    public InputStream getImageStream(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5 * 1000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return conn.getInputStream();
        }
        return null;
    }

    public static byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inStream.close();
        return outStream.toByteArray();
    }

    public void saveFile(Bitmap bm, String fileName) throws IOException {
        File dirFile = new File(ALBUM_PATH);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        File myCaptureFile = new File(ALBUM_PATH + fileName);
        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
    }

    private Runnable saveFileRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                saveFile(mBitmap, mFileName);
                mSaveMessage = "图片保存成功！";
            } catch (IOException e) {
                mSaveMessage = "图片保存失败！";
                e.printStackTrace();
            }
        }
    };

    private Runnable connectNet = new Runnable() {
        @Override
        public void run() {
            try {
                String filePath = "http://a4.qpic.cn/psb?/V12cYG6y4OfMYd/qKmElerFDiCG1pQ73mTja9m2o.07cbi0Gpxeuf58lTo!/m/dGMAAAAAAAAAnull&bo=gAKAAgAAAAADByI!&rf=photolist&t=5";
                mFileName = "fanyafeng.png";

                byte[] data = getImage(filePath);
                if (data != null) {
                    mBitmap = BitmapFactory.decodeByteArray(data, 0,
                            data.length);// bitmap
                } else {
                    Toast.makeText(TabMainActivity.this, "Image error!", 1)
                            .show();
                }
                mBitmap = BitmapFactory.decodeStream(getImageStream(filePath));
            } catch (Exception e) {
                Toast.makeText(TabMainActivity.this, "无法链接网络！", 1).show();
                e.printStackTrace();
            }

        }

    };


    public Fragment fragment;

    /**
     * FragmentPage 适配器
     */
    private class MyAdapter extends IndicatorFragmentPagerAdapter {
        private String[] tabNames = DataUtils.getDataFromUnitProp(getApplicationContext(), "downMenu")[0].split(",");
        private int[] tabIcons = {R.drawable.maintab_1_selector, R.drawable.maintab_2_selector, R.drawable.maintab_4_selector,
                R.drawable.maintab_3_selector};
        private LayoutInflater inflater;

        public MyAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            inflater = LayoutInflater.from(getApplicationContext());
        }

        @Override
        public int getCount() {
            return tabNames.length;
        }

        /**
         * 获取下面4个Tab标签
         */
        @Override
        public View getViewForTab(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = (TextView) inflater.inflate(R.layout.tab_main, container, false);
            }
            TextView textView = (TextView) convertView;
            textView.setText(tabNames[position]);
            //textView.setTextColor(getResources().getColor(R.color.white));
            textView.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[position], 0, 0);
            return textView;
        }


        public Fragment indexFragment;
        public Fragment gdlFragment;
        public Fragment persionFragment;
        public Fragment loginFragment;

        /**
         * 返回对应的Fragment页面
         */
        @Override
        public Fragment getFragmentForPage(int position) {
            if (position == 0) {//首页
                indexFragment = IndexFragment.newInstance("", "");
                return indexFragment;
            } else if (position == 1) {//功德林
                gdlFragment = GdlItemFragment.newInstance(getServerURL(), getUnitId());
                return gdlFragment;
            } else if (position == 2) {//我的
                return fragment;
            }
            MainFragment mainFragment = new MainFragment();
            Bundle bundle = new Bundle();
            bundle.putString(MainFragment.INTENT_STRING_TABNAME, tabNames[position]);
            bundle.putInt(MainFragment.INTENT_INT_INDEX, position);
            mainFragment.setArguments(bundle);
            return mainFragment;
        }
    }

    public Fragment getFragment() {
        boolean login = getLogined();
        if (login) {
            SharedPreferences sharedata = getSharedPreferences("data", 0);
            // persionFragment =  PersionFragment.newInstance(sharedata.getBoolean(Contant.LOGIN_DATA_HASUNITROLE, false),getUsername());
            // return persionFragment;
            return PersionFragment.newInstance(sharedata.getBoolean(Contant.LOGIN_DATA_HASUNITROLE, false), getUsername());
        } else {
            // loginFragment =  new LoginFragment();
            // return loginFragment;
            return new LoginFragment();
        }
    }

    @Override
    public void onFragmentInteraction(String str) {
        //Toast.makeText(getApplicationContext(),str,Toast.LENGTH_SHORT).show();
        if (str.equals(PersionFragment.MY_LOGOUT)) {
            //登出
            //getSupportFragmentManager().findFragmentByTag()
            LogoutThread threadout = new LogoutThread();
            threadout.start();
        } else if (str.equals(LoginFragment.LOGIN)) {
            //登录
            loginType = "clickLogin";
            usernameET = (EditText) findViewById(R.id.persion_username_editText);
            passwordET = (EditText) findViewById(R.id.persion_password_editText);
            LoginThread lt = new LoginThread();
            lt.start();
        }
    }

    private String loginType = "";
    private EditText usernameET;
    private EditText passwordET;

    //登录线程
    public class LoginThread extends Thread {
        @Override
        public void run() {
            if (loginType.equals("clickLogin")) {
                username = usernameET.getText().toString();
                password = passwordET.getText().toString();
            }
            Map<String, String> params = new HashMap<String, String>();
            params.put("username", username);
            params.put("password", password);
            Message msg = new Message();
            Bundle bundle = new Bundle();
            try {
                String str = PostUtil.postData(getServerURL() + "/api/login", params);
                bundle.putString("imsg", str);
            } catch (IOException e) {
                e.printStackTrace();
                bundle.putString("emsg", e.getMessage());
            }
            bundle.putString("action", "login");
            msg.setData(bundle);
            TabMainActivity.this.myHandler.sendMessage(msg);
        }
    }

    //登出线程
    public class LogoutThread extends Thread {
        public void run() {
            Message msg = new Message();
            Bundle bd = new Bundle();
            try {
                String str = PostUtil.postData(getServerURL() + "/api/logout", null);
                bd.putString("imsg", str);
            } catch (Exception e) {
                e.printStackTrace();
                bd.putString("emsg", e.getMessage());
            }
            bd.putString("action", "logout");
            msg.setData(bd);
            TabMainActivity.this.myHandler.sendMessage(msg);
        }
    }

    private MyHandler myHandler;
    private String username;
    private String password;

    //登录/登出 成功后更新界面
    public class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle b = msg.getData();
            String emsg = b.getString("emsg");
            String imsg = b.getString("imsg");
            String action = b.getString("action");
            if (emsg != null) {
                Toast.makeText(TabMainActivity.this, emsg, Toast.LENGTH_SHORT).show();
            } else {
                try {
                    JSONObject jo = new JSONObject(imsg);
                    String result = jo.getString("result");
                    Log.d("-----------------TabMainjo---------------------", jo.toString());
                    FragmentManager fm = getSupportFragmentManager();
                    if (result.equals("success")) {
                        //成功登陆
                        String hasUnitRole = jo.getString("hasUnitRole");
                        Toast.makeText(TabMainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor sharedata = getSharedPreferences(Contant.LOGIN_DATA, 0).edit();
                        sharedata.putString(Contant.LOGIN_DATA_USERNAME, username);
                        sharedata.putString(Contant.LOGIN_DATA_PASSWORD, password);
                        sharedata.putBoolean(Contant.LOGIN_DATA_LOGINED, true);
                        if (StrUtil.isNotBlank(hasUnitRole) && "true".equals(hasUnitRole)) {
                            sharedata.putBoolean(Contant.LOGIN_DATA_HASUNITROLE, true);
                        }
                        sharedata.commit();
                        //TODO:显示登录成功页面

                    } else {
                        //登录失败或退出
                        if ("login".equals(action)) {
                            Toast.makeText(TabMainActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                        }
                        SharedPreferences.Editor sharedata = getSharedPreferences(Contant.LOGIN_DATA, 0).edit();
                        sharedata.putBoolean(Contant.LOGIN_DATA_LOGINED, false);
                        sharedata.putBoolean(Contant.LOGIN_DATA_HASUNITROLE, false);
                        sharedata.putString(Contant.LOGIN_DATA_USERNAME, "");
                        sharedata.putString(Contant.LOGIN_DATA_PASSWORD, "");
                        sharedata.commit();
                        //TODO:显示登录界面
                        // getSupportFragmentManager().beginTransaction().replace(indicatorViewPager.,new LoginFragment()).commit();
                        // fm.beginTransaction().remove(fm.findFragmentByTag("android:switcher:"+R.id.tabmain_viewPager+":"+indicatorViewPager.getCurrentItem())).commit();
                    }

                    fragment = getFragment();
                    indicatorViewPager.notifyDataSetChanged();
                    indicatorViewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
                    indicatorViewPager.setCurrentItem(2, false);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(TabMainActivity.this, "服务器返回数据格式错误", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
