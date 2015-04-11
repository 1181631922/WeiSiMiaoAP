package com.xj.af.news;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.xj.af.R;
import com.xj.af.common.BaseFragment;

/**
 * 显示当家主持和新闻
 */
public class NewsFragment extends BaseFragment {

    private static final String Para_URL = "url";
    private static final String ARG_PARAM2 = "title";
    private static final String ID = "id";
    private WebView webView;
    private String url;
    private Handler myHandler;
    private String mParam1;
    private String mParam2;
    private long id;

    private OnFragmentInteractionListener mListener;

    public static NewsFragment newInstance(String param1, String param2,long id) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putString(Para_URL, param1);
        args.putString(ARG_PARAM2, param2);
        args.putLong(ID,id);
        fragment.setArguments(args);
        return fragment;
    }

    public NewsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(Para_URL);
            mParam2 = getArguments().getString(ARG_PARAM2);
            id = getArguments().getLong(ID);
        }
    }

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        super.onCreateView(savedInstanceState);
        setContentView(R.layout.fragment_news);
    }


    public void onButtonPressed(String str) {
        if (mListener != null) {
            mListener.onFragmentInteraction(str);
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        webView = (WebView) this.findViewById(R.id.webView_news);
        //isShowShare = true;
        Intent intent = getActivity().getIntent();
        long id = intent.getLongExtra("id",0);
        url =  mParam1 ;//首页显示当家主持
        url = id==0?url:(getServerURL()+ "/m/news/newsDetail/"+id+"?userName="+getUsername());

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.addJavascriptInterface(new Object(){
            @JavascriptInterface
            public void click(final int i,final int shopId,final String picPath,final long newsId) {
                Intent it = new Intent();
                it.putExtra("id", shopId+"");
                it.putExtra("picPath", picPath);
                it.putExtra("newsId", newsId+"");
                Log.d("xj", "NewsActivity:id:" + shopId);
                startActivity(it);
            }
        }, "android");
        webView.loadUrl(url);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(String str);
    }

}
