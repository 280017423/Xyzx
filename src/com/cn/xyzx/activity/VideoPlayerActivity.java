package com.cn.xyzx.activity;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Video;
import android.widget.MediaController;

import com.cn.xyzx.R;
import com.cn.xyzx.bean.VideoModel;
import com.cn.xyzx.db.DownLoadDao;
import com.cn.xyzx.util.ServerAPIConstant;
import com.cn.xyzx.widget.FullScreenVideoView;
import com.qianjiang.framework.util.EvtLog;
import com.qianjiang.framework.util.StringUtil;
import com.qianjiang.framework.widget.LoadingUpView;

public class VideoPlayerActivity extends ActivityBase implements OnPreparedListener, OnErrorListener {

	private MediaController mMediaController;
	private FullScreenVideoView mVideoView;
	private VideoModel mVideoModel;
	private LoadingUpView mLoadingUpView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		isHiddenSystemUi = false;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_simple_videoplayer);
		initVariables();
		initView();
	}

	private void initVariables() {
		mLoadingUpView = new LoadingUpView(this);
		mVideoModel = (VideoModel) getIntent().getSerializableExtra(Video.class.getName());
		if (null == mVideoModel || StringUtil.isNullOrEmpty(mVideoModel.getVideoUrl())) {
			toast(getString(R.string.toast_error_url));
			finish();
		}
		mLoadingUpView.showPopup(getString(R.string.video_loading_title));
		EvtLog.d("aaa", mVideoModel.getVideoUrl());
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		mLoadingUpView.dismiss();
		mVideoView.start();
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		if (DownLoadDao.hasFile(mVideoModel.getFileName())) {
			toast(getString(R.string.local_video_error));
		} else {
			toast(getString(R.string.toast_error_url));
		}
		mLoadingUpView.dismiss();
		finish();
		return true;
	}

	private void initView() {
		mVideoView = (FullScreenVideoView) findViewById(R.id.fragmentvideoplayer_videoview);
		if (DownLoadDao.hasFile(mVideoModel.getFileName())) {
			mVideoView.setVideoURI(Uri.parse(ServerAPIConstant.getDownloadPath() + mVideoModel.getFileName()));
		} else {
			mVideoView.setVideoURI(Uri.parse(mVideoModel.getVideoUrl()));
		}
		mVideoView.requestFocus();
		mVideoView.setOnPreparedListener(this);
		mVideoView.setOnErrorListener(this);

		mMediaController = new MediaController(this);
		mMediaController.setAnchorView(mVideoView);
		mMediaController.setKeepScreenOn(true);

		mVideoView.setMediaController(mMediaController);
	}
}
