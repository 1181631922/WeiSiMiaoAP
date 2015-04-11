package com.xj.af.util;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.Random;

public class PayUtils {
    public  static String PARTNER ;
    public  static String PRIVATE_KEY ;
    public  static String ALIPAY_RSA_PRIVATE_PKCS8;
    public  static String RSA_PUBLIC;
    public  static String SELLER;
    public final static int  SDK_PAY_FLAG = 1;
    public final static int SDK_CHECK_FLAG = 2;
    public final static String paymethod = "expressGateway";//启用银行卡支付，如果checkAccountIfExist返回false可使用次参数

    /**
     * 给我们自己付款的支付宝
     * @param a
     * @param subject
     * @param body
     * @param price
     * @param userName
     * @return
     */
    public static String getMyOrderInfo(Activity a,String subject,String body,String price,String userName){
        try {
            String strs [] = DataUtils.getDataFromUnitProp(a.getApplication(),DataUtils.MY_PARTNER,DataUtils.MY_PRIVATE_KEY,DataUtils.MY_ALIPAY_RSA_PRIVATE_PKCS8,DataUtils.MY_RSA_PUBLIC, DataUtils.MY_SELLER);
            PARTNER = strs[0];
            PRIVATE_KEY = strs[1];
            ALIPAY_RSA_PRIVATE_PKCS8 = strs[2];
            RSA_PUBLIC = strs[3];
            SELLER = strs[4];
        } catch (Exception e) {
            e.printStackTrace();
        }
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";
        orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";
        orderInfo += "&subject=" + "\"" + subject + "\"";//商品名称
        orderInfo += "&body=" + "\"" + body + "\"";// 商品详情
        orderInfo += "&total_fee=" + "\"" + price + "\"";//总金额
        //orderInfo += "&notify_url=\""+URLEncoder.encode("http://www.weisimiao.com/api/alipay/notify/getmoney/"+userName)+"\"";//支付宝主动通知接口
        orderInfo += "&notify_url=" + "\""+"http://www.weisimiao.com/api/shopsy/alipayNotify/getmoney/"+userName+"/\"";//支付宝主动通知接口
        orderInfo += "&service=\"mobile.securitypay.pay\"";
        orderInfo += "&payment_type=\"1\"";//支付类型，1为商品购买
        orderInfo += "&_input_charset=\"utf-8\"";//商户网站编码格式
        orderInfo += "&it_b_pay=\"30m\"";

        String sign = PayUtils.sign(orderInfo);
        try {
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        orderInfo += "&sign=\""+sign+"\"";
        orderInfo += "&sign_type=\"RSA\"";

        return orderInfo;
    }


    /**
     * 给寺院付款的支付宝账号信息
     * @param a
     * @param subject
     * @param body
     * @param price
     * @param userName
     * @return
     */
    public static String getOrderInfo(Activity a,String subject,String body,String price,String userName){
       // try {
           // Properties assertsPro = new Properties();
           // InputStream assertsInputStream = a.getAssets().open("unit.properties");
           // assertsPro.load(assertsInputStream);

            PARTNER = DataUtils.getSharedPreferences(a.getApplicationContext(),DataUtils.PARTNER)[0];//getDate(a.getApplicationContext(),DataUtils.PARTNER);// assertsPro.getProperty("ALIPAY_PARTNER");
            PRIVATE_KEY =DataUtils.getSharedPreferences(a.getApplicationContext(),DataUtils.PRIVATE_KEY)[0];//getDate(a.getApplicationContext(),DataUtils.PRIVATE_KEY);// assertsPro.getProperty("ALIPAY_PRIVATE_KEY");
            ALIPAY_RSA_PRIVATE_PKCS8 = DataUtils.getSharedPreferences(a.getApplicationContext(),DataUtils.ALIPAY_RSA_PRIVATE_PKCS8)[0];//getDate(a.getApplicationContext(),DataUtils.ALIPAY_RSA_PRIVATE_PKCS8);//assertsPro.getProperty("ALIPAY_RSA_PRIVATE_PKCS8");
            RSA_PUBLIC = DataUtils.getSharedPreferences(a.getApplicationContext(),DataUtils.RSA_PUBLIC)[0];//getDate(a.getApplicationContext(),DataUtils.RSA_PUBLIC);//assertsPro.getProperty("ALIPAY_RSA_PUBLIC");
            SELLER = DataUtils.getSharedPreferences(a.getApplicationContext(),DataUtils.SELLER)[0];//getDate(a.getApplicationContext(),DataUtils.SELLER);//assertsPro.getProperty("ALIPAY_SELLER");

            //assertsInputStream.close();
        //} catch (IOException e) {
        //    e.printStackTrace();
       // }
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";
        orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";
        orderInfo += "&subject=" + "\"" + subject + "\"";//商品名称
        orderInfo += "&body=" + "\"" + body + "\"";// 商品详情
        orderInfo += "&total_fee=" + "\"" + price + "\"";//总金额
        //orderInfo += "&notify_url=\""+URLEncoder.encode("http://www.weisimiao.com/api/alipay/notify/getmoney/"+userName)+"\"";//支付宝主动通知接口
        orderInfo += "&notify_url=" + "\""+"http://www.weisimiao.com/api/shopsy/alipayNotify/getmoney/"+userName+"/\"";//支付宝主动通知接口
        orderInfo += "&service=\"mobile.securitypay.pay\"";
        orderInfo += "&payment_type=\"1\"";//支付类型，1为商品购买
        orderInfo += "&_input_charset=\"utf-8\"";//商户网站编码格式
        orderInfo += "&it_b_pay=\"30m\"";

        String sign = PayUtils.sign(orderInfo);
        try {
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        orderInfo += "&sign=\""+sign+"\"";
        orderInfo += "&sign_type=\"RSA\"";

        return orderInfo;
    }
    /**
     * get the sdk version. 获取SDK版本号
     *
     */
    public static void getSDKVersion(Activity c) {
        PayTask payTask = new PayTask(c);
        String version = payTask.getVersion();
        Toast.makeText(c, version, Toast.LENGTH_SHORT).show();
    }


    public static String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
                Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

    public static String sign(String content) {
        return SignUtils.sign(content, ALIPAY_RSA_PRIVATE_PKCS8);
    }

    public static void pay(final Activity a,final Handler h,final String orderInfo){
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(a);
                // 调用支付接口
                String result = alipay.pay(orderInfo);
                Message msg = new Message();
                msg.what = PayUtils.SDK_PAY_FLAG;
                msg.obj = result;
                h.sendMessage(msg);
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    public static void checkAndPay(final Activity a,final Handler h){
        Runnable checkRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask payTask = new PayTask(a);
                boolean isExist = payTask.checkAccountIfExist();
                Message msg = new Message();
                msg.what = PayUtils.SDK_CHECK_FLAG;
                msg.obj = isExist;
                h.sendMessage(msg);
            }
        };
        Thread checkThread = new Thread(checkRunnable);
        checkThread.start();
    }
}
