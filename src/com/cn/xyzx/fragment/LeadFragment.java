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
import android.widget.ListView;

import com.cn.xyzx.R;
import com.cn.xyzx.activity.InfoCenterActivity;
import com.cn.xyzx.activity.WebViewActivity;
import com.cn.xyzx.adapter.LeadListAdapter;
import com.cn.xyzx.bean.LeaderModel;
import com.cn.xyzx.bean.ProductCateModel;
import com.cn.xyzx.db.DbDao;
import com.cn.xyzx.req.EnterpriseReq;
import com.cn.xyzx.util.ActionResult;
import com.cn.xyzx.util.ServerAPIConstant;
import com.qianjiang.framework.util.StringUtil;

public class LeadFragment extends FragmentBase implements OnItemClickListener {
	private List<LeaderModel> mLeaderList;
	private ListView mLvLeader;
	private LeadListAdapter mAdapter;
	private String mCateId;

	public static final LeadFragment newInstance(String cateId) {
		LeadFragment fragment = new LeadFragment();
		Bundle bundle = new Bundle();
		bundle.putString(ProductCateModel.CATE_ID, cateId);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		mCateId = getArguments().getString(ProductCateModel.CATE_ID);
		mLeaderList = new ArrayList<LeaderModel>();
		mAdapter = new LeadListAdapter(getActivity(), mLeaderList, mImageLoader);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mView = inflater.inflate(R.layout.fragment_lead, container, false);
		mLvLeader = (ListView) mView.findViewById(R.id.lead_listview);
		mLvLeader.setAdapter(mAdapter);
		mLvLeader.setOnItemClickListener(this);
		getLeaderList();
		return mView;
	}

	private void getLeaderList() {
		List<LeaderModel> list = DbDao.getModels(LeaderModel.class);
		if (null == mLeaderList || mLeaderList.isEmpty()) {
			if (isAdded()) {
				((InfoCenterActivity) getActivity()).showLoading();
			}
		} else {
			mLeaderList.addAll(list);
			mAdapter.notifyDataSetChanged();
		}
		new AsyncLogin().execute();
	}

	class AsyncLogin extends AsyncTask<Void, Void, ActionResult> {

		@Override
		protected void onPostExecute(ActionResult result) {
			if (getActivity().isFinishing()) {
				return;
			}
			if (result != null && ActionResult.RESULT_CODE_SUCCESS.equals(result.ResultCode)) {
			} else {
				showErrorMsg(result);
			}
			mLeaderList.clear();
			mLeaderList.addAll(DbDao.getModels(LeaderModel.class));
			mAdapter.notifyDataSetChanged();
			if (isAdded()) {
				((InfoCenterActivity) getActivity()).dismissLoading();
			}
		}

		@Override
		protected ActionResult doInBackground(Void... params) {
			return EnterpriseReq.getLeaderInfo(mCateId);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		LeaderModel model = mAdapter.getItem(position);
		if (null != model && !StringUtil.isNullOrEmpty(model.getId())) {
			Intent intent = new Intent(getActivity(), WebViewActivity.class);
			intent.setData(Uri.parse(ServerAPIConstant.getApiRootUrl() + ServerAPIConstant.API_GEI_LEADER_DETAIL
					+ model.getId()));
			startActivity(intent);
		}
	}
}
