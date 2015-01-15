package com.cn.xyzx.bean;

import com.qianjiang.framework.orm.BaseModel;

/**
 * 创建一个下载信息的实体类,每一个DownloadInfoModel保存这1个线程的下载信息 1.threadId:下载线程ID
 * 2.startPos：当前线程下载开始点 3.endPos:当前线程下载结束点 4.compeleteSize:当前线程下载了多少数据
 * 5.url:下载地址
 */
public class DownloadInfoModel extends BaseModel {
	private static final long serialVersionUID = -330571224680715176L;
	private int threadId; // 下载器id
	private int startPos; // 开始点
	private int endPos; // 结束点
	private int compeleteSize; // 完成度
	private String url; // 下载器网络标识

	public DownloadInfoModel() {
	}

	public DownloadInfoModel(int threadId, int startPos, int endPos, int compeleteSize, String url) {
		this.threadId = threadId;
		this.startPos = startPos;
		this.endPos = endPos;
		this.compeleteSize = compeleteSize;
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getThreadId() {
		return threadId;
	}

	public void setThreadId(int threadId) {
		this.threadId = threadId;
	}

	public int getStartPos() {
		return startPos;
	}

	public void setStartPos(int startPos) {
		this.startPos = startPos;
	}

	public int getEndPos() {
		return endPos;
	}

	public void setEndPos(int endPos) {
		this.endPos = endPos;
	}

	public int getCompeleteSize() {
		return compeleteSize;
	}

	public void setCompeleteSize(int compeleteSize) {
		this.compeleteSize = compeleteSize;
	}

}