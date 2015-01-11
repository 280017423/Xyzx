package com.cn.xyzx.activity;

import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.cn.xyzx.R;
import com.qianjiang.framework.util.PackageUtil;

public class AboutActivity extends ActivityBase implements OnClickListener {

	private String mVersionName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_about);
		initVariables();
		initView();
	}

	private void initVariables() {
		try {
			mVersionName =PackageUtil.getVersionName();
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void initView() {
		TextView tvVersion = (TextView) findViewById(R.id.tv_version_code);
		tvVersion.setText(getString(R.string.text_version_code, mVersionName));
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.ibtn_back:
				finish();
				break;
			default:
				break;
		}
	}
}
