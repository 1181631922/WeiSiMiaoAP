package com.xj.af.news;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.xj.af.R;

import com.xj.af.common.BaseFragment;
import com.xj.af.common.BaseXjFragment;
import com.xj.af.common.Constant;
import com.xj.af.common.list.PullDownView;
import com.xj.af.common.list.ScrollOverListView;
import com.xj.af.news.dummy.DummyContent;
import com.xj.af.util.StrUtil;
import com.xj.af.util.http.GetUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 *
 * 显示新闻列表
 */
public class NewsSortFragment extends BaseXjFragment  implements PullDownView.OnPullDownListener, AdapterView.OnItemClickListener {
    private static final int WHAT_DID_LOAD_DATA = 0;
    private static final int WHAT_DID_REFRESH = 1;
    private static final int WHAT_DID_MORE = 2;
    private PullDownView mPullDownView;


    private static final String ARG_URL = "param1";
    private static final String ARG_TITLE = "param2";
    private static final String ARG_ID = "id";
    private static final String ARG_ENNAME = "enName";
    boolean hasMore = true;//是否还有更多
    int pageSize = 20;
    int page = 1;
    private String url;
    private String title;
    private long id;
    private String enName;
    ListView listView;
    private UIHandler UIhandler;
    ArrayList<HashMap<String, Object>> listItem;
    private SimpleAdapter listItemAdapter;

    private OnFragmentInteractionListener mListener;


    public static NewsSortFragment newInstance(String url, String title,long id,String enName) {
        NewsSortFragment fragment = new NewsSortFragment();
        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        args.putString(ARG_TITLE, title);
        args.putLong(ARG_ID, id);
        args.putString(ARG_ENNAME,enName);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NewsSortFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("xj","this is onCreate()");
        if (getArguments() != null) {
            url = getArguments().getString(ARG_URL);
            title = getArguments().getString(ARG_TITLE);
            id = getArguments().getLong(ARG_ID);
            enName = getArguments().getString(ARG_ENNAME);
        }

        listItem = new ArrayList();
        listItemAdapter = new SimpleAdapter(getActivity(), listItem,// 数据源
                R.layout.news_sort_list,
                new String[] {  "ItemTitle" },
                new int[] {  R.id.newsSortList_ItemTitle });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("xj","this is onCreateView()");
        View view  =  inflater.inflate(R.layout.pulldown,container,false);
        mPullDownView = (PullDownView) view.findViewById(R.id.pull_down_view);
        mPullDownView.setOnPullDownListener(this);
        listView =  mPullDownView.getListView();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("xj","this is onActivityCreated()");
        listView.setAdapter(listItemAdapter);
        mPullDownView.enableAutoFetchMore(true, 0);
        loadData( );
    }

    public void loadData( ){
        UIhandler = new UIHandler();
        UIThread thread = new UIThread( WHAT_DID_LOAD_DATA);
        thread.start();
    }

    @Override
    public void onRefresh() {
        UIThread thread = new UIThread( WHAT_DID_REFRESH);
        thread.start();
    }

    @Override
    public void onMore() {
        if(hasMore) {
            UIThread thread = new UIThread(WHAT_DID_MORE);
            thread.start();
        }else{
            mPullDownView.notifyDidMore();
        }
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getActivity(), "啊，你点中我了 " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d("xj","this is onAttach()");
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("xj","this is onDetach()");
        mListener = null;
    }


    private class UIThread extends Thread {
        int opt;

        private UIThread(int opt) {
            this.opt = opt;
        }

        @Override
        public void run() {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            msg.setData(bundle);
            String newsStr = "";
            String _url = getUrl();
            try {
                newsStr = GetUtil.get(_url);
                bundle.putString("msg", newsStr);
            } catch (IOException e) {
                e.printStackTrace();
                bundle.putString("emsg", "网络不够给力，等会再试试吧(T_T)");
            }
            msg.what = opt;
            NewsSortFragment.this.UIhandler.sendMessage(msg);
        }
        /**给请求地址增加分页参数*/
        private String getUrl() {
            String _url = url;
            if(opt == WHAT_DID_MORE){
                page = page + 1 ;
            }else if(opt == WHAT_DID_REFRESH){
                page = 1;
            }else if (opt == WHAT_DID_LOAD_DATA){
                page = 1;
            }
            if(url.contains("?")){
                _url += "&";
            }else{
                _url += "?";
            }
            _url += "page="+page+"&pageSize="+pageSize;
            return _url;
        }
    }

