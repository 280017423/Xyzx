package com.cn.xyzx.adapter;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cn.xyzx.R;
import com.cn.xyzx.bean.FileStateModel;
import com.cn.xyzx.download.AppConstant;
import com.cn.xyzx.download.DownLoadDao;

public class LocalDownLoadAdapter extends BaseAdapter {
	private List<FileStateModel> mDownLoadList;
	private DownLoadDao mDownLoadDao;
	private Context mContext;
	private OnClickListener mListener;
	private ListView mListView;

	public LocalDownLoadAdapter(Context context, List<FileStateModel> models, DownLoadDao downLoadDao,
			OnClickListener listener, ListView listView) {
		mDownLoadList = models;
		mDownLoadDao = downLoadDao;
		mContext = context;
		mListener = listener;
		mListView = listView;
	}

	final class ViewHolder {
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

		final String name = fileStateModel.getMusicName();
		holder.mTvFileName.setText(name);
		int state = fileStateModel.getState();
		if (0 == state) {
			// 下载完成的文件，进度条被隐藏
			holder.mProgressBar.setVisibility(View.INVISIBLE);
			holder.mTvPercent.setText(AppConstant.AdapterConstant.down_over);
			holder.mBtnPauseOrStart.setVisibility(View.GONE);
		} else if (1 == state) {
			// 如果状态是1,则是正在下载
			int completeSize = fileStateModel.getCompleteSize();
			int fileSize = fileStateModel.getFileSize();
			float num = (float) completeSize * 100 / (float) fileSize;
			holder.mProgressBar.setVisibility(View.VISIBLE);
			holder.mBtnPauseOrStart.setVisibility(View.GONE);
			holder.mProgressBar.setProgress((int) num);
			holder.mTvPercent.setText(num + "%");
			// 当文件下载完成
			if (fileStateModel.getCompleteSize() == fileStateModel.getFileSize()) {
				fileStateModel.setState(0);
				mDownLoadList.set(position, fileStateModel);
				holder.mProgressBar.setVisibility(View.INVISIBLE);
				if (fileStateModel.getCompleteSize() == 0) {
					holder.mTvPercent.setText(AppConstant.AdapterConstant.down_fail);
				} else {
					holder.mTvPercent.setText(AppConstant.AdapterConstant.down_over);
				}
			}
		}
		holder.mBtnDelete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mDownLoadDao.deleteFileState(name); // 从数据库中删除
				mDownLoadList.remove(position); // 从列表中删除
				mListener.onClick(v); // 从下载队列删除
				File file = new File(AppConstant.NetworkConstant.savePath + name);
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
					tvPercent.setText(AppConstant.AdapterConstant.down_fail);
				} else {
					tvPercent.setText(AppConstant.AdapterConstant.down_over);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
