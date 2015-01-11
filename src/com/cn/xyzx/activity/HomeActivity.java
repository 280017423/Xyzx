package com.cn.xyzx.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.cn.xyzx.R;

public class HomeActivity extends ActivityBase implements OnItemClickListener, OnClickListener,
		android.content.DialogInterface.OnClickListener {

	private GridView mGvItems;
	private int images[];
	private SimpleAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		initVariables();
		initViews();

	}

	private void initVariables() {
		ArrayList<HashMap<String, Object>> lst_ImageItem = new ArrayList<HashMap<String, Object>>();
		images = new int[] {
				R.drawable.home01,
				R.drawable.home02,
				R.drawable.home03,
				R.drawable.home05,
				R.drawable.home06,
				R.drawable.home07, };
		for (int i = 0; i < images.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("ItemImage", images[i]);
			lst_ImageItem.add(map);
		}
		mAdapter = new SimpleAdapter(
				this, lst_ImageItem, R.layout.home_item, new String[] { "ItemImage" }, new int[] { R.id.ItemImage });
	}

	private void initViews() {
		mGvItems = (GridView) findViewById(R.id.home_gridview);
		mGvItems.setAdapter(mAdapter);
		mGvItems.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent infoCenter = new Intent();
		Bundle bundle = new Bundle();
		switch (position) {
		// 鑫亚概况
			case 0:
				bundle.putInt("parent_id", 4);
				infoCenter.putExtras(bundle);
				infoCenter.setClass(this, InfoCenterActivity.class);
				this.startActivity(infoCenter);
				break;
			// 产品展馆
			case 1:
				infoCenter.setClass(this, ProductActivity.class);
				startActivity(infoCenter);
				break;
			// 资讯中心
			case 2:
				infoCenter.setClass(this, NewsActivity.class);
				startActivity(infoCenter);
				break;
			// 学习中心
			case 3:
				infoCenter.setData(Uri.parse("http://www.sinya99.com"));
				infoCenter.setClass(this, WebViewActivity.class);
				startActivity(infoCenter);
				break;
			// 交流中心
			case 4:
				bundle.putInt("parent_id", 2);
				infoCenter.putExtras(bundle);
				infoCenter.setClass(this, InfoCenterActivity.class);
				this.startActivity(infoCenter);
				break;
			// 我的商务
			case 5:
				bundle.putInt("parent_id", 3);
				infoCenter.putExtras(bundle);
				infoCenter.setClass(this, InfoCenterActivity.class);
				this.startActivity(infoCenter);
				break;

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.bt_Bottom_menu:
				break;
			case R.id.bt_Bottom_back:
				exit();
				break;

			case R.id.bt_Bottom_down:
				break;
			case R.id.bt_Bottom_about:
				Intent intentAbout = new Intent();
				intentAbout.setClass(this, AboutActivity.class);
				this.startActivity(intentAbout);
				break;
			default:
				break;
		}
	}

	private void exit() {
		new AlertDialog.Builder(this).setTitle("确认").setMessage("确定退出吗？").setPositiveButton("是", this)
				.setNegativeButton("否", this).show();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch (which) {
			case -1:
				finish();
				break;
			default:
				break;
		}
	}

	@Override
	public void onBackPressed() {
		exit();
	}
}
