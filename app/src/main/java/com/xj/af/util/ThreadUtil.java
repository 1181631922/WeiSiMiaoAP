package com.xj.af.util;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.xj.af.util.http.GetUtil;
import com.xj.af.util.http.PostUtil;

import org.apache.http.ParseException;

import java.io.IOException;
import java.util.Map;

/**
 * new一个对象，输入handler和url，map可选，如果有map为post，没有则为get，
 * 返回的正确的字符串为imsg，错误的字符串为emsg。
 * what = 0 正常   -1 解析异常  -2 IO异常
 *
 */
public class ThreadUtil extends Thread {
    private Handler handler;
    private String url;
    private Map map;

    public ThreadUtil(Handler handler, String url) {
        this.handler = handler;
        this.url = url;
    }

    public ThreadUtil(Handler handler, String url,Map map) {
        this.handler = handler;
        this.url = url;
        this.map = map;
    }

    @Override
    public void run() {
        if (StrUtil.isNotBlank(url)) {
            Message msg = new Message();
            Bundle bd = new Bundle();
            String imsg = "";
            String emsg = "";
            try {
                if(map==null) {
                    imsg = GetUtil.get(url);
                }else{
                    imsg = PostUtil.postData(url,map);
                }
                msg.what = 0;
            } catch (ParseException e) {
                e.printStackTrace();
                msg.what = -1;
                emsg =  e.getMessage();
            } catch (IOException e) {
                msg.what = -2;
                emsg =  e.getMessage();
                e.printStackTrace();
            }
            Log.d("xj", "url=" + url + " " + "imsg=" + imsg + " what=" + msg.what);
            bd.putString("emsg",emsg);
            bd.putString("imsg", imsg);
            msg.setData(bd);
            handler.sendMessage(msg);
        }
    }

}
