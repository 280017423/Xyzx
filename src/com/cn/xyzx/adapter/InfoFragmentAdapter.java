package com.cn.xyzx.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class InfoFragmentAdapter extends FragmentPagerAdapter {

	private List<Fragment> fragments;
	private String[] mTitles;

	public InfoFragmentAdapter(FragmentManager fm, List<Fragment> fragments, String[] titles) {
		super(fm);
		this.fragments = fragments;
		mTitles = titles;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		if (null == mTitles) {
			return null;
		}
		return mTitles[position];
	}

	@Override
	public Fragment getItem(int position) {
		return this.fragments.get(position);
	}

	@Override
	public int getCount() {
		return this.fragments.size();
	}
}
