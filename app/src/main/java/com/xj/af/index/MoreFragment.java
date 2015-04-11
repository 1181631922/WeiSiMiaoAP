package com.xj.af.index;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xj.af.common.BaseFragment;
import com.xj.af.R;

/**
 * 没什么用
 */
public class MoreFragment extends BaseFragment {
	private ProgressBar progressBar;
	private TextView textView;
	private int tabIndex;
	public static final String INTENT_INT_INDEX = "intent_int_index";

	@Override
	protected void onCreateView(Bundle savedInstanceState) {
		super.onCreateView(savedInstanceState);
		setContentView(R.layout.fragment_tabmain_item);
		tabIndex = getArguments().getInt(INTENT_INT_INDEX);
		progressBar = (ProgressBar) findViewById(R.id.fragment_mainTab_item_progressBar);
		textView = (TextView) findViewById(R.id.fragment_mainTab_item_textView);
		textView.setText("界面" + " " + tabIndex + " 加载完毕");
		handler.sendEmptyMessageDelayed(1, 1000);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		handler.removeMessages(1);
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			progressBar.setVisibility(View.GONE);
			textView.setVisibility(View.VISIBLE);
		}
	};
}
