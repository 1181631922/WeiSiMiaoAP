package com.xj.af.common;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.xj.af.common.GroupAdapter;
import com.xj.af.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaojia on 2015/2/6.
 */
public class BaseBackFragmentActivity extends BaseFragmentActivity{

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





}
