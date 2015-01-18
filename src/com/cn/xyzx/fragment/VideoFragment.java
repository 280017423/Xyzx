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
import com.cn.xyzx.bean.FileStateModel;
import com.cn.xyzx.bean.VideoModel;
import com.cn.xyzx.db.DbDao;
import com.cn.xyzx.db.DownLoadDao;
import com.cn.xyzx.req.VideoReq;
import com.cn.xyzx.util.ActionResult;
import com.qianjiang.framework.app.QJActivityBase.ActionListener;
import com.qianjiang.framework.util.NetUtil;
import com.qianjiang.framework.util.StringUtil;
import com.qianjiang.framework.util.UIUtil;

public class VideoFragment extends FragmentBase implements OnItemClickListener, OnClickListener {

	private List<VideoModel> mLeaderList;
	private GridView mGvHonor;
	private VideoAdapter mAdapter;
	private Button mBtnLocalDownload;

	public static final VideoFragment newInstance() {
		VideoFragment fragment = new VideoFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		mLeaderList = new ArrayList<VideoModel>();
		mAdapter = new VideoAdapter(getActivity(), mLeaderList, mImageLoader, this);
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
		getVideoList();
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
			refreashVideoList();
			mAdapter.notifyDataSetChanged();
		}
		new AsyncLogin().execute();
	}

	@Override
	public void onResume() {
		refreashVideoList();
		mAdapter.notifyDataSetChanged();
		super.onResume();
	}

	class AsyncLogin extends AsyncTask<Void, Void, ActionResult> {

		@Override
		protected void onPostExecute(ActionResult result) {
			if (result != null && ActionResult.RESULT_CODE_SUCCESS.equals(result.ResultCode)) {

			} else {
				showErrorMsg(result);
			}
			mLeaderList.clear();
			mLeaderList.addAll(DbDao.getModels(VideoModel.class));
			refreashVideoList();
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
		VideoModel model = mAdapter.getItem(position);
		if (null != model && !StringUtil.isNullOrEmpty(model.getId())) {
			Intent intent = new Intent(getActivity(), VideoPlayerActivity.class);
			intent.putExtra(Video.class.getName(), model);
			startActivity(intent);
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
			case R.id.btn_down_icon:
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
						VideoModel model = (VideoModel) v.getTag();
						if (null == model || StringUtil.isNullOrEmpty(model.getFileName())
								|| StringUtil.isNullOrEmpty(model.getVideoUrl())) {
							return;
						}
						int status = ((InfoCenterActivity) getActivity()).startDownload(model.getFileName(),
								model.getTitle(), model.getVideoUrl(), model.getPicture());
						if (-1 == status) {
							toast(getString(R.string.video_has_download));
						} else if (-2 == status) {
							toast(getString(R.string.video_has_download));
						} else {
							toast(getString(R.string.add_video_success));
						}
					}

				});
				break;
			default:
				break;
		}
	}

	private void refreashVideoList() {
		List<FileStateModel> fileStateModels = DownLoadDao.getFileState();
		if (null != mLeaderList && !mLeaderList.isEmpty() && null != fileStateModels) {
			for (int i = 0; i < mLeaderList.size(); i++) {
				VideoModel videoModel = mLeaderList.get(i);
				videoModel.setHasDownload(0); // 初始化未下载
				for (int j = 0; j < fileStateModels.size(); j++) {
					FileStateModel fileStateModel = fileStateModels.get(j);
					String url = fileStateModel.getUrl();
					if (!StringUtil.isNullOrEmpty(url) && url.equals(videoModel.getVideoUrl())) {
						videoModel.setHasDownload(1);
					}
				}
			}
			mAdapter.notifyDataSetChanged();
		}
	}
}
