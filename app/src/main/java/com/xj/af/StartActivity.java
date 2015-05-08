package com.xj.af;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xj.af.common.BaseActivity;
import com.xj.af.common.Constant;
import com.xj.af.main.TabMainActivity;
import com.xj.af.util.Contant;
import com.xj.af.util.DataUtils;
import com.xj.af.util.StrUtil;
import com.xj.af.util.http.HttpUtil;
import com.xj.af.util.http.PostUtil;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * 启动页面
 */
public class StartActivity extends BaseActivity {
    private TextView UITxt;
    private CheckUpdateHandler UIhandler;
    private Animation myAnimation_Scale;
    private String version;
    private String updateInfo;
    private String durl;
    private ProgressBar progressBar;
    private int fileLength;
    private int DownedFileLength;
    private InputStream inputStream;
    private URLConnection connection;
    private OutputStream outputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        UITxt = (TextView) findViewById(R.id.textView_start);
        progressBar = (ProgressBar) findViewById(R.id.start_progressBar1);
        //极光推送
        //JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        Set tags = new HashSet<String>();
        tags.add(getUnitId()+"");
        JPushInterface.setAliasAndTags(getApplicationContext(), null, tags);
        myAnimation_Scale = new ScaleAnimation(0.0f, 1.4f, 0.0f, 1.4f,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        try {
            version = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        UIhandler = new CheckUpdateHandler();
        CheckUpdateThread thread = new CheckUpdateThread();
        thread.start();
    }

    /**是否升级*/
    private class CheckUpdateHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            String emsg = bundle.getString("emsg");
            String imsg = bundle.getString("imsg");
            durl = bundle.getString("durl");
            if (emsg != null) {
                UITxt.setBackgroundColor(Color.argb(100, 255, 255, 255));
            } else if (imsg != null && imsg.equals("update")) {
                new AlertDialog.Builder(StartActivity.this)
                        .setMessage(updateInfo)
                        .setPositiveButton("升级",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int whichButton) {
                                        progressBar.setVisibility(View.VISIBLE);
                                        DownLoadThread dt = new DownLoadThread();
                                        dt.start();
                                        setResult(RESULT_OK);// 确定按钮事件
                                        //finish();
                                    }
                                })
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int whichButton) {
                                        // 取消按钮事件
                                        Intent i = new Intent();
                                        i.setClass(StartActivity.this,
                                                TabMainActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                }).show();
            }
            UITxt.setText(emsg);
        }
    }

    private Handler progressHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (!Thread.currentThread().isInterrupted()) {
                switch (msg.what) {
                    case 0:
                        progressBar.setMax(fileLength);
                        break;
                    case 1:
                        progressBar.setProgress(DownedFileLength);
                        int x = DownedFileLength * 100 / fileLength;
                        break;
                    case 2:
                        progressBar.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
            }

        }
    };

    private void DownFile(String durl) {
        try {
            URL url = new URL(durl);
            connection = url.openConnection();
            inputStream = connection.getInputStream();
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String downloadDir = Environment.getExternalStorageDirectory().getPath() + "/download";
        File file1 = new File(downloadDir);
        if (!file1.exists()) {
            file1.mkdir();
        }
        String filePath = downloadDir + "/af.apk";
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Message message = new Message();
        try {
            outputStream = new FileOutputStream(file);
            fileLength = connection.getContentLength();
            int bufferLen = 1024;
            byte[] buffer = new byte[bufferLen];
            message.what = 0;
            progressHandler.sendMessage(message);
            int count = 0;
            while ((count = inputStream.read(buffer)) != -1) {
                DownedFileLength += count;
                outputStream.write(buffer, 0, count);
                Message message1 = new Message();
                message1.what = 1;
                progressHandler.sendMessage(message1);
            }

            Message message2 = new Message();
            message2.what = 2;
            progressHandler.sendMessage(message2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class DownLoadThread extends Thread {

        @Override
        public void run() {
            DownFile(durl);
            installApk(HttpUtil.downloadPath + "/af.apk");
        }

        // 安装apk文件
        private void installApk(String filename) {
            File file = new File(filename);
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW); // 浏览网页的Action(动作)
            String type = "application/vnd.android.package-archive";
            intent.setDataAndType(Uri.fromFile(file), type); // 设置数据类型
            startActivity(intent);
        }

    }
    /**开始检查升级*/
    private class CheckUpdateThread extends Thread {
        @Override
        public void run() {
            String unitId = "";
            String serverURL = "";
            unitId = DataUtils.getUnitId(getApplicationContext());
            String url = getServerURL() + "/api/start/init";
            Log.d("----------------------------------url",url);
            Context context = getWindow().getContext();
            TelephonyManager telephonemanage = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            // 写日志
            try {
                Map<String, String> params = new HashMap<String, String>();
                params.put("logTx.optName", "打开移动端");
                params.put("logTx.action", getClass().getName());
                params.put("logTx.deviceId", telephonemanage.getDeviceId());
                params.put("logTx.device", Build.MODEL);
                params.put("logTx.des", telephonemanage.getLine1Number());
                params.put("logTx.osVersion", Build.VERSION.RELEASE);
                params.put("logTx.unitId", unitId);
                //自动登陆
                SharedPreferences sharedata = getSharedPreferences("data", 0);
                String username = sharedata.getString(Contant.LOGIN_DATA_USERNAME, "");
                String password = sharedata.getString(Contant.LOGIN_DATA_PASSWORD, "");
                params.put("username", username);
                params.put("password", password);
//                JPushInterface.setTags();
                JPushInterface.setAlias(getApplication(),username,new TagAliasCallback() {
                    @Override
                    public void gotResult(int i, String s, Set<String> strings) {
                        //设置标签别名请注意处理call back结果。只有call back 返回值为 0 才设置成功
                    }
                });
                //Thread.sleep(500);
                String str = PostUtil.postData(url, params);
                if (str != null && !str.equals("")) {
                    JSONObject jo = new JSONObject(str);
                    Log.d("-------------jo-----------",jo.toString());
                    //保存登录结果
                    String loginResult = jo.getString("loginResult");
                    String unitInfo = jo.getString("unit");//单位信息，判断是否需要初始化
                    if (loginResult != null && !loginResult.equals("") && !loginResult.equals("null")) {
                        JSONObject jr = new JSONObject(loginResult);
                        String loginedStr = jr.getString("result");
                        if(StrUtil.isNotBlank(unitInfo)){
                            String cacheEditDate = DataUtils.getSharedPreferences(getApplicationContext(), Constant.UNIT_EDITDATA)[0];
                            JSONObject unitJs = new JSONObject(unitInfo);
                            String dbDate = unitJs.getString("editDate");
                            if(dbDate!=null && (!dbDate.equals(cacheEditDate))){
                                //初始化
                                DataUtils.init(getApplicationContext(),unitJs.toString());
                            }
                        }

                        SharedPreferences.Editor sharedatae = getSharedPreferences(Contant.LOGIN_DATA, 0).edit();
                        if (loginedStr != null && loginedStr.equals("success")) {
                            //登录成功后检查是否为管理员
                            String hasUnitRole = jr.getString("hasUnitRole");
                            sharedatae.putBoolean(Contant.LOGIN_DATA_LOGINED, true);
                            if (StrUtil.isNotBlank(hasUnitRole) && "true".equals(hasUnitRole)) {
                                sharedatae.putBoolean(Contant.LOGIN_DATA_HASUNITROLE, true);
                            } else {
                                sharedatae.putBoolean(Contant.LOGIN_DATA_HASUNITROLE, false);
                            }
                        } else {
                            sharedatae.putBoolean(Contant.LOGIN_DATA_LOGINED, false);
                        }
                        sharedatae.commit();
                    }

                    // 检测版本升级
                    String appInfo = jo.getString("appInfo");
                    Log.d("-------------------appinfo----------",appInfo);
                    if (StrUtil.isNotBlank(appInfo)) {
                        JSONObject jso = new JSONObject(appInfo);

                        String durl = jso.getString("downloadUrl");
                        Log.d("--------------------------drul---------",durl);
                        String ver = jso.getString("version");
                        updateInfo = jso.getString("des");
                        int status = jso.getInt("status");

                        if (!ver.equals(version) && status == 0) {
                            // 需要升级
                            Message msg = new Message();
                            Bundle bundle = new Bundle();
                            bundle.putString("imsg", "update");
                            bundle.putString("durl", durl);
                            msg.setData(bundle);
                            StartActivity.this.UIhandler.sendMessage(msg);
                        } else {
                            Intent i = new Intent();
                            i.setClass(StartActivity.this, TabMainActivity.class);
                            startActivity(i);
                            finish();
                            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
                        }
                    } else {
                        Intent i = new Intent();
                        i.setClass(StartActivity.this, TabMainActivity.class);
                        startActivity(i);
                        finish();
                        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                Message msg = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("emsg", "网络不够给力，等会再试试吧(T_T)");
                msg.setData(bundle);
                StartActivity.this.UIhandler.sendMessage(msg);
            }
        }


    }
    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }
}
