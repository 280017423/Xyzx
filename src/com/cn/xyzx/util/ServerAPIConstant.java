package com.cn.xyzx.util;

import java.io.File;

import com.qianjiang.framework.app.QJApplicationBase;
import com.qianjiang.framework.util.AppUtil;
import com.qianjiang.framework.util.PackageUtil;

/**
 * 定义与服务器端的接口交互需要用到的常量
 * 
 * @author zou.sq
 */
public class ServerAPIConstant {
	public static final String ACTION_UPDATE_DOWNLOAD_PROGRESS = "ACTION_UPDATE_DOWNLOAD_PROGRESS";

	public static final String API_GEI_PRODUCT = "api/product_list.php";
	public static final String API_GEI_PRODUCT_DETAIL = "api/product_msg.php?productId=";
	public static final String API_GEI_NEWS = "api/new_list.php";
	public static final String API_GEI_NEWS_DETAIL = "api/new_msg.php?newsId=";
	public static final String API_GEI_LEADER_DETAIL = "api/leadStyle_msg.php?id=";
	public static final String API_GEI_ENTERPRISE_CULTURE = "api/singlePage.php?id=1659";
	public static final String API_GEI_ENTERPRISE_INTRODUCE = "api/singlePage.php?id=16";
	public static final String API_GEI_SINGLE_PAGE = "api/singlePage.php?id=";
	public static final String API_GEI_LEADER_LIST = "api/leadStyle_list.php";
	public static final String API_GEI_VIDEO_LIST = "api/video_list.php";
	public static final String API_GEI_FILE_LIST = "api/file_list.php";
	public static final String API_FEED_BACK = "api/feedback.php";

	public static final String KEY_CATE = "cate";
	public static final String KEY_TEL = "tel";
	public static final String KEY_CONTENT = "content";
	public static final String KEY_LIST = "list";
	public static final String KEY_CATE_ID = "cate_id";

	public static final String API_ROOT_URL = "api_root_url";

	public static String getApiRootUrl() {
		return AppUtil.getMetaDataByKey(QJApplicationBase.CONTEXT, API_ROOT_URL);
	}

	public static String getAppSign() {
		return "xyzx";
	}

	/**
	 * 获取接口地址
	 * 
	 * @param interfaceName
	 *            接口名字
	 * @return String
	 * @throws
	 */
	public static String getUrl(String interfaceName) {
		return getApiRootUrl() + interfaceName;
	}

	public static String getDownloadPath() {
		File videoDir = null;
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			videoDir = new File(
					android.os.Environment.getExternalStorageDirectory(),
					PackageUtil.getConfigString("video_download_dir"));
		}
		if (!videoDir.exists()) {
			videoDir.mkdirs();
		}
		return videoDir.getAbsolutePath() + File.separator;
	}

}
