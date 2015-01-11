package com.cn.xyzx.req;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.cn.xyzx.bean.HonorModel;
import com.cn.xyzx.bean.LeaderModel;
import com.cn.xyzx.bean.ResponsibilityModel;
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
public class EnterpriseReq {

	private static final String TAG = "ProductReq";

	private EnterpriseReq() {
	}

	public static ActionResult getLeaderInfo(String cateId) {
		ActionResult result = new ActionResult();
		try {
			String url = ServerAPIConstant.getUrl(ServerAPIConstant.API_GEI_LEADER_LIST);
			List<NameValuePair> getParams = new ArrayList<NameValuePair>();
			getParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_CATE_ID, cateId));
			JsonResult jsonResult = HttpClientUtil.get(url, getParams);
			if (jsonResult != null) {
				if (!jsonResult.isOK()) {
					result.ResultObject = jsonResult.Msg;
				} else {
					List<LeaderModel> cateModels = jsonResult.getData(new TypeToken<List<LeaderModel>>() {
					}.getType());
					DbDao.deleteTableFromDb(LeaderModel.class);
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

	public static ActionResult getHonorInfo(String cateId) {
		ActionResult result = new ActionResult();
		try {
			String url = ServerAPIConstant.getUrl(ServerAPIConstant.API_GEI_LEADER_LIST);
			List<NameValuePair> getParams = new ArrayList<NameValuePair>();
			getParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_CATE_ID, cateId));
			JsonResult jsonResult = HttpClientUtil.get(url, getParams);
			if (jsonResult != null) {
				if (!jsonResult.isOK()) {
					result.ResultObject = jsonResult.Msg;
				} else {
					List<HonorModel> cateModels = jsonResult.getData(new TypeToken<List<HonorModel>>() {
					}.getType());
					DbDao.deleteTableFromDb(HonorModel.class);
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
	public static ActionResult getResponsibilityInfo(String cateId) {
		ActionResult result = new ActionResult();
		try {
			String url = ServerAPIConstant.getUrl(ServerAPIConstant.API_GEI_LEADER_LIST);
			List<NameValuePair> getParams = new ArrayList<NameValuePair>();
			getParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_CATE_ID, cateId));
			JsonResult jsonResult = HttpClientUtil.get(url, getParams);
			if (jsonResult != null) {
				if (!jsonResult.isOK()) {
					result.ResultObject = jsonResult.Msg;
				} else {
					List<ResponsibilityModel> cateModels = jsonResult.getData(new TypeToken<List<ResponsibilityModel>>() {
					}.getType());
					DbDao.deleteTableFromDb(ResponsibilityModel.class);
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
