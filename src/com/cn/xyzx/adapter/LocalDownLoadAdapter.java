package com.cn.xyzx.adapter;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cn.xyzx.R;
import com.cn.xyzx.bean.FileStateModel;
import com.cn.xyzx.db.DownLoadDao;
import com.cn.xyzx.util.ServerAPIConstant;
import com.qianjiang.framework.imageloader.core.DisplayImageOptions;
import com.qianjiang.framework.imageloader.core.DisplayImageOptions.Builder;
import com.qianjiang.framework.imageloader.core.ImageLoader;
import com.qianjiang.framework.imageloader.core.display.SimpleBitmapDisplayer;
import com.qianjiang.framework.util.StringUtil;

public class LocalDownLoadAdapter extends BaseAdapter {
	private List<FileStateModel> mDownLoadList;
	private Context mContext;
	private OnClickListener mListener;
	private ListView mListView;
	private ImageLoader mImageDownloader;
	private Builder mOptions;

	public LocalDownLoadAdapter(Context context, List<FileStateModel> models, OnClickListener listener,
			ListView listView, ImageLoader loader) {
		mDownLoadList = models;
		mContext = context;
		mListener = listener;
		mListView = listView;
		mImageDownloader = loader;
		mOptions = new DisplayImageOptions.Builder().cacheInMemory().cacheOnDisc()
				.displayer(new SimpleBitmapDisplayer());
	}

	final class ViewHolder {
		public ImageView mIvVideoPic; // 显示文件名称
		public TextView mTvFileName; // 显示文件名称
		public ProgressBar mProgressBar; // 进度条
		public TextView mTvPercent; // 百分比
		public Button mBtnDelete; // 删除按钮
		public Button mBtnPauseOrStart; // 暂停删除按钮
		public View mView;
	}

	@Override
	public int getCount() {
		if (null == mDownLoadList) {
			return 0;
		}
		return mDownLoadList.size();
	}

	@Override
	public FileStateModel getItem(int position) {
		if (null == mDownLoadList) {
			return null;
		}
		return mDownLoadList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		final FileStateModel fileStateModel = mDownLoadList.get(position);
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.view_local_download_item, null);
			holder = new ViewHolder();
			holder.mView = convertView.findViewById(R.id.rl_local_layout);
			holder.mIvVideoPic = (ImageView) convertView.findViewById(R.id.iv_file_icon);
			holder.mTvFileName = (TextView) convertView.findViewById(R.id.tv_file_name);
			holder.mProgressBar = (ProgressBar) convertView.findViewById(R.id.pb_download);
			holder.mTvPercent = (TextView) convertView.findViewById(R.id.tv_percent);
			holder.mBtnDelete = (Button) convertView.findViewById(R.id.btn_local_delete);
			holder.mBtnPauseOrStart = (Button) convertView.findViewById(R.id.btn_start_pause_download);
			holder.mBtnPauseOrStart.setTag(fileStateModel);
			holder.mBtnDelete.setTag(fileStateModel.getUrl());
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.mView.setTag(fileStateModel.getUrl());

		mImageDownloader.displayImage(fileStateModel.getPicture(), holder.mIvVideoPic,
				mOptions.showImageForEmptyUri(R.drawable.news_default).build());

		final String title = fileStateModel.getTitle();
		final String name = fileStateModel.getFileName();
		holder.mTvFileName.setText(StringUtil.isNullOrEmpty(title) ? name : title);
		int state = fileStateModel.getState();
		if (0 == state) {
			holder.mBtnPauseOrStart.setVisibility(View.GONE);
			if (fileStateModel.getCompleteSize() == fileStateModel.getFileSize()) {
				if (fileStateModel.getCompleteSize() == 0) {
					holder.mProgressBar.setVisibility(View.VISIBLE);
					holder.mTvPercent.setText(R.string.status_download_fail);
				} else {
					holder.mProgressBar.setVisibility(View.GONE);
					holder.mTvPercent.setText(R.string.status_has_download);
				}
			}
		} else if (1 == state) {
			// 如果状态是1,则是正在下载
			int completeSize = fileStateModel.getCompleteSize();
			int fileSize = fileStateModel.getFileSize();
			float num = (float) completeSize * 100 / (float) fileSize;
			holder.mProgressBar.setVisibility(View.VISIBLE);
			holder.mBtnPauseOrStart.setVisibility(View.GONE);
			holder.mProgressBar.setProgress((int) num);
			holder.mTvPercent.setText((int) num + "%");
			// 当文件下载完成
			if (fileStateModel.getCompleteSize() == fileStateModel.getFileSize()) {
				fileStateModel.setState(0);
				mDownLoadList.set(position, fileStateModel);
				holder.mProgressBar.setVisibility(View.GONE);
				if (fileStateModel.getCompleteSize() == 0) {
					holder.mTvPercent.setText(R.string.status_download_fail);
				} else {
					holder.mTvPercent.setText(R.string.status_has_download);
				}
			}
		}
		holder.mBtnDelete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DownLoadDao.deleteFileState(name); // 从数据库中删除
				mDownLoadList.remove(position); // 从列表中删除
				mListener.onClick(v); // 从下载队列删除
				File file = new File(ServerAPIConstant.getDownloadPath() + name);
				file.delete(); // 在存储器中删除
				notifyDataSetChanged();
			}
		});
		return convertView;
	}

	public void updateProgress(FileStateModel fileStateModel) {
		try {
			View contain = mListView.findViewWithTag(fileStateModel.getUrl());
			ProgressBar progressBar = (ProgressBar) contain.findViewById(R.id.pb_download);
			TextView tvPercent = (TextView) contain.findViewById(R.id.tv_percent);

			int completeSize = fileStateModel.getCompleteSize();
			int fileSize = fileStateModel.getFileSize();
			float num = (float) completeSize * 100 / (float) fileSize;
			progressBar.setVisibility(View.VISIBLE);
			progressBar.setProgress((int) num);
			tvPercent.setText((int) num + "%");
			// 当文件下载完成
			if (fileStateModel.getCompleteSize() == fileStateModel.getFileSize()) {
				fileStateModel.setState(0);
				progressBar.setVisibility(View.INVISIBLE);
				if (fileStateModel.getCompleteSize() == 0) {
					tvPercent.setText(R.string.status_download_fail);
				} else {
					tvPercent.setText(R.string.status_has_download);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
