package com.cn.xyzx.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.xyzx.R;
import com.cn.xyzx.adapter.DocumentAdapter;
import com.cn.xyzx.bean.DocumentModel;
import com.cn.xyzx.db.DbDao;
import com.cn.xyzx.download.HttpDownloader;
import com.cn.xyzx.req.DocumentReq;
import com.cn.xyzx.util.ActionResult;
import com.cn.xyzx.util.OpenFileUtil;
import com.cn.xyzx.util.ServerAPIConstant;
import com.cn.xyzx.widget.CustomProgressDialog;
import com.cn.xyzx.widget.CustomProgressDialog.DIALOG_DEFAULT_LAYOUT_TYPE;
import com.qianjiang.framework.util.AppDownloader.DownloadProgressListener;
import com.qianjiang.framework.util.EvtLog;
import com.qianjiang.framework.util.NetUtil;
import com.qianjiang.framework.util.StringUtil;
import com.qianjiang.framework.widget.LoadingUpView;

public class StudyCenterActivity extends ActivityBase implements OnClickListener, OnItemClickListener {

	private static final int DOWN_LOAD_SUCCESS = 1;
	private static final int DOWN_LOAD_FAIL = -1;
	private static final int DOWN_LOAD_ING = 0;
	private List<DocumentModel> mDocumentList;
	private GridView mGvDocument;
	private DocumentAdapter mAdapter;
	private LoadingUpView mLoadingUpView;
	private CustomProgressDialog mCustomProgressDialog;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
				case DOWN_LOAD_SUCCESS:
					mCustomProgressDialog.dismiss();
					toast("下载成功");
					break;
				case DOWN_LOAD_FAIL:
					mCustomProgressDialog.dismiss();
					toast("下载失败");
					break;
				case DOWN_LOAD_ING:
					final int progress = msg.arg1;
					int max = msg.arg2;
					ProgressBar progressBar = mCustomProgressDialog.getProgressBar();
					final TextView tvProgress = mCustomProgressDialog.getProgressText();
					if (progressBar != null) {
						progressBar.setMax(max);
						progressBar.setProgress(progress);
					}
					tvProgress.setText("当前进度: " + progress * 100 / max + "%");
					break;

				default:
					break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_study_center);
		initVariables();
		initView();
	}

	private void initVariables() {
		mDocumentList = new ArrayList<DocumentModel>();
		mAdapter = new DocumentAdapter(this, mDocumentList, mImageLoader);
		mLoadingUpView = new LoadingUpView(this);
		mCustomProgressDialog = new CustomProgressDialog(this, DIALOG_DEFAULT_LAYOUT_TYPE.FORCE_UPDATE);
	}

	private void initView() {
		mGvDocument = (GridView) findViewById(R.id.study_gridView);
		mGvDocument.setNumColumns(3);
		mGvDocument.setAdapter(mAdapter);
		mGvDocument.setOnItemClickListener(this);
		getFileList();
	}

	private void getFileList() {
		List<DocumentModel> list = DbDao.getModels(DocumentModel.class);
		if (null == list || list.isEmpty()) {
			showLoadingUpView(mLoadingUpView);
		} else {
			mDocumentList.addAll(list);
			mAdapter.notifyDataSetChanged();
		}
		new AsyncLogin().execute();
	}

	class AsyncLogin extends AsyncTask<Void, Void, ActionResult> {

		@Override
		protected void onPostExecute(ActionResult result) {
			if (result != null && ActionResult.RESULT_CODE_SUCCESS.equals(result.ResultCode)) {

			} else {
				showErrorMsg(result);
			}
			mDocumentList.clear();
			mDocumentList.addAll(DbDao.getModels(DocumentModel.class));
			mAdapter.notifyDataSetChanged();
			dismissLoadingUpView(mLoadingUpView);
		}

		@Override
		protected ActionResult doInBackground(Void... params) {
			return DocumentReq.getFileList();
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.bt_Bottom_back:
				finish();
				break;
			default:
				break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (!Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			toast(getString(R.string.no_sd_card));
			return;
		}
		if (!NetUtil.isNetworkAvailable()) {
			toast(getString(R.string.network_is_not_available));
			return;
		}
		final DocumentModel model = (DocumentModel) parent.getAdapter().getItem(position);
		if (null == model || StringUtil.isNullOrEmpty(model.getFileName())
				|| StringUtil.isNullOrEmpty(model.getVideoUrl())) {
			return;
		}
		final String savePath = ServerAPIConstant.getDownloadPath();// 保存地址
		final File tempFile = new File(savePath + model.getFileName());
		if (null != tempFile && tempFile.exists()) {
			Intent intent = OpenFileUtil.openFile(tempFile, StudyCenterActivity.this);
			if (null != intent) {
				startActivity(intent);
			} else {
				Toast.makeText(StudyCenterActivity.this, getString(R.string.no_app_found), Toast.LENGTH_LONG).show();
			}
			return;
		}
		mCustomProgressDialog.show();
		ProgressBar progressBar = mCustomProgressDialog.getProgressBar();
		progressBar.setProgress(0);

		new Thread(new Runnable() {

			@Override
			public void run() {
				HttpDownloader downloader = new HttpDownloader();

				downloader.downFile(model.getVideoUrl(), savePath, model.getFileName(), new DownloadProgressListener() {

					@Override
					public void onError(boolean isUpgradeMust) {
						mHandler.sendEmptyMessage(DOWN_LOAD_FAIL);
					}

					@Override
					public void onDownloading(int progress, int max) {
						EvtLog.d("aaa", "progress:" + progress + "---" + "max:" + max);
						if (0 != max) {
							Message msg = mHandler.obtainMessage();
							msg.arg1 = progress;
							msg.arg2 = max;
							mHandler.sendMessage(msg);
						}
					}

					@Override
					public void onDownloadComplete() {
						mHandler.sendEmptyMessage(DOWN_LOAD_SUCCESS);
						if (null != tempFile && tempFile.exists()) {
							StudyCenterActivity.this.runOnUiThread(new Runnable() {

								@Override
								public void run() {
									Intent intent = OpenFileUtil.openFile(tempFile, StudyCenterActivity.this);
									if (null != intent) {
										startActivity(intent);
									} else {
										Toast.makeText(StudyCenterActivity.this, getString(R.string.no_app_found),
												Toast.LENGTH_LONG).show();
									}
								}
							});
						}
					}
				});
			}
		}).start();
	}
}
