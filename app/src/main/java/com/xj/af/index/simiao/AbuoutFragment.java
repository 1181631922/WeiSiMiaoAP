package com.xj.af.index.simiao;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.xj.af.R;
import com.xj.af.common.BaseFragment;
import com.xj.af.common.BaseXjFragment;
import com.xj.af.entity.Unit;
import com.xj.af.util.JsonUtil;
import com.xj.af.util.StrUtil;
import com.xj.af.util.ThreadUtil;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AbuoutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AbuoutFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Button button;
    public String phoneNumber;
    private Handler handler;
    private TextView tv;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AbuoutFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AbuoutFragment newInstance(String param1, String param2) {
        AbuoutFragment fragment = new AbuoutFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public AbuoutFragment() {
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
        setContentView(R.layout.fragment_abuout);
        button = (Button) findViewById(R.id.about_call_Button);
        tv = (TextView) findViewById(R.id.about_textView);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /**一键直呼设置电话*/
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Bundle bd = msg.getData();
                if (msg.what == 0) {
                    String jsonStr = bd.getString("imsg");
                    Unit unit = JsonUtil.getEntity(jsonStr, Unit.class);
                    phoneNumber = unit.getPhoneNumber();
                    button.setOnClickListener(new CallOnClick());
                    String str = "";
                    if (StrUtil.isNotBlank(unit.getPhoneNumber()))
                        str += "\n电话：" + unit.getPhoneNumber();
                    if (StrUtil.isNotBlank(unit.getAddress()))
                        str += "\n地址：" + unit.getAddress();
                    if (StrUtil.isNotBlank(unit.getDomain()))
                        str += "\n网址：" + unit.getDomain();
                    if (StrUtil.isNotBlank(unit.getQq()))
                        str += "\nQQ交流群：" + unit.getQq();
                    if (StrUtil.isNotBlank(unit.getWeixin()))
                        str += "\n微信：" + unit.getWeixin();
                    tv.setText(str);
                } else {
                    Toast.makeText(getActivity(), bd.getString("emsg"), Toast.LENGTH_SHORT).show();
                }
            }
        };

        new ThreadUtil(handler, getUrl("/api/unit/") + getUnitId()).start();

    }

    /**
     * 一键直呼
     */
    class CallOnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (StrUtil.isNotBlank(phoneNumber)) {
                Uri uri = Uri.parse("tel:" + phoneNumber) ;	// 设置操作的路径
                Intent it = new Intent() ;
                it.setAction(Intent.ACTION_DIAL) ;	// 设置要操作的Action
                it.setData(uri) ;	// 要设置的数据
                startActivity(it) ;

//                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+ phoneNumber));
//                startActivity(intent);
            } else {
                Toast.makeText(getActivity(), "电话号码错误:" + phoneNumber, Toast.LENGTH_SHORT)
                        .show();
            }
        }

    }

}
