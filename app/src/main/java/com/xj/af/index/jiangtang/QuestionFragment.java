package com.xj.af.index.jiangtang;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.xj.af.R;
import com.xj.af.common.BaseFragment;
import com.xj.af.util.Contant;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuestionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuestionFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private WebView mWebView;
    private OnFragmentInteractionListener mListener;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QuestionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QuestionFragment newInstance(String param1, String param2) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public QuestionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_question, container, false);
        setContentView(R.layout.fragment_question);
        mWebView = (WebView) findViewById(R.id.questionAdd_webView);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String url = getServerURL()+ "/m/wenda/";
        Intent it = getActivity().getIntent();
        String username = it.getStringExtra("username");
        String action = it.getStringExtra("action");
        String query = "";

        if(username!=null && !"".equals(username)){
            query = "?search_EQ_loginName="+username;
            if("question".equals(action)){
                query += "&search_EQ_unitId="+getUnitId();
                url += getUnitId()+query;
            }else if("huifu".equals(action)){
                url += "answer"+query;
            }
        }else{
            url += getUnitId();
        }
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        mWebView.addJavascriptInterface(new Object(){
            @JavascriptInterface
            public void login(String username,String password){
               onButtonPressed(username,password);
            }
        }, "android");

        mWebView.loadUrl(url);
    }
    public void onButtonPressed(String  str,String pas) {
        if (mListener != null) {
            mListener.onFragmentInteraction(str,pas);
        }
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String str,String pas);
    }
}
