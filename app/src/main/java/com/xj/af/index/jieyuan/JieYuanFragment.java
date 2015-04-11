package com.xj.af.index.jieyuan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.xj.af.R;

import com.xj.af.common.BaseXjFragment;
import com.xj.af.persion.PersionActivity;
import com.xj.af.util.StrUtil;
import com.xj.af.util.http.GetUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
 */
public class JieYuanFragment extends BaseXjFragment implements AbsListView.OnItemClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private UIHandler uiHandler;
    ArrayList<HashMap<String, Object>> listItem;
    private SimpleAdapter listItemAdapter;
    private String url;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private GridView mListView;


    // TODO: Rename and change types of parameters
    public static JieYuanFragment newInstance(String param1, String param2) {
        JieYuanFragment fragment = new JieYuanFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public JieYuanFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            url = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // TODO: Change Adapter to display your content
        listItem = new ArrayList();
        listItemAdapter = new SimpleAdapter(getActivity(), listItem,// 数据源
                R.layout.jieyuan_item,
                new String[] {  "ItemTitle" },
                new int[] {  R.id.jieyuan_ItemText });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_jieyuan, container, false);

        mListView = (GridView) view.findViewById(R.id.jieyuan_gridView);
        mListView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("xj", "this is onActivityCreated()");
        loadData( );
    }

    private void loadData(){
        uiHandler = new UIHandler();
        UIThread thread = new UIThread();
        thread.start();
    }

    private class UIThread extends Thread {
        @Override
        public void run() {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            msg.setData(bundle);
            String newsStr = "";
            try {
                newsStr = GetUtil.get(url);
                bundle.putString("msg", newsStr);
            } catch (IOException e) {
                e.printStackTrace();
                 bundle.putString("emsg", "网络不够给力，等会再试试吧(T_T)");
            }
            JieYuanFragment.this.uiHandler.sendMessage(msg);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
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
        mListener = null;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //*
       if(!getLogined()){
           Toast.makeText(getActivity(), "请登陆", Toast.LENGTH_LONG).show();
           Intent it = new Intent(getActivity(),PersionActivity.class);
           startActivity(it);
           return;
       }//*/
        Intent it = null;
        if(url.endsWith("1"))
                it = new Intent(getActivity(),JieYuanDetailActivity.class);
         if(url.endsWith("2"))
                it = new Intent(getActivity(),ZhuyinDetailActivity.class);
        GridView listv = (GridView)parent;
        SimpleAdapter la = (SimpleAdapter)listv.getAdapter();
        Map map = (Map) la.getItem(position);
        it.putExtra("id", (Long) map.get("id"));
        it.putExtra("des",map.get("des")==null?"":map.get("des").toString());
        it.putExtra("picPath",map.get("picPath")==null?"":map.get("picPath").toString());
        it.putExtra("title", map.get("ItemTitle")==null?"":map.get("ItemTitle").toString());
        startActivity(it);
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();
        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
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


    private class UIHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            String emsg = bundle.getString("emsg");
            String message = bundle.getString("msg");
            if(StrUtil.isNotBlank(emsg)){
                Toast.makeText(getActivity(),emsg,Toast.LENGTH_SHORT).show();
            }
            if(message==null)return;
            try {
                JSONArray sortJsonArray = new JSONArray(message);
                for (int i = 0; i < sortJsonArray.length(); i++) {
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    JSONObject jsonObj = ((JSONObject) sortJsonArray.opt(i));
                    JSONObject jsonJyjs = ((JSONObject) new JSONObject(jsonObj.getString("jieYuanJingShu")));
                    map.put("ItemTitle",jsonJyjs.getString("name"));
                    map.put("id",jsonJyjs.getLong("id"));
                    map.put("des",jsonJyjs.getString("des"));
                    map.put("picPath",jsonJyjs.getString("picPath"));
                    listItem.add(map);
                }
                mListView.setAdapter(listItemAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

}
