package com.cn.xyzx.util;

import android.content.Context;

import com.cn.xyzx.bean.DocumentModel;
import com.cn.xyzx.bean.HonorModel;
import com.cn.xyzx.bean.LeaderModel;
import com.cn.xyzx.bean.NewsCateModel;
import com.cn.xyzx.bean.NewsModel;
import com.cn.xyzx.bean.ProductCateModel;
import com.cn.xyzx.bean.ProductModel;
import com.cn.xyzx.bean.ResponsibilityModel;
import com.cn.xyzx.bean.VideoModel;
import com.qianjiang.framework.app.QJApplicationBase;
import com.qianjiang.framework.orm.DataManager;
import com.qianjiang.framework.orm.DatabaseBuilder;
import com.qianjiang.framework.util.PackageUtil;

/**
 * 数据库初始化类
 * 
 * @author zou.sq
 */
public class DBUtil {
	private static DatabaseBuilder DATABASE_BUILDER;
	private static PMRDataManager INSTANCE;

	// 获取数据库实例
	static {
		if (DATABASE_BUILDER == null) {
			DATABASE_BUILDER = new DatabaseBuilder(PackageUtil.getConfigString("db_name"));
			DATABASE_BUILDER.addClass(ProductCateModel.class);
			DATABASE_BUILDER.addClass(ProductModel.class);
			DATABASE_BUILDER.addClass(NewsCateModel.class);
			DATABASE_BUILDER.addClass(NewsModel.class);
			DATABASE_BUILDER.addClass(LeaderModel.class);
			DATABASE_BUILDER.addClass(HonorModel.class);
			DATABASE_BUILDER.addClass(ResponsibilityModel.class);
			DATABASE_BUILDER.addClass(VideoModel.class);
			DATABASE_BUILDER.addClass(DocumentModel.class);
		}
	}

	/**
	 * 
	 * @return 数据库管理器
	 */
	public static DataManager getDataManager() {
		if (INSTANCE == null) {
			INSTANCE = new PMRDataManager(QJApplicationBase.CONTEXT, DATABASE_BUILDER);
		}
		return INSTANCE;
	}

	static class PMRDataManager extends DataManager {
		protected PMRDataManager(Context context, DatabaseBuilder databaseBuilder) {
			super(context, PackageUtil.getConfigString("db_name"), PackageUtil.getConfigInt("db_version"), databaseBuilder);
		}
	}

}
