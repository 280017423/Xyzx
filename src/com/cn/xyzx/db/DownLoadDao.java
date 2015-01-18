package com.cn.xyzx.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;

import com.cn.xyzx.bean.DownloadInfoModel;
import com.cn.xyzx.bean.FileStateModel;
import com.cn.xyzx.util.DBUtil;
import com.qianjiang.framework.orm.DataAccessException;
import com.qianjiang.framework.orm.DataManager;

public class DownLoadDao {
	public static String LOCK = "dblock";
	public static String FILE_LOCK = "fileLock";

	/**
	 * 查看数据库中是否有数据
	 * 
	 * @param urlstr
	 *            下载地址
	 * @return count==0 如果数据库里没有数据,返回true,如果数据库里有数据返回false
	 */
	public static boolean isFirstDownLoad(String urlstr) {
		DataManager dataManager = DBUtil.getDataManager();
		String sql = "select count(*)  from DOWNLOAD_INFO_MODEL where URL = ?";
		Cursor cursor = dataManager.rawQuery(sql, new String[] { urlstr });
		cursor.moveToFirst();
		// getInt方法返回第0列的值
		int count = cursor.getInt(0);
		cursor.close();
		dataManager.close();
		return count == 0;
	}

	/**
	 * 保存 下载的具体信息 保存和更新方法最好设置为同步
	 */
	public static void saveInfos(List<DownloadInfoModel> infos) {
		synchronized (LOCK) {
			DataManager dataManager = DBUtil.getDataManager();
			dataManager.beginTransaction();
			try {
				for (DownloadInfoModel info : infos) {
					dataManager.save(info);
				}
			} catch (DataAccessException e) {
				e.printStackTrace();
			} finally {
				dataManager.endTransaction();
			}
			dataManager.close();
		}
	}

	/**
	 * 插入一条文件的状态记录
	 * **/
	public static void insertFileState(FileStateModel fileState) {
		synchronized (FILE_LOCK) {
			DataManager dataManager = DBUtil.getDataManager();
			dataManager.beginTransaction();
			try {
				dataManager.save(fileState);
			} catch (DataAccessException e) {
				e.printStackTrace();
			} finally {
				dataManager.endTransaction();
			}
			dataManager.close();
		}
	}

	/**
	 * 得到下载具体信息
	 * 
	 * @return List<DownloadInfoModel> 一个下载器信息集合器,里面存放了每条线程的下载信息
	 */
	public static List<DownloadInfoModel> getInfos(String urlstr) {
		List<DownloadInfoModel> list = new ArrayList<DownloadInfoModel>();
		DataManager dataManager = DBUtil.getDataManager();
		dataManager.beginTransaction();
		try {
			list = dataManager.getList(DownloadInfoModel.class, "URL = ?", new String[] { urlstr });
		} catch (DataAccessException e) {
			e.printStackTrace();
		} finally {
			dataManager.endTransaction();
		}
		dataManager.close();
		return list;
	}

	/**
	 * 得到文件的下载状态
	 * **/
	public static List<FileStateModel> getFileState() {
		List<FileStateModel> list = new ArrayList<FileStateModel>();
		DataManager dataManager = DBUtil.getDataManager();
		dataManager.beginTransaction();
		try {
			list = dataManager.getList(FileStateModel.class, null, null);
		} catch (DataAccessException e) {
			e.printStackTrace();
		} finally {
			dataManager.endTransaction();
		}
		dataManager.close();
		return list;
	}

	/**
	 * 如果存在则返回true
	 * **/
	public static boolean hasFile(String fileName) {
		DataManager dataManager = DBUtil.getDataManager();
		String sql = "select count(*)  from FILE_STATE_MODEL where FILE_NAME = ?";
		Cursor cursor = dataManager.rawQuery(sql, new String[] { fileName });
		cursor.moveToFirst();
		// getInt方法返回第0列的值
		int count = cursor.getInt(0);
		cursor.close();
		dataManager.close();
		return count != 0;
	}

	/**
	 * 更新数据库中的下载信息,保存和更新方法最好设置为同步
	 * 
	 * @param threadId
	 *            线程号
	 * @param compeleteSize
	 *            已经下载的长度
	 * @param urlstr
	 *            下载地址
	 */
	public static void updataInfos(int threadId, int compeleteSize, String urlstr) {
		synchronized (LOCK) {
			DataManager dataManager = DBUtil.getDataManager();
			dataManager.beginTransaction();
			ContentValues values = new ContentValues();
			values.put("COMPELETE_SIZE", compeleteSize);
			dataManager.updateByClause(DownloadInfoModel.class, values, "THREAD_ID = ? AND URL = ?", new String[] {
					threadId + "",
					urlstr });
			dataManager.endTransaction();
			dataManager.close();
		}
	}

	/**
	 * 更新文件的状态，1为正在下载，0为暂停
	 * **/
	public static void updateFileState(String url, int status) {
		synchronized (FILE_LOCK) {
			DataManager dataManager = DBUtil.getDataManager();
			dataManager.beginTransaction();
			ContentValues values = new ContentValues();
			values.put("STATE", status);
			dataManager.updateByClause(FileStateModel.class, values, "URL = ?", new String[] { url });
			dataManager.endTransaction();
			dataManager.close();
		}
	}
	
	/**
	 * 更新文件的状态，1为正在下载，0为暂停
	 * **/
	public static void updateFileCompleteSize(String url, int completeSize) {
		synchronized (FILE_LOCK) {
			DataManager dataManager = DBUtil.getDataManager();
			dataManager.beginTransaction();
			ContentValues values = new ContentValues();
			values.put("COMPLETE_SIZE", completeSize);
			dataManager.updateByClause(FileStateModel.class, values, "URL = ?", new String[] { url });
			dataManager.endTransaction();
			dataManager.close();
		}
	}

	/**
	 * 更新文件的下载状态
	 * **/
	public static void updateFileDownState(List<FileStateModel> list) {
		synchronized (FILE_LOCK) {
			DataManager dataManager = DBUtil.getDataManager();
			dataManager.beginTransaction();
			try {
				for (FileStateModel fileState : list) {
					ContentValues values = new ContentValues();
					values.put("COMPLETE_SIZE", fileState.getCompleteSize());
					values.put("STATE", fileState.getState());
					dataManager.updateByClause(FileStateModel.class, values, "URL = ?",
							new String[] { fileState.getUrl() });
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				dataManager.endTransaction();
			}
			dataManager.close();
		}
	}

	public static void delete(String url) {
		DataManager dataManager = DBUtil.getDataManager();
		dataManager.open();
		dataManager.delete(DownloadInfoModel.class, "URL = ?", new String[] { url });
		dataManager.close();
	}

	public static void deleteFileState(String fileName) {
		DataManager dataManager = DBUtil.getDataManager();
		dataManager.open();
		dataManager.delete(FileStateModel.class, "FILE_NAME = ?", new String[] { fileName });
		dataManager.close();
	}

}
