package com.xj.af.gdl;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.xj.af.R;
import com.xj.af.common.BaseBackActivity;
import com.xj.af.util.StrUtil;
import com.xj.af.util.ThreadUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyGdlActivity extends BaseBackActivity {
    private Handler handler;
    private ListView lv;
    private List listItem;
    private SimpleAdapter listItemAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_my_gdl);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.title_bar);
        title = "我的布施";
        lv = (ListView)findViewById(R.id.listView);
        String url = getServerURL()+"/api/shopsy/accountMoney/myMoney/"+getUsername();
        listItem = new ArrayList<HashMap<String, Object>>();
        listItemAdapter = new SimpleAdapter(this, listItem,// 数据源
                R.layout.gdl_list,
                new String[] {"project","money","date" },
                new int[] {  R.id.gdl_textView_project,R.id.gdl_textView_money ,R.id.gdl_textView_name});
        handler = new MyHandler();
        ThreadUtil tu = new ThreadUtil(handler, url);
        tu.start();
    }


    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Bundle bd = msg.getData();
            String imsg = bd.getString("imsg");
            String emsg = bd.getString("emsg");
            if(StrUtil.isNotBlank(imsg)){
                try {
                    JSONArray newsJsonArray = new JSONArray(imsg);
                    for (int i = 0; i < newsJsonArray.length(); i++) {
                        HashMap<String, Object> map2 = new HashMap<String, Object>();
                        JSONObject jsonObj = ((JSONObject) newsJsonArray.opt(i));
                        String shopingsySimpleStr = jsonObj.getString("shopingsySimple");
                        if(StrUtil.isNotBlank(shopingsySimpleStr)){
                            JSONObject jo = new JSONObject(shopingsySimpleStr);
                            map2.put("project", jo.getString("name"));

                        }
                        //map2.put("date", jsonObj.getString("name"));
                        map2.put("money", jsonObj.getString("money"));
                        listItem.add(map2);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                lv.setAdapter(listItemAdapter);
            }
        }

    }
}
