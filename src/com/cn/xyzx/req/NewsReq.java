package com.cn.xyzx.req;

import java.util.List;

import com.cn.xyzx.bean.NewsCateModel;
import com.cn.xyzx.bean.NewsModel;
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
public class NewsReq {

	private static final String TAG = "NewsReq";

	private NewsReq() {
	}

	public static ActionResult getNewsInfo() {
		ActionResult result = new ActionResult();
		try {
			String url = ServerAPIConstant.getUrl(ServerAPIConstant.API_GEI_NEWS);
			JsonResult jsonResult = HttpClientUtil.get(url, null);
			if (jsonResult != null) {
				if (!jsonResult.isOK()) {
					result.ResultObject = jsonResult.Msg;
				} else {
					List<NewsCateModel> cateModels = jsonResult.getData(ServerAPIConstant.KEY_CATE,
							new TypeToken<List<NewsCateModel>>() {
							}.getType());
					List<NewsModel> newsModels = jsonResult.getData(ServerAPIConstant.KEY_LIST,
							new TypeToken<List<NewsModel>>() {
							}.getType());
					DbDao.deleteTableFromDb(NewsCateModel.class);
					DbDao.deleteTableFromDb(NewsModel.class);
					DbDao.saveModels(cateModels);
					DbDao.saveModels(newsModels);
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
