package com.cn.xyzx.req;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

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
public class FeedbackReq {

	private static final String TAG = "FeedbackReq";

	private FeedbackReq() {
	}

	public static ActionResult feedback(String tel, String content) {
		ActionResult result = new ActionResult();
		try {
			String url = ServerAPIConstant.getUrl(ServerAPIConstant.API_FEED_BACK);
			List<NameValuePair> getParams = new ArrayList<NameValuePair>();
			getParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_TEL, tel));
			getParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_CONTENT, content));
			JsonResult jsonResult = HttpClientUtil.get(url, getParams);
			if (jsonResult != null) {
				result.ResultObject = jsonResult.Msg;
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
