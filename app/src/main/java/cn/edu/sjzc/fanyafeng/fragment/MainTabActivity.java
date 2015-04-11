package cn.edu.sjzc.fanyafeng.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import com.xj.af.R;

/**
 */
public class MainTabActivity extends FragmentActivity {
	private FragmentTabHost mTabHost;

	private LayoutInflater layoutInflater;

	private Class fragmentArray[] = { FragmentPage1.class, FragmentPage2.class,
			FragmentPage3.class, ExhibitionFragment.class, FragmentPage5.class };

	private int mImageViewArray[] = { R.drawable.tab_home_btn,
			R.drawable.tab_message_btn,
            R.drawable.tab_selfinfo_btn,
			R.drawable.tab_square_btn,
            R.drawable.tab_more_btn };

	private String mTextviewArray[] = { "寺院服务", "流通处", "素食", "展会", "更多" };

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_tab_layout);

		initView();
	}

	/**
	 */
	private void initView() {
		layoutInflater = LayoutInflater.from(this);

		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
		
		int count = fragmentArray.length;

		for (int i = 0; i < count; i++) {
			TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i])
					.setIndicator(getTabItemView(i));

			mTabHost.addTab(tabSpec, fragmentArray[i], null);
			mTabHost.getTabWidget().getChildAt(i)
					.setBackgroundResource(R.drawable.selector_tab_background);
			
		}
	}

	/**
	 */
	private View getTabItemView(int index) {
		View view = layoutInflater.inflate(R.layout.tab_item_view, null);

		ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
		imageView.setImageResource(mImageViewArray[index]);

		TextView textView = (TextView) view.findViewById(R.id.textview);
		textView.setText(mTextviewArray[index]);

		return view;
	}
}
