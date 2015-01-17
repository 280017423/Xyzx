package com.cn.xyzx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.xyzx.R;
import com.cn.xyzx.util.ActionResult;
import com.cn.xyzx.util.UiUtil;
import com.qianjiang.framework.imageloader.core.ImageLoader;
import com.qianjiang.framework.util.StringUtil;
import com.qianjiang.framework.widget.LoadingUpView;

/**
 * Fragment基类
 * 
 * @author zou.sq
 */
public class FragmentActivityBase extends FragmentActivity {

	protected ImageLoader mImageLoader = ImageLoader.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		UiUtil.hiddenSystemUi(this);
	}

	/**
	 * toast显示错误消息
	 * 
	 * @param result
	 */
	protected void showErrorMsg(ActionResult result) {
		showErrorToast(result, getResources().getString(R.string.network_is_not_available));
	}

	/**
	 * toast显示错误消息
	 * 
	 * @param result
	 * @param netErrorMsg
	 *            网络异常时显示消息
	 */
	protected void showErrorMsg(ActionResult result, String netErrorMsg) {
		showErrorToast(result, netErrorMsg);
	}

	/**
	 * 显示错误消息
	 * 
	 * @param result
	 * @param netErrorMsg
	 *            网络异常时显示消息
	 */
	private void showErrorToast(ActionResult result, String netErrorMsg) {
		if (result == null) {
			return;
		}
		if (ActionResult.RESULT_CODE_NET_ERROR.equals(result.ResultCode)) {
			toast(netErrorMsg);
		} else if (result.ResultObject != null) {
			// 增加RESULT_CODE_ERROR值时也弹出网络异常
			if (ActionResult.RESULT_CODE_NET_ERROR.equals(result.ResultObject.toString())) {
				toast(netErrorMsg);
			} else {
				toast(result.ResultObject.toString());
			}
		}

	}

	protected static boolean showLoadingUpView(LoadingUpView loadingUpView) {
		if (loadingUpView != null && !loadingUpView.isShowing()) {
			loadingUpView.showPopup();
			return true;
		}
		return false;
	}

	protected static boolean dismissLoadingUpView(LoadingUpView loadingUpView) {
		if (loadingUpView != null && loadingUpView.isShowing()) {
			loadingUpView.dismiss();
			return true;
		}
		return false;
	}

	void handleActivityResult(int requestCode, int resultCode, Intent data) {
	}

	/**
	 * 默认的toast方法，该方法封装下面的两点特性：<br>
	 * 1、只有当前activity所属应用处于顶层时，才会弹出toast；<br>
	 * 2、默认弹出时间为 Toast.LENGTH_SHORT;
	 * 
	 * @param msg
	 *            弹出的信息内容
	 */
	public void toast(final String msg) {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (!StringUtil.isNullOrEmpty(msg)) {
					Toast toast = Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT);
					TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
					// 用来防止某些系统自定义了消息框
					if (tv != null) {
						tv.setGravity(Gravity.CENTER);
					}
					toast.show();
				}
			}
		});
	}

	/**
	 * 弹出toast
	 * 
	 * @param e
	 *            出错的exception
	 */
	public void toast(final Throwable e) {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(getBaseContext(),
						getBaseContext().getResources().getString(R.string.msg_operate_fail_try_again),
						Toast.LENGTH_SHORT).show();
			}
		});
	}
}
