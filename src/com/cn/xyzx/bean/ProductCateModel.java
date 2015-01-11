package com.cn.xyzx.bean;

import com.qianjiang.framework.orm.BaseModel;

public class ProductCateModel extends BaseModel {
	public static final String CATE_ID = "CATE_ID";
	private static final long serialVersionUID = 7842259224034673462L;
	private String cateId;
	private String cateName;

	public String getCateId() {
		return cateId;
	}

	public void setCateId(String cateId) {
		this.cateId = cateId;
	}

	public String getCateName() {
		return cateName;
	}

	public void setCateName(String cateName) {
		this.cateName = cateName;
	}

}
