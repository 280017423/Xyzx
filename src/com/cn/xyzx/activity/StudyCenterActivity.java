package com.cn.xyzx.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.cn.xyzx.R;
import com.cn.xyzx.adapter.DocumentAdapter;
import com.cn.xyzx.bean.DocumentModel;
import com.cn.xyzx.db.DbDao;
import com.cn.xyzx.req.DocumentReq;
import com.cn.xyzx.util.ActionResult;
import com.qianjiang.framework.widget.LoadingUpView;

public class StudyCenterActivity extends ActivityBase implements OnClickListener, OnItemClickListener {

	private List<DocumentModel> mDocumentList;
	private GridView mGvDocument;
	private DocumentAdapter mAdapter;
	private LoadingUpView mLoadingUpView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_study_center);
		initVariables();
		initView();
	}

	private void initVariables() {
		mDocumentList = new ArrayList<DocumentModel>();
		mAdapter = new DocumentAdapter(this, mDocumentList, mImageLoader);
		mLoadingUpView = new LoadingUpView(this);
	}

	private void initView() {
		mGvDocument = (GridView) findViewById(R.id.study_gridView);
		mGvDocument.setNumColumns(3);
		mGvDocument.setAdapter(mAdapter);
		mGvDocument.setOnItemClickListener(this);
		getFileList();
	}

	private void getFileList() {
		List<DocumentModel> list = DbDao.getModels(DocumentModel.class);
		if (null == list || list.isEmpty()) {
			showLoadingUpView(mLoadingUpView);
		} else {
			mDocumentList.addAll(list);
			mAdapter.notifyDataSetChanged();
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
			mDocumentList.clear();
			mDocumentList.addAll(DbDao.getModels(DocumentModel.class));
			mAdapter.notifyDataSetChanged();
			dismissLoadingUpView(mLoadingUpView);
		}

		@Override
		protected ActionResult doInBackground(Void... params) {
			return DocumentReq.getFileList();
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.bt_Bottom_back:
				finish();
				break;
			default:
				break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub

	}
}
