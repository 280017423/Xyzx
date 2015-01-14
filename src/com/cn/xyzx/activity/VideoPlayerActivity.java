package com.cn.xyzx.activity;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Video;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.cn.xyzx.R;
import com.cn.xyzx.bean.VideoModel;
import com.qianjiang.framework.util.EvtLog;
import com.qianjiang.framework.util.StringUtil;
import com.qianjiang.framework.widget.LoadingUpView;

public class VideoPlayerActivity extends ActivityBase implements OnPreparedListener, OnErrorListener {

	private static final String TOAST_ERROR_URL = "播放出错了，请检查网络或者视频是否存在";
	private static final String DIALOG_TITILE = "奋力加载中，请稍后...";

	private MediaController mc;
	private LinearLayout llMain;
	private LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

	private VideoView videoView;
	private VideoModel mVideoModel;
	private LoadingUpView mLoadingUpView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_simple_videoplayer);
		initVariables();
		initView();
	}

	private void initVariables() {
		mLoadingUpView = new LoadingUpView(this);
		mVideoModel = (VideoModel) getIntent().getSerializableExtra(Video.class.getName());
		if (null == mVideoModel || StringUtil.isNullOrEmpty(mVideoModel.getVideoUrl())) {
			Toast.makeText(getApplicationContext(), TOAST_ERROR_URL, Toast.LENGTH_LONG).show();
			finish();
		}
		mLoadingUpView.showPopup(DIALOG_TITILE);
		EvtLog.d("aaa", mVideoModel.getVideoUrl());
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		mLoadingUpView.dismiss();
		videoView.start();
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		Toast.makeText(getApplicationContext(), TOAST_ERROR_URL, Toast.LENGTH_LONG).show();
		mLoadingUpView.dismiss();
		finish();

		return true;
	}

	private void initView() {

		videoView = new VideoView(this);
		// videoView.setVideoURI(Uri.parse(NetworkConstant.savePath +
		// mVideoModel.getFineName()));
		videoView.setVideoURI(Uri.parse(mVideoModel.getVideoUrl()));
		videoView.requestFocus();
		videoView.setOnPreparedListener(this);
		videoView.setOnErrorListener(this);

		mc = new MediaController(this);
		mc.setAnchorView(videoView);
		mc.setKeepScreenOn(true);

		videoView.setMediaController(mc);

		llMain = new LinearLayout(this);
		llMain.setGravity(Gravity.CENTER_VERTICAL);
		llMain.setOrientation(LinearLayout.VERTICAL);
		llMain.setLayoutParams(params);

		llMain.addView(videoView, params);
		setContentView(llMain);
	}

}
