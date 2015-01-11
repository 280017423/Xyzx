package com.cn.xyzx.bean;

import com.cn.xyzx.util.ServerAPIConstant;
import com.qianjiang.framework.orm.BaseModel;

public class ResponsibilityModel extends BaseModel {
	private static final long serialVersionUID = -110740210001636251L;
	private String cateId;
	private String id;// 文章ID
	private String title;// 文章标题
	private String subtitle;// 副标题
	private String picture;// 缩略图
	private String summary;// 简介

	public String getCateId() {
		return cateId;
	}

	public void setCateId(String cateId) {
		this.cateId = cateId;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

}
