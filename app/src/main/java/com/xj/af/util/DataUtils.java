package com.xj.af.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;

import com.xj.af.common.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by xiaojia on 2015/3/13.
 * 数据获取，报考支付宝账号，单位号
 * 1. 打开开始页面先从调用init(Context context,Handler handler)初始化数据，handler中设置返回的值
 * 2. unitId 命名规则：af621XXXX
 */
public class DataUtils {
   /**寺院支付宝账号  从服务器获取，*/
    public final static String PARTNER = "partner";
    public final static String PRIVATE_KEY = "privateKey";
    public final static String ALIPAY_RSA_PRIVATE_PKCS8="alipayRsaPrivatePkcs8";
    public final static String RSA_PUBLIC = "rsaPublic";
    public final static String SELLER = "seller";
   /**我们支付宝账号   保存在unit.properties中*/
    public final static String MY_PARTNER ="myPartner";
    public final static String MY_PRIVATE_KEY ="mYPrivateKey";
    public final static String MY_ALIPAY_RSA_PRIVATE_PKCS8 ="mYAlipayRsaPrivatePkcs8";
    public final static String MY_RSA_PUBLIC = "myRsaPublic";
    public final static String MY_SELLER = "mySeller";
    public final static String SERVER_URL = "serverUrl";

    /**从文件名获取，命名规则为   af621XXXX*/
    public final static String UNITID = "unitId";//21


    /**
     * 从unit.propertiest文件中获取数据
     * @param context
     * @param strs
     * @return
     */
    public static String []  getDataFromUnitProp(Context context,String ... strs){

        Properties assertsPro = new Properties();
        try {
            InputStream assertsInputStream = context.getAssets().open("unit.properties");
            assertsPro.load(assertsInputStream);
            assertsInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String [] ret = new String [strs.length] ;
        for(int i=0;i<strs.length;i++)
        {
            String str = strs[i];
            ret[i] = assertsPro.getProperty(str);
        }
        return ret;
    }




    public static void init(Context context,String unitInfo){
        //重新获取数据
        //先在属性文件中获取数据
        getDataFromPropInit(context);
        //在apk中获取数据,主要获取unitid，覆盖属性文件中的数据
        //getDataFromApk(context);
        //覆盖其他数据
        initUnitInfo(context,unitInfo);

    }




    /**获取我们的支付宝账号和测试unit*/
    private static void getDataFromPropInit(Context context){
        String strs[] = getDataFromUnitProp(context,new String [] {UNITID,SERVER_URL});
        setSharedPreferences(context,new String [] {UNITID,SERVER_URL},strs[0],strs[1]);
    }





    /**
     * 解密
     * @param str
     * @return
     */
    private  static String decrypt(String str){

        return str;
    }

    /**
     * 获取unitid
     * @param context
     */
    private static void getDataFromApk(Context context) {
        //从apk包中获取
        ApplicationInfo appinfo = context.getApplicationInfo();
        String sourceDir = appinfo.sourceDir;
        //注意这里：默认放在meta-inf/里， 所以需要再拼接一下
        String key = "META-INF/" + "af6";
        ZipFile zipfile = null;
        try {
            zipfile = new ZipFile(sourceDir);
            Enumeration<?> entries = zipfile.entries();
            int s = 5;
            while (entries.hasMoreElements()) {
                ZipEntry entry = ((ZipEntry) entries.nextElement());
                String entryName = entry.getName();
                if(entryName.startsWith(key+"21")){
                    setSharedPreferences(context,new String []{UNITID},entryName.substring(s));
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zipfile != null) {
                try {
                    zipfile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 根据unitID获取单位信息     *
     */
    private static void  initUnitInfo(Context c,String unitInfo){
        try {
            JSONObject js = new JSONObject(unitInfo);
            setSharedPreferences(c,new String[]{PARTNER,PRIVATE_KEY,ALIPAY_RSA_PRIVATE_PKCS8,RSA_PUBLIC,SELLER,Constant.UNIT_EDITDATA},
                    decrypt(js.getString("zfpn")),decrypt(js.getString("zfpk")),decrypt(js.getString("zfrpk8")),js.getString("zfrp"),js.getString("zfsr"),js.getString("editDate"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**从缓冲，文件名，属性文件中获取unitId*/
    public static String getUnitId(Context context){
        String unitId = "";
        unitId = getSharedPreferences(context,UNITID)[0];
        if(StrUtil.isNotBlank(unitId))
            return unitId;
        getDataFromApk(context);
        unitId = getSharedPreferences(context,UNITID)[0];
        if(StrUtil.isBlank(unitId)) {
            unitId = getDataFromUnitProp(context, UNITID)[0];
        }
        setSharedPreferences(context,new String[]{UNITID},unitId);
        return unitId;
    }

    //==========SharedPreferences 方法封装===========================================================================
    public static void setSharedPreferences(Context context,String [] keys,String ... values){
        SharedPreferences.Editor sharedata = context.getSharedPreferences(Constant.LOGIN_DATA, 0).edit();
        String ret [] = new String[keys.length];
        for(int i=0;i<keys.length;i++){
            sharedata.putString(keys[i],values[i]);
        }
        sharedata.commit();
    }
    public static void setSharedPreferences(Context context,String [] keys,int ... values){
        SharedPreferences.Editor sharedata = context.getSharedPreferences(Constant.LOGIN_DATA, 0).edit();
        for(int i=0;i<keys.length;i++){
            sharedata.putInt(keys[i],values[i]);
        }
        sharedata.commit();
    }
    public static void setSharedPreferences(Context context,String [] keys,boolean ... values){
        SharedPreferences.Editor sharedata = context.getSharedPreferences(Constant.LOGIN_DATA, 0).edit();
        for(int i=0;i<keys.length;i++){
            sharedata.putBoolean(keys[i],values[i]);
        }
        sharedata.commit();
    }
    public static void setSharedPreferences(Context context,String [] keys,long ... values){
        SharedPreferences.Editor sharedata = context.getSharedPreferences(Constant.LOGIN_DATA, 0).edit();
        for(int i=0;i<keys.length;i++){
            sharedata.putLong(keys[i], values[i]);
        }
        sharedata.commit();
    }

    public static String [] getSharedPreferences(Context context,String ... values){
        SharedPreferences sharedata = context.getSharedPreferences(Constant.LOGIN_DATA, 0);
        String ret [] = new String[values.length];
        for(int i=0;i<values.length;i++){
            ret[i] = sharedata.getString(values[i],"");
        }
        return ret;
    }

}
