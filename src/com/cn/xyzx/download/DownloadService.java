package com.cn.xyzx.download;

import java.util.HashMap;
import java.util.Map;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.cn.xyzx.R;
import com.cn.xyzx.bean.FileStateModel;
import com.cn.xyzx.bean.LoadInfoModel;
import com.cn.xyzx.db.DownLoadDao;
import com.cn.xyzx.util.ServerAPIConstant;

public class DownloadService extends Service {
	public static Map<String, Downloader> mDownloadersMap = new HashMap<String, Downloader>();;
	// 存放每个下载文件完成的长度
	private Map<String, Integer> mCompleteSizes = new HashMap<String, Integer>();
	// 存放每个下载文件的总长度
	private Map<String, Integer> mFileSizes = new HashMap<String, Integer>();
	private Downloader mDownloader;
	private IBinder mBinder = new PunchBinder();
	private Handler mHandler = new Handler() {

		/**
		 * 接收Download中每个线程传输过来的数据
		 * **/
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				String url = (String) msg.obj;
				int length = msg.arg1;
				if (mCompleteSizes.get(url) != null && null != mFileSizes.get(url)) {
					int completeSize = mCompleteSizes.get(url);
					int fileSize = mFileSizes.get(url);
					completeSize += length;
					mCompleteSizes.put(url, completeSize);
					if (0 != fileSize && completeSize == fileSize) {
						// 如果文件下载完成,更新localdown_info表中文件的状态为0
						// 之所以在这里判断而不在activity是为了在activity没有启动的状态时也能更新
						DownLoadDao.updateFileState(url, 0);
					}
					Intent intent = new Intent();
					intent.setAction(ServerAPIConstant.ACTION_UPDATE_DOWNLOAD_PROGRESS);
					intent.putExtra("completeSize", completeSize);
					intent.putExtra("url", url);
					sendBroadcast(intent);
				}
			} else if (msg.what == -1) {
				Toast.makeText(DownloadService.this, R.string.status_download_fail, Toast.LENGTH_LONG).show();
			}
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		Log.d("aaa", "onBind");
		return mBinder;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		Log.d("aaa", "onUnbind");
		return true;
	}

	@Override
	public void onCreate() {
		Log.d("aaa", "onCreate");
		super.onCreate();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		Log.d("aaa", "onStart");
		super.onStart(intent, startId);
	}

	public void startDownload(final String fileName, final String title, final String downPath, final String picUrl) {
		// 先从数据库中判断，这个文件是否已经在下载列表了
		if (DownLoadDao.hasFile(fileName)) {
			Toast.makeText(getApplicationContext(), R.string.video_has_download, Toast.LENGTH_SHORT).show();
			return;
		}
		String savePath = ServerAPIConstant.getDownloadPath();// 保存地址
		mDownloader = mDownloadersMap.get(downPath);
		if (mDownloader == null) {
			mDownloader = new Downloader(downPath, savePath, fileName, 1, this, mHandler);
			mDownloadersMap.put(downPath, mDownloader);// 创建完一个新的下载器,必须把它加入到下载器集合里去
		}
		if (mDownloader.isdownloading()) {
			return;
		}
		// LoadInfo是一个实体类,里面封装了一些下载所需要的信息,每个loadinfo对应1个下载器
		new Thread(new Runnable() {

			@Override
			public void run() {
				LoadInfoModel loadInfo = mDownloader.getDownloaderInfors();
				if (null != loadInfo) {
					FileStateModel fileState = new FileStateModel(
							fileName, downPath, loadInfo.getComplete(), loadInfo.getFileSize(), 1);
					fileState.setTitle(title);
					fileState.setPicture(picUrl);
					DownLoadDao.insertFileState(fileState);// 在localdown_info表中插入一条下载数据
					mCompleteSizes.put(downPath, loadInfo.getComplete());
					mFileSizes.put(downPath, fileState.getFileSize());
					mDownloader.download();
				}
			}
		}).start();
	}

	/**
	 * 重新下载方法，如果下载暂停了，调用此方法重新下载
	 * **/
	public void reStartDownload(String fileName, final String downPath) {
		String savePath = ServerAPIConstant.getDownloadPath();// 保存地址
		mDownloader = mDownloadersMap.get(downPath);
		if (mDownloader == null) {
			mDownloader = new Downloader(downPath, savePath, fileName, 1, this, mHandler);
			mDownloadersMap.put(downPath, mDownloader);// 创建完一个新的下载器,必须把它加入到下载器集合里去
		}
		if (mDownloader.isdownloading()) {
			return;
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				LoadInfoModel loadInfo = mDownloader.getDownloaderInfors();
				if (null != loadInfo) {
					mCompleteSizes.put(downPath, loadInfo.getComplete());
					mDownloader.download();
				}
			}
		}).start();
	}

	/**
	 * 改变文件状态的方法，如果文件正在下载，就会暂停，如果暂停则开始下载
	 * **/
	public void switchState(final String fileName, final String downPath) {
		Downloader downloader = mDownloadersMap.get(downPath);
		if (downloader != null) {
			if (downloader.isdownloading()) {
				downloader.pause();// 正在下载则暂停
				Toast.makeText(getApplicationContext(), "暂停下载！", Toast.LENGTH_SHORT).show();
			} else if (downloader.isPause()) {
				Toast.makeText(getApplicationContext(), "开始下载！", Toast.LENGTH_SHORT).show();
				this.reStartDownload(fileName, downPath);
			}
		} else {
			// 如果downloaders中没有url的数据,肯定是处于暂停状态，直接开始下载
			reStartDownload(fileName, downPath);
		}
	}

	public void deleteData(String url) {
		if (mDownloadersMap.get(url) != null) {
			mDownloadersMap.get(url).delete(url);
			mDownloadersMap.get(url).reset();
			mDownloadersMap.remove(url);
		}
		if (mCompleteSizes.get(url) != null) {
			mCompleteSizes.remove(url);
		}
		if (mFileSizes.get(url) != null) {
			mFileSizes.remove(url);
		}
	}

	public class PunchBinder extends Binder {
		public DownloadService getService() {
			return DownloadService.this;
		}
	}
}
