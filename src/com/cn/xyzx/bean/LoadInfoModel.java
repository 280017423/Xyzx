package com.cn.xyzx.bean;

/**
 * 自定义的一个记载下载器详细信息的类 一个下载器对应一个LoadInfo 包括: fileSize文件大小 complete完成度
 * urlstring下载地址
 */
public class LoadInfoModel {
	public int fileSize;// 文件大小
	private int complete;// 完成度
	private String urlstring;// 下载器标识

	/**
	 * fileSize文件大小 complete完成度 urlstring下载地址
	 **/
	public LoadInfoModel(int fileSize, int complete, String urlstring) {
		this.fileSize = fileSize;
		this.complete = complete;
		this.urlstring = urlstring;
	}

	public LoadInfoModel() {
	}

	public int getFileSize() {
		return fileSize;
	}

	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

	public int getComplete() {
		return complete;
	}

	public void setComplete(int complete) {
		this.complete = complete;
	}

	public String getUrlstring() {
		return urlstring;
	}

	public void setUrlstring(String urlstring) {
		this.urlstring = urlstring;
	}
}