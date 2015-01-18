package com.cn.xyzx.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.cn.xyzx.R;
import com.cn.xyzx.adapter.InfoFragmentAdapter;
import com.cn.xyzx.download.DownloadService;
import com.cn.xyzx.download.DownloadService.PunchBinder;
import com.cn.xyzx.fragment.EnterpriseIntroduceFragment;
import com.cn.xyzx.fragment.HonorFragment;
import com.cn.xyzx.fragment.LeadFragment;
import com.cn.xyzx.fragment.ResponsibilityFragment;
import com.cn.xyzx.fragment.RunHeFragment;
import com.cn.xyzx.fragment.VideoFragment;
import com.cn.xyzx.util.ServerAPIConstant;
import com.cn.xyzx.widget.LineTabIndicator;
import com.qianjiang.framework.widget.LoadingUpView;

public class InfoCenterActivity extends FragmentActivityBase implements OnClickListener {
	private ViewPager mViewPager;
	private LineTabIndicator mLineTabIndicator;
	private InfoFragmentAdapter mPageAdapter;
	private LoadingUpView mLoadingUpView;
	private DownloadService mService;
	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mService = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			PunchBinder binder = (PunchBinder) service;
			mService = binder.getService();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info_center);
		bindPunchService();
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

		String url = ServerAPIConstant.getApiRootUrl() + ServerAPIConstant.API_GEI_ENTERPRISE_INTRODUCE;
		EnterpriseIntroduceFragment fragment1 = EnterpriseIntroduceFragment.newInstance(url);
		fragments.add(fragment1);

		LeadFragment fragment2 = LeadFragment.newInstance("572");
		fragments.add(fragment2);

		String url3 = ServerAPIConstant.getApiRootUrl() + ServerAPIConstant.API_GEI_ENTERPRISE_CULTURE;
		EnterpriseIntroduceFragment fragment3 = EnterpriseIntroduceFragment.newInstance(url3);
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

	protected void bindPunchService() {
		Intent mIntent = new Intent(this, DownloadService.class);
		bindService(mIntent, mConnection, Context.BIND_AUTO_CREATE);
	}

	protected void unbindPunchService() {
		try {
			unbindService(mConnection);
			Log.d("aaa", "unbindPunchService");
		} catch (IllegalArgumentException e) {
			Log.d("aaa", "Service wasn't bound!");
		}
	}

	@Override
	protected void onDestroy() {
		unbindPunchService();
		super.onDestroy();
	}

	public int startDownload(final String fileName, String title, final String downPath, String picUrl) {
		return mService.startDownload(fileName, title, downPath, picUrl);
	}

}
