package com.cn.xyzx.bean;

import com.cn.xyzx.util.ServerAPIConstant;
import com.qianjiang.framework.orm.BaseModel;

public class VideoModel extends BaseModel {
	private static final long serialVersionUID = 6312187208166867611L;
	private String id;// 文章ID
	private String title;// 文章标题
	private String videoUrl; // 视频路径
	private String picture;// 缩略图
	private String summary;// 简介
	private String fileName;// 简介
	private int hasDownload;

	public String getPicture() {
		return null == picture ? "" : ServerAPIConstant.getApiRootUrl() + picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getTitle() {
		return null == title ? "" : title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getVideoUrl() {
		return null == videoUrl ? "" : ServerAPIConstant.getApiRootUrl() + videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public int getHasDownload() {
		return hasDownload;
	}

	public void setHasDownload(int hasDownload) {
		this.hasDownload = hasDownload;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFineName(String fileName) {
		this.fileName = fileName;
	}

}
