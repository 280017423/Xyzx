package com.cn.xyzx.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;

import com.cn.xyzx.R;
import com.cn.xyzx.adapter.InfoFragmentAdapter;
import com.cn.xyzx.fragment.EnterpriseCultureFragment;
import com.cn.xyzx.fragment.EnterpriseIntroduceFragment;
import com.cn.xyzx.fragment.HonorFragment;
import com.cn.xyzx.fragment.LeadFragment;
import com.cn.xyzx.fragment.ResponsibilityFragment;
import com.cn.xyzx.fragment.RunHeFragment;
import com.cn.xyzx.fragment.VideoFragment;
import com.cn.xyzx.widget.LineTabIndicator;
import com.qianjiang.framework.widget.LoadingUpView;

public class InfoCenterActivity extends FragmentActivityBase implements OnClickListener {
	private ViewPager mViewPager;
	private LineTabIndicator mLineTabIndicator;
	private InfoFragmentAdapter mPageAdapter;
	private LoadingUpView mLoadingUpView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info_center);
		initVariables();
		initView();
		initFragments();
	}

	private void initVariables() {
		mLoadingUpView = new LoadingUpView(this);
	}

	private void initView() {
		mViewPager = (ViewPager) findViewById(R.id.viewpager_infoCenter);
		mLineTabIndicator = (LineTabIndicator) findViewById(R.id.line_tab_indicator);
	}

	private void initFragments() {
		List<Fragment> fragments = new ArrayList<Fragment>();

		EnterpriseIntroduceFragment fragment1 = EnterpriseIntroduceFragment.newInstance();
		fragments.add(fragment1);

		LeadFragment fragment2 = LeadFragment.newInstance("572");
		fragments.add(fragment2);

		EnterpriseCultureFragment fragment3 = EnterpriseCultureFragment.newInstance();
		fragments.add(fragment3);

		VideoFragment videoFragment = VideoFragment.newInstance();
		fragments.add(videoFragment);

		HonorFragment fragment5 = HonorFragment.newInstance("573");
		fragments.add(fragment5);

		ResponsibilityFragment fragment6 = ResponsibilityFragment.newInstance("574");
		fragments.add(fragment6);

		RunHeFragment fragment7 = RunHeFragment.newInstance(0);
		fragments.add(fragment7);
		String[] titles = getResources().getStringArray(R.array.titles);
		mPageAdapter = new InfoFragmentAdapter(getSupportFragmentManager(), fragments, titles);
		mViewPager.setAdapter(mPageAdapter);
		mLineTabIndicator.setViewPager(mViewPager);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.bt_Bottom_back:
				finish();
				break;
			default:
				break;
		}
	}

	public void showLoading() {
		if (null != mLoadingUpView && !mLoadingUpView.isShowing()) {
			mLoadingUpView.showPopup();
		}
	}

	public void dismissLoading() {
		if (null != mLoadingUpView && mLoadingUpView.isShowing()) {
			mLoadingUpView.dismiss();
		}
	}
}
