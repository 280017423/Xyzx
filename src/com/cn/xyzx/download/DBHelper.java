package com.cn.xyzx.download;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private static String DATABASE_NAME = "xinya.db";
	private static int version = 1;

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, version);
	}

	/**
	 * 在down.db数据库下创建一个download_info表存储下载信息 创建一个localdown_info表存储本地下载信息
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table download_info(_id integer PRIMARY KEY AUTOINCREMENT, thread_id integer, "
				+ "start_pos integer, end_pos integer, compelete_size integer,url varchar(50))");
		db.execSQL("create table localdown_info(_id integer PRIMARY KEY AUTOINCREMENT,music_name varchar(30),url varchar(50),completeSize integer,fileSize integer,state integer)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sql = "drop table if exists download_info";
		String sqlOne = "drop table if exists localdown_info";
		db.execSQL(sql);
		db.execSQL(sqlOne);
		onCreate(db);
	}

}
