package com.xj.af.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xj.af.R;
import com.xj.af.util.Contant;
import com.xj.af.util.StrUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by xiaojia on 2015/1/30.
 */
public class BaseBackActivity extends BaseActivity {
    public TextView titleBar_TextView ;
    public ImageButton titleBar_ImageButton ;
    public ImageButton titleBar_share;
    public boolean isShowShare = false;
    public String title ;
    private PopupWindow popupWindow;
    private ListView lv_group;
    private View view;
    private View top_title;
    private TextView tvtitle;
    private List<String> groups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onResume() {
        super.onResume();
        titleBar_TextView = (TextView) findViewById(R.id.titleBar_TextView);
        titleBar_ImageButton = (ImageButton) findViewById(R.id.titleBar_back);
        titleBar_share = (ImageButton) findViewById(R.id.titleBar_share);
        if (titleBar_ImageButton != null){
            titleBar_ImageButton.setVisibility(View.VISIBLE);
            titleBar_ImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    onBackPressed();
                }
            });
        }

        if(titleBar_share != null){
            if(isShowShare){
                titleBar_share.setVisibility(View.VISIBLE);
                titleBar_share.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // showWindow(arg0);  下拉菜单分享
                        showShare();
                        //shareTo();
                    }
                });
            }
            else{
                titleBar_share.setVisibility(View.GONE);
            }
        }



        if(title!=null && !title.equals("")){
            setTitle(title);
        }
        //FrontiaStatistics stat = Frontia.getStatistics();
        //stat.pageviewStart(getApplicationContext(), title);
    }
    @Override
    public void setTitle(CharSequence title) {
        if (titleBar_TextView != null)
            titleBar_TextView.setText(title);
    }

    /*消息发送到*/
    public void shareTo(){
        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");  //分享的数据类型
        intent.putExtra(Intent.EXTRA_SUBJECT, "subject");  //主题
        intent.putExtra(Intent.EXTRA_TEXT,  "content");  //内容
        startActivity(Intent.createChooser(intent, "title"));
    }


    public void showShare() {

    }


    /*自定义下拉列表**/
    private void showWindow(View parent) {
        if (popupWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.title_menu_list, null);
            lv_group = (ListView) view.findViewById(R.id.lvGroup);
            // 加载数据
            groups = new ArrayList<String>();
            groups.add("新浪微博");
            groups.add("腾讯微博");
            groups.add("微信");
            groups.add("QQ空间");
            List icoList = new ArrayList<Integer>();
            icoList.add(R.drawable.sns_sina_icon);
            icoList.add(R.drawable.sns_qq_icon);
            icoList.add(R.drawable.sns_weixin_icon);
            icoList.add(R.drawable.sns_qzone_icon);
            GroupAdapter groupAdapter = new GroupAdapter(this, groups,icoList);
            lv_group.setAdapter(groupAdapter);
            // 创建一个PopuWidow对象
            popupWindow = new PopupWindow(view, 450, 560);
        }
        // 使其聚集
        popupWindow.setFocusable(true);
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        // 显示的位置为:屏幕的宽度的一半-PopupWindow的高度的一半
        int xPos = windowManager.getDefaultDisplay().getWidth() / 2
                - popupWindow.getWidth() / 2;
        Log.i("xj", "xPos:" + xPos);
        popupWindow.showAsDropDown(parent, xPos, 0);
        lv_group.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
            }
        });
    }
}
