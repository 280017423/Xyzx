package com.cn.xyzx.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cn.xyzx.R;

public class EnterpriseCultureFragment extends FragmentBase {

	public static final EnterpriseCultureFragment newInstance() {
		EnterpriseCultureFragment fragment = new EnterpriseCultureFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		initVariables();
		super.onCreate(savedInstanceState);
	}

	private void initVariables() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_enterprise_culture, container, false);
		return view;
	}

}
