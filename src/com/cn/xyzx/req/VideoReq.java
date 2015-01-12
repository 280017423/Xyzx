package com.cn.xyzx.req;

import java.util.List;

import com.cn.xyzx.bean.VideoModel;
import com.cn.xyzx.db.DbDao;
import com.cn.xyzx.util.ActionResult;
import com.cn.xyzx.util.ServerAPIConstant;
import com.pdw.gson.reflect.TypeToken;
import com.qianjiang.framework.app.JsonResult;
import com.qianjiang.framework.util.EvtLog;
import com.qianjiang.framework.util.HttpClientUtil;

/**
 * Description the class 产品数据请求类
 * 
 * @version 1.0
 * @author zou.sq
 */
public class VideoReq {

	private static final String TAG = "NewsReq";

	private VideoReq() {
	}

	public static ActionResult getVideoInfo() {
		ActionResult result = new ActionResult();
		try {
			String url = ServerAPIConstant.getUrl(ServerAPIConstant.API_GEI_VIDEO_LIST);
			JsonResult jsonResult = HttpClientUtil.get(url, null);
			if (jsonResult != null) {
				if (!jsonResult.isOK()) {
					result.ResultObject = jsonResult.Msg;
				} else {
					List<VideoModel> cateModels = jsonResult.getData(new TypeToken<List<VideoModel>>() {
					}.getType());
					DbDao.deleteTableFromDb(VideoModel.class);
					DbDao.saveModels(cateModels);
				}
				result.ResultCode = jsonResult.Code;
			} else {
				result.ResultCode = ActionResult.RESULT_CODE_NET_ERROR;
			}
		} catch (Exception e) {
			result.ResultCode = ActionResult.RESULT_CODE_NET_ERROR;
			EvtLog.w(TAG, e);
		}
		return result;
	}
}
