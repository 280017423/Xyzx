package com.cn.xyzx.bean;

import com.cn.xyzx.util.ServerAPIConstant;
import com.qianjiang.framework.orm.BaseModel;

public class NewsModel extends BaseModel {
	private static final long serialVersionUID = -110740210001636251L;
	private String cateId;
	private String newsId;
	private String summary;
	private String picture;
	private String title;

	public String getCateId() {
		return cateId;
	}

	public void setCateId(String cateId) {
		this.cateId = cateId;
	}

	public String getNewsId() {
		return newsId;
	}

	public void setNewsId(String newsId) {
		this.newsId = newsId;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

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

}
