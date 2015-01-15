package com.cn.xyzx.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.cn.xyzx.R;
import com.cn.xyzx.widget.CustomDialog.Builder;

public class HomeActivity extends ActivityBase implements OnItemClickListener, OnClickListener {

	private static final int DIALOG_EXIT_APP = 0;
	private GridView mGvItems;
	private int images[];
	private SimpleAdapter mAdapter;
	private CheckBox mCbNoNotice;

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
				this, lst_ImageItem, R.layout.view_home_item, new String[] { "ItemImage" },
				new int[] { R.id.ItemImage });
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
				infoCenter.setData(Uri.parse("http://www.sinya99.com"));
				infoCenter.setClass(this, WebViewActivity.class);
				startActivity(infoCenter);
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
		showDialog(DIALOG_EXIT_APP);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
			case DIALOG_EXIT_APP:
				Builder builder = createDialogBuilder(this, getString(R.string.button_text_tips),
						getString(R.string.exit_dialog_title), getString(R.string.button_text_no),
						getString(R.string.button_text_yes));
				View view = View.inflate(this, R.layout.view_clear_cache, null);
				mCbNoNotice = (CheckBox) view.findViewById(R.id.cb_no_notice);
				builder.setmDialogView(view);
				Dialog updateDialog = builder.create(id);
				// 小米手机，默认点击对话框外部也可关闭，需要多一部设置
				updateDialog.setCanceledOnTouchOutside(false);
				updateDialog.setCancelable(true);
				return updateDialog;
			default:
				break;
		}
		return super.onCreateDialog(id);
	}

	@Override
	public void onNegativeBtnClick(int id, DialogInterface dialog, int which) {
		switch (id) {
			case DIALOG_EXIT_APP:
				if (mCbNoNotice.isChecked()) {
					clearWebViewCache();
				}
				finish();
				break;
			default:
				break;
		}
		super.onNegativeBtnClick(id, dialog, which);
	}

	/**
	 * 清除WebView缓存
	 */
	public void clearWebViewCache() {
		try {
			deleteDatabase("xyzx.db");
		} catch (Exception e) {
			e.printStackTrace();
		}
		File appCacheDir = new File(getDir("webview", MODE_WORLD_WRITEABLE).getAbsolutePath());
		Log.d("aaa", "appCacheDir path=" + appCacheDir.getAbsolutePath());
		deleteDirRecursive(appCacheDir);
	}

	public static void deleteDirRecursive(File dir) {
		if (dir == null || !dir.exists() || !dir.isDirectory()) {
			return;
		}
		File[] files = dir.listFiles();
		if (files == null) {
			return;
		}
		for (File f : files) {
			if (f.isFile()) {
				f.delete();
			} else {
				deleteDirRecursive(f);
			}
		}
	}

	@Override
	public void onBackPressed() {
		exit();
	}
}
