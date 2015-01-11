package com.cn.xyzx.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.xyzx.R;
import com.cn.xyzx.util.ActionResult;
import com.qianjiang.framework.imageloader.core.ImageLoader;
import com.qianjiang.framework.util.StringUtil;
import com.qianjiang.framework.widget.LoadingUpView;

/**
 * Fragment基类
 * 
 * @author zou.sq
 */
public class FragmentBase extends Fragment {

	protected ImageLoader mImageLoader = ImageLoader.getInstance();

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	/**
	 * toast显示错误消息
	 * 
	 * @param result
	 */
	protected void showErrorMsg(ActionResult result) {
		if (isAdded()) {
			showErrorToast(result, getResources().getString(R.string.network_is_not_available));
		}
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

	/**
	 * 默认的toast方法，该方法封装下面的两点特性：<br>
	 * 1、只有当前activity所属应用处于顶层时，才会弹出toast；<br>
	 * 2、默认弹出时间为 Toast.LENGTH_SHORT;
	 * 
	 * @param msg
	 *            弹出的信息内容
	 */
	public void toast(final String msg) {
		if (!isAdded()) {
			return;
		}
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (!StringUtil.isNullOrEmpty(msg)) {
					Toast toast = Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT);
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

	/**
	 * 
	 * @Method: onPositiveBtnClick
	 * @Description: 确定按钮回调
	 * @param id
	 *            当前对话框对象的ID
	 * @param dialog
	 *            DialogInterface 对象
	 * @param which
	 *            dialog ID
	 */
	public void onPositiveBtnClick(int id, DialogInterface dialog, int which) {
	}

	/**
	 * 
	 * @Method: onPositiveBtnClick
	 * @Description: 取消按钮回调
	 * @param id
	 *            当前对话框对象的ID
	 * @param dialog
	 *            DialogInterface 对象
	 * @param which
	 *            dialog ID
	 */
	public void onNegativeBtnClick(int id, DialogInterface dialog, int which) {
	}

}
