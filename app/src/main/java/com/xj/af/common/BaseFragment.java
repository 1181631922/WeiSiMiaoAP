package com.xj.af.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xj.af.util.Contant;
import com.xj.af.util.StrUtil;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class BaseFragment extends Fragment {
	private LayoutInflater inflater;
	private View contentView;
	private Context context;
	private ViewGroup container;
    private String serverURL;
    private String unitId;
    private String alipayEnable;


    public String getUsername(){
        SharedPreferences sharedata = getActivity().getSharedPreferences("data", 0);
        if(getLogined())
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

    public boolean getLogined() {
        SharedPreferences sharedata = getActivity().getSharedPreferences("data", 0);
        String username = sharedata.getString(Contant.LOGIN_DATA_USERNAME, "");
        String password = sharedata.getString(Contant.LOGIN_DATA_PASSWORD, "");
        boolean logined = sharedata.getBoolean(Contant.LOGIN_DATA_LOGINED,
                false);
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password))
            return false;
        return logined;
    }

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity().getApplicationContext();
        try {
            Properties assertsPro = new Properties();
            InputStream assertsInputStream = getActivity().getAssets().open("unit.properties");
            assertsPro.load(assertsInputStream);
            unitId = assertsPro.getProperty("unitId");
            serverURL = assertsPro.getProperty("serverUrl");
            alipayEnable = assertsPro.getProperty("ALIPAY_ENABLE");
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	@Override
	public  View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
		this.container = container;
		onCreateView(savedInstanceState);
		if (contentView == null)
			return super.onCreateView(inflater, container, savedInstanceState);
		return contentView;
	}

	protected void onCreateView(Bundle savedInstanceState) {

	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		contentView = null;
		container = null;
		inflater = null;
	}

	public Context getApplicationContext() {
		return context;
	}

	public void setContentView(int layoutResID) {
		setContentView((ViewGroup) inflater.inflate(layoutResID, container, false));
	}

	public void setContentView(View view) {
		contentView = view;
	}

	public View getContentView() {
		return contentView;
	}

	public View findViewById(int id) {
		if (contentView != null)
			return contentView.findViewById(id);
		return null;
	}

	// http://stackoverflow.com/questions/15207305/getting-the-error-java-lang-illegalstateexception-activity-has-been-destroyed
	@Override
	public void onDetach() {
		super.onDetach();
		try {
			Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);

		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

    public static String getMilliToDate(String time){
        Date date = new Date(Long.valueOf(time));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date);
    }


}
