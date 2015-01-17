package com.cn.xyzx.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.cn.xyzx.R;

public class LoadingActivity extends ActivityBase {
	private static final int DISPLAY_TIME = 3000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_loading);
		jumpToMain();
	}

	private void jumpToMain() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Intent openMainActivity = new Intent(LoadingActivity.this, HomeActivity.class);
				startActivity(openMainActivity);
				finish();
			}
		}, DISPLAY_TIME);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
