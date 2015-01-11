package com.cn.xyzx.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.cn.xyzx.bean.NewsCateModel;

public class NewsFragmentAdapter extends FragmentPagerAdapter {

	private List<Fragment> mFragments;
	private List<NewsCateModel> mProductCateModels;

	public NewsFragmentAdapter(FragmentManager fm, List<Fragment> fragments, List<NewsCateModel> list) {
		super(fm);
		this.mFragments = fragments;
		mProductCateModels = list;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		if (null == mProductCateModels) {
			return null;
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
