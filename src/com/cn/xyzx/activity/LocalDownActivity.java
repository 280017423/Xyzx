package com.cn.xyzx.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.cn.xyzx.R;
import com.cn.xyzx.adapter.LocalDownLoadAdapter;
import com.cn.xyzx.bean.FileStateModel;
import com.cn.xyzx.db.DownLoadDao;
import com.cn.xyzx.download.DownloadService;
import com.cn.xyzx.download.DownloadService.PunchBinder;
import com.cn.xyzx.util.ServerAPIConstant;
import com.qianjiang.framework.util.StringUtil;
import com.qianjiang.framework.util.UIUtil;

public class LocalDownActivity extends ActivityBase {

	private static final int DELAY_TIME = 2000;
	private ListView mLvDownload;
	private List<FileStateModel> mFileStateModels;// 用于存放要显示的列表
	private LocalDownLoadAdapter mLocalDownLoadAdapter;// 自定义adapter
	private UpdateReceiver mUpdateReceiver;// 广播接收器
	private DownloadService mService;
	private TextView mTvEmptyContent;
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
		setContentView(R.layout.activity_local_download);
		bindPunchService();
		initVariable();
		initViews();
	}

	private void initVariable() {
		mFileStateModels = new ArrayList<FileStateModel>();
		mUpdateReceiver = new UpdateReceiver();
		mUpdateReceiver.registerAction(ServerAPIConstant.ACTION_UPDATE_DOWNLOAD_PROGRESS);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mFileStateModels.clear();
		mFileStateModels.addAll(DownLoadDao.getFileState());
		mLocalDownLoadAdapter.notifyDataSetChanged();
		if (null == mFileStateModels || mFileStateModels.isEmpty()) {
			mTvEmptyContent.setVisibility(View.VISIBLE);
			mLvDownload.setVisibility(View.GONE);
		} else {
			mTvEmptyContent.setVisibility(View.GONE);
			mLvDownload.setVisibility(View.VISIBLE);
		}
		for (int i = 0; i < mFileStateModels.size(); i++) {
			FileStateModel fileState = mFileStateModels.get(i);
			if (null == fileState || StringUtil.isNullOrEmpty(fileState.getFileName())
					|| StringUtil.isNullOrEmpty(fileState.getUrl())) {
				return;
			}
			if (null != mService) {
				mService.reStartDownload(fileState.getFileName(), fileState.getUrl());
			}
		}
	}

	private void initViews() {
		mTvEmptyContent = (TextView) findViewById(R.id.tv_empty_content);
		mLvDownload = (ListView) this.findViewById(R.id.listview);
		mLocalDownLoadAdapter = new LocalDownLoadAdapter(this, mFileStateModels, new OnClickListener() {

			@Override
			public void onClick(final View v) {
				UIUtil.limitReClick(LocalDownActivity.class.getName(), DELAY_TIME, new ActionListener() {

					@Override
					public void doAction() {
						switch (v.getId()) {
							case R.id.btn_local_delete:
								mService.deleteData((String) v.getTag());
								if (null == mFileStateModels || mFileStateModels.isEmpty()) {
									mTvEmptyContent.setVisibility(View.VISIBLE);
									mLvDownload.setVisibility(View.GONE);
								} else {
									mTvEmptyContent.setVisibility(View.GONE);
									mLvDownload.setVisibility(View.VISIBLE);
								}
								break;
							case R.id.btn_start_pause_download:
								break;

							default:
								break;
						}
					}
				});
			}
		}, mLvDownload, mImageLoader);
		mLvDownload.setAdapter(mLocalDownLoadAdapter);
		mLvDownload.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
				UIUtil.limitReClick(LocalDownActivity.class.getName(), DELAY_TIME, new ActionListener() {

					@Override
					public void doAction() {
						FileStateModel fileState = (FileStateModel) parent.getAdapter().getItem(position);
						if (null == fileState || StringUtil.isNullOrEmpty(fileState.getFileName())
								|| StringUtil.isNullOrEmpty(fileState.getUrl()) || fileState.isComplete()) {
							return;
						}
						mService.switchState(fileState.getFileName(), fileState.getUrl());
					}
				});
			}
		});
	}

	class UpdateReceiver extends BroadcastReceiver {

		public void registerAction(String action) {
			IntentFilter intentFilter = new IntentFilter();
			intentFilter.addAction(action);
			registerReceiver(this, intentFilter);
		}

		@Override
		public void onReceive(Context context, Intent intent) {
			// 接收来自DownloadService传送过来的数据,并且更新进度条
			if (intent.getAction().equals(ServerAPIConstant.ACTION_UPDATE_DOWNLOAD_PROGRESS)) {
				String url = intent.getStringExtra("url");
				int completeSize = intent.getIntExtra("completeSize", 0);
				for (int i = 0; i < mFileStateModels.size(); i++) {
					FileStateModel fileState = mFileStateModels.get(i);
					if (fileState.getUrl().equals(url)) {
						fileState.setCompleteSize(completeSize);
						mLocalDownLoadAdapter.updateProgress(fileState);
						break;
					}
				}
			}
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
		unregisterReceiver(mUpdateReceiver);
		DownLoadDao.updateFileDownState(mFileStateModels);// 当activity退出时,更新localdown_info这个表
		super.onDestroy();
	}

}
