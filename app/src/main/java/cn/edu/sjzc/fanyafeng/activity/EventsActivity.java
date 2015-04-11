package cn.edu.sjzc.fanyafeng.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xj.af.R;
import com.xj.af.common.BaseActivity;
import com.xj.af.common.BaseBackActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.sjzc.fanyafeng.adapter.EventAdapter;
import cn.edu.sjzc.fanyafeng.bean.EventBean;
import cn.edu.sjzc.fanyafeng.listviewui.PullToRefreshListView;
import cn.edu.sjzc.fanyafeng.util.RefreshableView;

import java.text.SimpleDateFormat;

public class EventsActivity extends BaseBackActivity {

    private ImageView event_img;
    private TextView event_name, event_time;
    private ListView event_listview;
    private EventAdapter eventAdapter;
    private List<EventBean> eventBeans;
    private List<Map<String, Object>> eventsList = new ArrayList<Map<String, Object>>();
    private String eventImg, eventName, eventTime;
    private EventBean[] eventArray;

    private PullToRefreshListView mPullListView;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
    private boolean mIsStart = true;
    private int mCurIndex = 0;
    private static final int mLoadDataCount = 100;

    private RefreshableView refreshableView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean flag = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

        setContentView(R.layout.activity_events);

        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar);
        title = "活动";
        initView();
        initData();

    }

    private void initView() {
        this.event_name = (TextView) EventsActivity.this.findViewById(R.id.event_name);
        this.event_time = (TextView) EventsActivity.this.findViewById(R.id.event_time);

        this.event_img = (ImageView) EventsActivity.this.findViewById(R.id.event_img);
    }

    private void initData() {
        refreshableView = (RefreshableView) findViewById(R.id.events_refreshable);
        event_listview = (ListView) EventsActivity.this.findViewById(R.id.event_listview);
        eventBeans = new ArrayList<EventBean>();
        eventArray = new EventBean[]{
                new EventBean("http://hiphotos.baidu.com/lzc196806/pic/item/d84f738da76514d2f11f3665.jpg", "放生", "2015/4/7"),
                new EventBean("http://img.pusa123.com/www/uploads/allimg/130602/5424_130602094335_1.jpg", "夏令营", "2015/4/8"),
                new EventBean("http://img.pusa123.com/www/uploads/allimg/150403/17422_150403102211_1.jpg", "传灯", "2015/4/8"),
                new EventBean("http://img.pusa123.com/www/uploads/allimg/150403/17422_150403151837_1.jpg", "交流", "2015/4/8"),
                new EventBean("http://img.pusa123.com/www/uploads/allimg/150403/17422_150403102211_2.jpg", "普茶", "2015/4/8"),
                new EventBean("http://img.pusa123.com/www/uploads/allimg/150403/17422_150403102211_2.jpg", "行脚", "2015/4/8"),
                new EventBean("http://img.pusa123.com/www/uploads/allimg/150403/17422_150403102211_3.jpg", "朝圣", "2015/4/8"),
                new EventBean("http://img.pusa123.com/www/uploads/allimg/150403/17422_150403102211_4.jpg", "坐禅", "2015/4/8")
        };

        putData();
        Arrays.sort(eventArray);
        eventBeans = Arrays.asList(eventArray);

        eventAdapter = new EventAdapter(EventsActivity.this, eventBeans);
        event_listview.setAdapter(eventAdapter);
        event_listview.setOnItemClickListener(new eventInfoOnItemClickListener());
        refreshableView.setOnRefreshListener(new RefreshableView.PullToRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                refreshableView.finishRefreshing();
            }
        }, 0);

    }

    private void putData() {
        for (int i = 0; i < eventArray.length-4; i++) {
            EventBean eventBean = new EventBean(eventImg, eventName, "点击查看详情", eventTime);

            String mimg = eventArray[i].getEventImg();
            String mname = eventArray[i].getEventName();
            String mtime = eventArray[i].getEventTime();

            Map<String, Object> map = new HashMap<>();
            map.put("eventImg", mimg);
            map.put("eventName", mname);
            map.put("eventTime", mtime);
            eventsList.add(map);

        }
    }

    protected class eventInfoOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent it_event_info = new Intent(EventsActivity.this, EventInfoActivity.class);
            for (int i = 0; i <= position; i++) {
                if (position == i) {
                    Map map = (Map) eventsList.get(i);
                    Bundle bundle = new Bundle();
                    bundle.putString("eventimg_info", (String) map.get("eventImg"));
                    bundle.putString("eventname_info", (String) map.get("eventName"));
                    bundle.putString("eventtime_info", (String) map.get("eventTime"));
                    it_event_info.putExtras(bundle);
                }
            }
            startActivity(it_event_info);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_events, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
