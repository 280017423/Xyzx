package com.cn.xyzx.util;

import android.app.Activity;
import android.view.View;

/**
 * 隐藏系统状态栏
 * 
 * @author zou.sq
 */
public class UiUtil {
	public static void hiddenSystemUi(Activity paramActivity) {
		paramActivity.getWindow().getDecorView().setSystemUiVisibility(View.GONE);
	}
}
