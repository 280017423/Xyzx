/**
 * 
 */
package com.cn.xyzx.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.cn.xyzx.R;
import com.cn.xyzx.activity.InfoCenterActivity;
import com.cn.xyzx.activity.WebViewActivity;
import com.cn.xyzx.adapter.HonorAdapter;
import com.cn.xyzx.bean.HonorModel;
import com.cn.xyzx.bean.ProductCateModel;
import com.cn.xyzx.db.DbDao;
import com.cn.xyzx.req.EnterpriseReq;
import com.cn.xyzx.util.ActionResult;
import com.cn.xyzx.util.ServerAPIConstant;
import com.qianjiang.framework.util.StringUtil;
import com.qianjiang.framework.widget.LoadingUpView;

public class HonorFragment extends FragmentBase implements OnItemClickListener {
	private List<HonorModel> mLeaderList;
	private GridView mGvHonor;
	private HonorAdapter mAdapter;
	private String mCateId;

	public static final HonorFragment newInstance(String cateId) {
		HonorFragment fragment = new HonorFragment();
		Bundle bundle = new Bundle();
		bundle.putString(ProductCateModel.CATE_ID, cateId);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		mCateId = getArguments().getString(ProductCateModel.CATE_ID);
		mLeaderList = new ArrayList<HonorModel>();
		mAdapter = new HonorAdapter(getActivity(), mLeaderList, mImageLoader);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mView = inflater.inflate(R.layout.info_fragment, container, false);
		mGvHonor = (GridView) mView.findViewById(R.id.info_gridView);
		mGvHonor.setNumColumns(3);
		mGvHonor.setAdapter(mAdapter);
		mGvHonor.setOnItemClickListener(this);
		getLeaderList();
		return mView;
	}

	private void getLeaderList() {
		if (isAdded()) {
			((InfoCenterActivity) getActivity()).showLoading();
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
			mLeaderList.clear();
			mLeaderList.addAll(DbDao.getModels(HonorModel.class));
			mAdapter.notifyDataSetChanged();
			if (isAdded()) {
				((InfoCenterActivity) getActivity()).dismissLoading();
			}
		}

		@Override
		protected ActionResult doInBackground(Void... params) {
			return EnterpriseReq.getHonorInfo(mCateId);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		HonorModel model = mAdapter.getItem(position);
		if (null != model && !StringUtil.isNullOrEmpty(model.getId())) {
			Intent intent = new Intent(getActivity(), WebViewActivity.class);
			intent.setData(Uri.parse(ServerAPIConstant.getApiRootUrl() + ServerAPIConstant.API_GEI_LEADER_DETAIL
					+ model.getId()));
			startActivity(intent);
		}
	}
}
