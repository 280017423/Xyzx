package com.cn.xyzx.req;

import java.util.List;

import com.cn.xyzx.bean.DocumentModel;
import com.cn.xyzx.db.DbDao;
import com.cn.xyzx.util.ActionResult;
import com.cn.xyzx.util.ServerAPIConstant;
import com.pdw.gson.reflect.TypeToken;
import com.qianjiang.framework.app.JsonResult;
import com.qianjiang.framework.util.EvtLog;
import com.qianjiang.framework.util.HttpClientUtil;

/**
 * Description the class文档数据请求类
 * 
 * @version 1.0
 * @author zou.sq
 */
public class DocumentReq {

	private static final String TAG = "DocumentReq";

	private DocumentReq() {
	}

	public static ActionResult getFileList() {
		ActionResult result = new ActionResult();
		try {
			String url = ServerAPIConstant.getUrl(ServerAPIConstant.API_GEI_FILE_LIST);
			JsonResult jsonResult = HttpClientUtil.get(url, null);
			if (jsonResult != null) {
				if (!jsonResult.isOK()) {
					result.ResultObject = jsonResult.Msg;
				} else {
					List<DocumentModel> cateModels = jsonResult.getData(new TypeToken<List<DocumentModel>>() {
					}.getType());
					DbDao.deleteTableFromDb(DocumentModel.class);
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
