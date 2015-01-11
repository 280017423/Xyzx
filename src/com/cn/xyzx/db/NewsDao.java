package com.cn.xyzx.db;

import java.util.ArrayList;
import java.util.List;

import com.cn.xyzx.bean.NewsCateModel;
import com.cn.xyzx.bean.NewsModel;
import com.cn.xyzx.util.DBUtil;
import com.qianjiang.framework.orm.DataAccessException;
import com.qianjiang.framework.orm.DataManager;

/**
 * 
 * Description the class 打卡数据处理类
 * 
 * @version 1.0
 * @author zou.sq
 * 
 */
public final class NewsDao {

	private static final String PRIMARY_KEY_WHERE = " = ?";

	private NewsDao() {
	}

	/**
	 * 
	 * @Description 根据model类型获取所有的本地数据
	 * @param type
	 *            需要获取的数据类型
	 * @param <T>
	 *            Basemodel的子类
	 * @return List<T> 指定数据类型集合
	 * 
	 */
	public static List<NewsModel> getNewsModels(String cateId) {
		List<NewsModel> results = new ArrayList<NewsModel>();
		DataManager dataManager = DBUtil.getDataManager();
		dataManager.open();
		try {
			results = dataManager.getList(NewsModel.class, NewsCateModel.CATE_ID + PRIMARY_KEY_WHERE,
					new String[] { cateId });
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		dataManager.close();
		return results;
	}
}
