package com.xj.af.persion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xj.af.R;
import com.xj.af.common.BaseFragment;
import com.xj.af.gdl.MyGdlActivity;
import com.xj.af.index.TabActivity;
import com.xj.af.manager.ManagerActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PersionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PersionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersionFragment extends BaseFragment implements View.OnClickListener, View.OnTouchListener {

    //private MyHandler myHandler;
    private TextView tv;
    private TextView huifuTv;
    private TextView suixiTv;
    private TextView gdlTv;
    private RelativeLayout rtv;
    private RelativeLayout rhuifuTv;
    private RelativeLayout rsuixiTv;
    private RelativeLayout rgdlTv;
    private RelativeLayout newsRL;
    String username = "";
    String password = "";
    private String loginType = "clickLogin"	;
    Button buttonOut ;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private boolean hasUnitRole;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PersionFragment.
     */
    public static PersionFragment newInstance(boolean param1, String param2) {
        PersionFragment fragment = new PersionFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public PersionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            hasUnitRole = getArguments().getBoolean(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            username = mParam2;
        }

    }

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        super.onCreateView(savedInstanceState);
        setContentView(R.layout.fragment_persion);
        tv =               (TextView)findViewById(R.id.persion_wenti_textView);
        huifuTv =          (TextView)findViewById(R.id.persion_huifu_textView);
        suixiTv =          (TextView)findViewById(R.id.persion_suixi_textView);
        gdlTv =            (TextView)findViewById(R.id.persion_gdl_textView);
        rtv =              (RelativeLayout)findViewById(R.id.persion_wenti_relative);
        rhuifuTv =         (RelativeLayout)findViewById(R.id.persion_huifu_relative);
        rsuixiTv =         (RelativeLayout)findViewById(R.id.persion_suixi_relative);
        rgdlTv =           (RelativeLayout)findViewById(R.id.persion_gdl_relative);
        newsRL =           (RelativeLayout)findViewById(R.id.persion_news_relative);
        buttonOut =       (Button)findViewById(R.id.persion_loginout_button);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(hasUnitRole){
            newsRL.setVisibility(View.VISIBLE);
        }else{
            newsRL.setVisibility(View.GONE);
        }

        rtv.setOnClickListener(this);
        rtv.setOnTouchListener(this);
        rhuifuTv.setOnClickListener(this);
        rhuifuTv.setOnTouchListener(this);
        rsuixiTv.setOnClickListener(this);
        rsuixiTv.setOnTouchListener(this);
        newsRL.setOnClickListener(this);
        newsRL.setOnTouchListener(this);
        buttonOut.setOnClickListener(this);

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

    public void onButtonPressed(String uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }

    }
    public static final String MY_QUESTION = "myQuestion";
    public static final String MY_ANSWER = "myAnswer";
    public static final String MY_SUIXI = "mySuiXi";
    public static final String MY_GDL = "myGdl";
    public static final String MY_NEWS = "myNews";
    public static final String MY_LOGOUT = "myLogout";
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.persion_wenti_relative:
                //onButtonPressed(MY_QUESTION);
                Intent i = new Intent(getActivity(),
                        TabActivity.class);
                i.putExtra(TabActivity.key,TabActivity.JiangTang);
                i.putExtra("username", username);
                i.putExtra("action","question");
                i.putExtra("tab",MY_QUESTION);
                startActivity(i);
                break;
            case R.id.persion_huifu_relative:
                Intent ihuifu = new Intent(getActivity(),
                        TabActivity.class);
                ihuifu.putExtra(TabActivity.key,TabActivity.JiangTang);
                ihuifu.putExtra("username", username);
                ihuifu.putExtra("action","huifu");
                ihuifu.putExtra("tab",MY_ANSWER);
                startActivity(ihuifu);
                break;
            case R.id.persion_suixi_relative:
                Intent isuixi = new Intent(getActivity(),TabActivity.class);
                isuixi.putExtra(TabActivity.key,TabActivity.JiangTang);
                isuixi.putExtra("url", getServerURL()+"/api/newsreply/myfahui/"+username+"/"+getUnitId());
                isuixi.putExtra("title", "法会信息");
                isuixi.putExtra("tab",MY_SUIXI);
                startActivity(isuixi);
                break;
            case R.id.persion_gdl_relative:
                Intent igdl = new Intent(getActivity(),MyGdlActivity.class);
                startActivity(igdl);
                break;
            case R.id.persion_news_relative:
                Intent inews = new Intent(getActivity(),ManagerActivity.class);
                startActivity(inews);
                break;
            case R.id.persion_loginout_button:
                onButtonPressed(MY_LOGOUT);
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                v.setBackgroundResource(R.drawable.press_up_concor);
                break;
            case MotionEvent.ACTION_UP:
                v.setBackgroundResource(R.drawable.nor_up_concor);
                break;
            case  MotionEvent.ACTION_HOVER_MOVE:
                v.setBackgroundResource(R.drawable.nor_up_concor);
                break;
        }

        return false;
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
        public void onFragmentInteraction(String uri);
    }

}
