package com.cn.xyzx.bean;

import com.cn.xyzx.util.ServerAPIConstant;
import com.qianjiang.framework.orm.BaseModel;

public class ProductModel extends BaseModel {
	private static final long serialVersionUID = -110740210001636251L;
	private String cateId;
	private String productId;
	private String picture;
	private String title;

	public String getCateId() {
		return cateId;
	}

	public void setCateId(String cateId) {
		this.cateId = cateId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
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
