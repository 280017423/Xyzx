package com.cn.xyzx.req;

import java.util.List;

import com.cn.xyzx.bean.ProductCateModel;
import com.cn.xyzx.bean.ProductModel;
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
public class ProductReq {

	private static final String TAG = "ProductReq";

	private ProductReq() {
	}

	public static ActionResult getProductInfo() {
		ActionResult result = new ActionResult();
		try {
			String url = ServerAPIConstant.getUrl(ServerAPIConstant.API_GEI_PRODUCT);
			JsonResult jsonResult = HttpClientUtil.get(url, null);
			if (jsonResult != null) {
				if (!jsonResult.isOK()) {
					result.ResultObject = jsonResult.Msg;
				} else {
					List<ProductCateModel> cateModels = jsonResult.getData(ServerAPIConstant.KEY_CATE,
							new TypeToken<List<ProductCateModel>>() {
							}.getType());
					List<ProductModel> productModels = jsonResult.getData(ServerAPIConstant.KEY_LIST,
							new TypeToken<List<ProductModel>>() {
							}.getType());
					DbDao.deleteTableFromDb(ProductCateModel.class);
					DbDao.deleteTableFromDb(ProductModel.class);
					DbDao.saveModels(cateModels);
					DbDao.saveModels(productModels);
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