    private class UIHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            String emsg = bundle.getString("emsg");
            String message = bundle.getString("msg");
            boolean lastPage = false;//是否到了最后一页
            if(message==null)return;
            if(msg.what == WHAT_DID_REFRESH){
                listItem.clear();
            }
            try {
                if(StrUtil.isNotBlank(enName) && enName.equals(Constant.NEWSSORT_ENNAME_jingDianJieDu)){
                    //分类列表  经典解读
                    lastPage = true;
                    JSONArray sortJsonArray = new JSONArray(message);
                    for (int i = 0; i < sortJsonArray.length(); i++) {
                        HashMap<String, Object> map = new HashMap<String, Object>();
                        JSONObject jsonObj = ((JSONObject) sortJsonArray.opt(i));
                        long id = jsonObj.getLong("id");
                        String name = jsonObj.getString("name");
                        map.put("ItemTitle", name);
                        map.put("id", id);
                        map.put("type", "newsSort");
                        map.put("enName", jsonObj.getString("enName"));
                        listItem.add(map);
                    }
                }else {//新闻列表
                    JSONObject jo = new JSONObject(message);
                    JSONArray jsonObjs = jo.getJSONArray("content");
                    lastPage = jo.getBoolean("lastPage");
                    for (int i = 0; i < jsonObjs.length(); i++) {
                        HashMap<String, Object> map = new HashMap<String, Object>();
                        JSONObject jsonObj = ((JSONObject) jsonObjs.opt(i));
                        String name = jsonObj.getString("title");
                        String kaishi = jsonObj.getString("createTime");
                        setTitle(map, name, kaishi);
                        map.put("id", jsonObj.getLong("id"));
                        map.put("type", "news");
                        listItem.add(map);
                    }
                }
                if(lastPage) {
                    hasMore = false;
                    mPullDownView.enableAutoFetchMore(false,0);

                }

                switch (msg.what) {
                    case WHAT_DID_LOAD_DATA:{
                        mPullDownView.notifyDidLoad();
                        break;
                    }
                    case WHAT_DID_REFRESH : {
                        mPullDownView.enableAutoFetchMore(true,0);
                        mPullDownView.notifyDidRefresh();
                        hasMore = true;
                        break;
                    }
                    case WHAT_DID_MORE:{
                        mPullDownView.notifyDidMore();
                        if(lastPage){
                            Toast.makeText(getActivity(),"已经到了最后一页",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                }

                listItemAdapter.notifyDataSetChanged();
            }catch (JSONException e) {
                e.printStackTrace();
            }

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                    ScrollOverListView listv = (ScrollOverListView) arg0;
                    ListAdapter la = listv.getAdapter();
                    SimpleAdapter sa = getSimpleAdapter(la);
                    Intent i = new Intent();
                    Map map = (Map) sa.getItem(arg2);
                    i.putExtra("id", (Long) map.get("id"));

                    Object obj = map.get("ItemTitle");
                    if(obj!=null){
                        String title = (String)obj;
                        i.putExtra("title", title);
                    }
                    Object typeObj = map.get("type");
                    if(typeObj != null && typeObj.toString().equals("newsSort")){
                    //newsSort
                       // i.putExtra("enName",enName);
                        i.setClass(getActivity(),NewsSortActivity.class);
                    }else {
                    //news
                        i.setClass(getActivity(), NewsActivitActivity.class);
                    }
                    startActivity(i);
                }

                private SimpleAdapter getSimpleAdapter(ListAdapter la){
                    if(la instanceof HeaderViewListAdapter){
                        HeaderViewListAdapter hvla = (HeaderViewListAdapter)la;
                        ListAdapter _la = hvla.getWrappedAdapter();
                        return getSimpleAdapter(_la);
                    }else{
                        return (SimpleAdapter)la;
                    }
                }
            });
        }

        private void setTitle(HashMap<String, Object> map, String name, String kaishi) {
            DateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
            if("jinRiShuoFa".equals(enName) || (url!=null && url.indexOf("jinRiShuoFa") != -1)){
                if(kaishi!=null && !"".equals(kaishi))
                {
                    String reTime = sdf.format(Long.parseLong(kaishi));
                    map.put("ItemTitle", reTime+"开示");
                }
            }
            else {
                map.put("ItemTitle", name);
            }
        }
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

}
