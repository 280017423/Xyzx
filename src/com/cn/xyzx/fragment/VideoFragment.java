package com.cn.xyzx.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Video;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;

import com.cn.xyzx.R;
import com.cn.xyzx.activity.InfoCenterActivity;
import com.cn.xyzx.activity.LocalDownActivity;
import com.cn.xyzx.activity.VideoPlayerActivity;
import com.cn.xyzx.adapter.VideoAdapter;
import com.cn.xyzx.bean.VideoModel;
import com.cn.xyzx.db.DbDao;
import com.cn.xyzx.download.DownloadManager;
import com.cn.xyzx.download.DownloadService;
import com.cn.xyzx.req.VideoReq;
import com.cn.xyzx.util.ActionResult;
import com.cn.xyzx.util.ServerAPIConstant;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;
import com.qianjiang.framework.app.QJActivityBase.ActionListener;
import com.qianjiang.framework.util.NetUtil;
import com.qianjiang.framework.util.StringUtil;
import com.qianjiang.framework.util.UIUtil;

public class VideoFragment extends FragmentBase implements OnItemClickListener, OnClickListener {

	private List<VideoModel> mLeaderList;
	private GridView mGvHonor;
	private VideoAdapter mAdapter;
	private Button mBtnLocalDownload;
	private DownloadManager mDownloadManager;

	public static final VideoFragment newInstance() {
		VideoFragment fragment = new VideoFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		mLeaderList = new ArrayList<VideoModel>();
		mAdapter = new VideoAdapter(getActivity(), mLeaderList, mImageLoader, this);
		mDownloadManager = DownloadService.getDownloadManager(getActivity().getApplicationContext());
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mView = inflater.inflate(R.layout.fragment_info, container, false);
		mGvHonor = (GridView) mView.findViewById(R.id.info_gridView);
		mBtnLocalDownload = (Button) mView.findViewById(R.id.btn_local_download);
		mBtnLocalDownload.setVisibility(View.VISIBLE);
		mGvHonor.setNumColumns(3);
		mGvHonor.setAdapter(mAdapter);
		mGvHonor.setOnItemClickListener(this);
		mBtnLocalDownload.setOnClickListener(this);
		return mView;
	}

	private void getVideoList() {
		List<VideoModel> list = DbDao.getModels(VideoModel.class);
		if (null == list || list.isEmpty()) {
			if (isAdded()) {
				((InfoCenterActivity) getActivity()).showLoading();
			}
		} else {
			mLeaderList.clear();
			mLeaderList.addAll(list);
			mAdapter.notifyDataSetChanged();
		}
		new AsyncLogin().execute();
	}

	@Override
	public void onResume() {
		getVideoList();
		mAdapter.notifyDataSetChanged();
		super.onResume();
	}

	class AsyncLogin extends AsyncTask<Void, Void, ActionResult> {

		@Override
		protected void onPostExecute(ActionResult result) {
			if (null == getActivity() || getActivity().isFinishing()) {
				return;
			}
			if (result != null && ActionResult.RESULT_CODE_SUCCESS.equals(result.ResultCode)) {

			} else {
				showErrorMsg(result);
			}
			mLeaderList.clear();
			mLeaderList.addAll(DbDao.getModels(VideoModel.class));
			mAdapter.notifyDataSetChanged();
			if (isAdded()) {
				((InfoCenterActivity) getActivity()).dismissLoading();
			}
		}

		@Override
		protected ActionResult doInBackground(Void... params) {
			return VideoReq.getVideoInfo();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		final VideoModel model = mAdapter.getItem(position);
		if (null != model && !StringUtil.isNullOrEmpty(model.getVideoUrl())) {
			if (mDownloadManager.hasDownLoaded(model.getVideoUrl())) {
				Intent intent = new Intent(getActivity(), VideoPlayerActivity.class);
				intent.putExtra(Video.class.getName(), model);
				startActivity(intent);
			} else {
				UIUtil.limitReClick(VideoFragment.class.getName(), new ActionListener() {

					@Override
					public void doAction() {
						if (!isAdded()) {
							return;
						}
						if (!Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
							toast(getActivity().getString(R.string.no_sd_card));
							return;
						}
						if (!NetUtil.isNetworkAvailable()) {
							toast(getActivity().getString(R.string.network_is_not_available));
							return;
						}
						if (null == model || StringUtil.isNullOrEmpty(model.getFileName())
								|| StringUtil.isNullOrEmpty(model.getVideoUrl())) {
							return;
						}
						startDownload(model);
						Intent intent1 = new Intent(getActivity(), LocalDownActivity.class);
						startActivity(intent1);
					}
				});
			}
		}
	}

	@Override
	public void onClick(final View v) {

		switch (v.getId()) {
			case R.id.btn_local_download:
				if (isAdded()) {
					startActivity(new Intent(getActivity(), LocalDownActivity.class));
				}
				break;
			default:
				break;
		}
	}

	private void startDownload(VideoModel videoModel) {
		try {
			int status = mDownloadManager.addNewDownload(videoModel.getVideoUrl(), videoModel.getTitle(),
					ServerAPIConstant.getDownloadPath() + videoModel.getFileName(), true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
					false, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
					null);
			if (0 == status) {
				toast(getString(R.string.video_has_download));
			}
		} catch (DbException e) {
			LogUtils.e(e.getMessage(), e);
		}
	}
}
