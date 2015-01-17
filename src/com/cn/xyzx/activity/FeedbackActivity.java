package com.cn.xyzx.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.cn.xyzx.R;
import com.cn.xyzx.req.FeedbackReq;
import com.cn.xyzx.util.ActionResult;
import com.qianjiang.framework.util.NetUtil;
import com.qianjiang.framework.util.StringUtil;
import com.qianjiang.framework.widget.LoadingUpView;

public class FeedbackActivity extends ActivityBase implements OnClickListener {

	private boolean mIsGettingData;
	private LoadingUpView mLoadingUpView;
	private EditText mEdtContent;
	private EditText mEdtTel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		isHiddenSystemUi = false;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		initView();
	}

	private void initView() {
		mLoadingUpView = new LoadingUpView(this);
		mEdtContent = (EditText) findViewById(R.id.edt_feedback_content);
		mEdtTel = (EditText) findViewById(R.id.edt_feedback_tel);
	}

	private void submitContent() {
		if (mIsGettingData) {
			return;
		}
		final String content = mEdtContent.getText().toString().trim();
		final String tel = mEdtTel.getText().toString().trim();
		if (StringUtil.isNullOrEmpty(tel)) {
			toast(getString(R.string.tel_empty));
			return;
		}
		if (StringUtil.isNullOrEmpty(content)) {
			toast(getString(R.string.feedback_empty));
			return;
		}
		if (!NetUtil.isNetworkAvailable()) {
			toast(getString(R.string.network_is_not_available));
			return;
		}
		mIsGettingData = true;
		showLoadingUpWindow(mLoadingUpView);
		new AsyncLogin(tel, content).execute();
	}

	class AsyncLogin extends AsyncTask<Void, Void, ActionResult> {

		String content = "";
		String tel = "";

		public AsyncLogin(String tel, String content) {
			this.content = content;
			this.tel = tel;
		}

		@Override
		protected void onPostExecute(ActionResult result) {
			dismissLoadingUpView(mLoadingUpView);
			if (result != null && ActionResult.RESULT_CODE_SUCCESS.equals(result.ResultCode)) {
				toast(getString(R.string.thanks_for_feedback));
				finish();
			} else {
				showErrorMsg(result);
			}
		}

		@Override
		protected ActionResult doInBackground(Void... params) {
			return FeedbackReq.feedback(tel, content);
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.ibtn_back:
				finish();
				break;
			case R.id.btn_feedback:
				submitContent();
				break;
			default:
				break;
		}
	}
}
