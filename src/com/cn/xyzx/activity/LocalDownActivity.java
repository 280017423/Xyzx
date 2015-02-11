package com.cn.xyzx.activity;

import java.io.File;
import java.lang.ref.WeakReference;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cn.xyzx.R;
import com.cn.xyzx.download.DownloadInfo;
import com.cn.xyzx.download.DownloadManager;
import com.cn.xyzx.download.DownloadService;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;

public class LocalDownActivity extends ActivityBase implements OnClickListener {

	private ListView mLvDownload;
	private DownloadManager mDownloadManager;
	private DownloadListAdapter mLocalDownLoadAdapter;
	private Context mAppContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_local_download);
		initVariable();
		initViews();
	}

	private void initVariable() {
		mAppContext = getApplicationContext();
		mLocalDownLoadAdapter = new DownloadListAdapter(mAppContext);
		mDownloadManager = DownloadService.getDownloadManager(getApplicationContext());
	}

	@Override
	public void onResume() {
		super.onResume();
		mLocalDownLoadAdapter.notifyDataSetChanged();
	}

	private void initViews() {
		mLvDownload = (ListView) this.findViewById(R.id.listview);
		mLvDownload.setAdapter(mLocalDownLoadAdapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.ibtn_back:
				finish();
				break;
			default:
				break;
		}
	}

	private class DownloadListAdapter extends BaseAdapter {

		private final Context mContext;

		private DownloadListAdapter(Context context) {
			mContext = context;
		}

		@Override
		public int getCount() {
			if (mDownloadManager == null) {
				return 0;
			}
			return mDownloadManager.getDownloadInfoListCount();
		}

		@Override
		public Object getItem(int i) {
			return mDownloadManager.getDownloadInfo(i);
		}

		@Override
		public long getItemId(int i) {
			return i;
		}

		@Override
		public View getView(int i, View view, ViewGroup viewGroup) {
			DownloadItemViewHolder holder = null;
			DownloadInfo downloadInfo = mDownloadManager.getDownloadInfo(i);
			if (view == null) {
				view = View.inflate(mContext, R.layout.view_video_download_item, null);
				holder = new DownloadItemViewHolder();
				holder.label = (TextView) view.findViewById(R.id.download_label);
				holder.state = (TextView) view.findViewById(R.id.download_state);
				holder.progressBar = (ProgressBar) view.findViewById(R.id.download_pb);
				holder.stopBtn = (Button) view.findViewById(R.id.download_stop_btn);
				holder.removeBtn = (Button) view.findViewById(R.id.download_remove_btn);
				view.setTag(holder);
			} else {
				holder = (DownloadItemViewHolder) view.getTag();
			}
			holder.update(downloadInfo);

			HttpHandler<File> handler = downloadInfo.getHandler();
			if (handler != null) {
				RequestCallBack<File> callBack = handler.getRequestCallBack();
				if (callBack instanceof DownloadManager.ManagerCallBack) {
					DownloadManager.ManagerCallBack managerCallBack = (DownloadManager.ManagerCallBack) callBack;
					if (managerCallBack.getBaseCallBack() == null) {
						managerCallBack.setBaseCallBack(new DownloadRequestCallBack());
					}
				}
				callBack.setUserTag(new WeakReference<DownloadItemViewHolder>(holder));
			}

			return view;
		}
	}

	public class DownloadItemViewHolder {
		TextView label;
		TextView state;
		ProgressBar progressBar;
		Button stopBtn;
		Button removeBtn;

		private DownloadInfo downloadInfo;

		public void update(DownloadInfo downloadInfo) {
			this.downloadInfo = downloadInfo;
			refresh();
		}

		public void refresh() {
			if (null == downloadInfo) {
				return;
			}
			this.label.setText(downloadInfo.getFileName());
			state.setText(downloadInfo.getState().toString());
			if (downloadInfo.getFileLength() > 0) {
				progressBar.setProgress((int) (downloadInfo.getProgress() * 100 / downloadInfo.getFileLength()));
			} else {
				progressBar.setProgress(0);
			}

			stopBtn.setVisibility(View.VISIBLE);
			stopBtn.setText(mAppContext.getString(R.string.pause));
			HttpHandler.State state = downloadInfo.getState();
			switch (state) {
				case WAITING:
					stopBtn.setText(mAppContext.getString(R.string.pause));
					break;
				case STARTED:
					stopBtn.setText(mAppContext.getString(R.string.pause));
					break;
				case LOADING:
					stopBtn.setText(mAppContext.getString(R.string.pause));
					break;
				case CANCELLED:
					stopBtn.setText(mAppContext.getString(R.string.resume));
					break;
				case SUCCESS:
					stopBtn.setVisibility(View.GONE);
					break;
				case FAILURE:
					stopBtn.setText(mAppContext.getString(R.string.retry));
					break;
				default:
					break;
			}

			removeBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					try {
						mDownloadManager.removeDownload(downloadInfo);
						File downloadFile = new File(downloadInfo.getFileSavePath());
						downloadFile.delete();
						mLocalDownLoadAdapter.notifyDataSetChanged();
					} catch (DbException e) {
						LogUtils.e(e.getMessage(), e);
					}
				}
			});

			stopBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					HttpHandler.State state = downloadInfo.getState();
					switch (state) {
						case WAITING:
						case STARTED:
						case LOADING:
							try {
								mDownloadManager.stopDownload(downloadInfo);
							} catch (DbException e) {
								LogUtils.e(e.getMessage(), e);
							}
							break;
						case CANCELLED:
						case FAILURE:
							try {
								mDownloadManager.resumeDownload(downloadInfo, new DownloadRequestCallBack());
							} catch (DbException e) {
								LogUtils.e(e.getMessage(), e);
							}
							mLocalDownLoadAdapter.notifyDataSetChanged();
							break;
						default:
							break;
					}
				}
			});
		}
	}

	private class DownloadRequestCallBack extends RequestCallBack<File> {

		@SuppressWarnings("unchecked")
		private void refreshListItem() {
			if (userTag == null) {
				return;
			}
			WeakReference<DownloadItemViewHolder> tag = (WeakReference<DownloadItemViewHolder>) userTag;
			DownloadItemViewHolder holder = tag.get();
			if (holder != null) {
				holder.refresh();
			}
		}

		@Override
		public void onStart() {
			refreshListItem();
		}

		@Override
		public void onLoading(long total, long current, boolean isUploading) {
			refreshListItem();
		}

		@Override
		public void onSuccess(ResponseInfo<File> responseInfo) {
			refreshListItem();
		}

		@Override
		public void onFailure(HttpException error, String msg) {
			refreshListItem();
		}

		@Override
		public void onCancelled() {
			refreshListItem();
		}
	}

	@Override
	public void onDestroy() {
		try {
			if (mLocalDownLoadAdapter != null && mDownloadManager != null) {
				mDownloadManager.backupDownloadInfoList();
			}
		} catch (DbException e) {
			LogUtils.e(e.getMessage(), e);
		}
		super.onDestroy();
	}
}
