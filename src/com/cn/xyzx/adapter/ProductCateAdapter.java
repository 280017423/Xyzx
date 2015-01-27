package com.cn.xyzx.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.cn.xyzx.bean.ProductCateModel;

/**
 * 产品title适配器
 * 
 * @version 1.0
 * @author zou.sq
 */
public class ProductCateAdapter extends FragmentPagerAdapter {

	private List<Fragment> mFragments;
	private List<ProductCateModel> mProductCateModels;

	public ProductCateAdapter(FragmentManager fm, List<Fragment> fragments, List<ProductCateModel> list) {
		super(fm);
		this.mFragments = fragments;
		mProductCateModels = list;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		if (null == mProductCateModels) {
			return "";
		}
		return mProductCateModels.get(position).getCateName();
	}

	@Override
	public Fragment getItem(int position) {
		return this.mFragments.get(position);
	}

	@Override
	public int getCount() {
		return this.mProductCateModels.size();
	}

}