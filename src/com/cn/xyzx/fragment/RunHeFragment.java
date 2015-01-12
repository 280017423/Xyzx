package com.cn.xyzx.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.cn.xyzx.R;
import com.cn.xyzx.activity.WebViewActivity;
import com.cn.xyzx.util.ServerAPIConstant;

public class RunHeFragment extends Fragment implements OnClickListener {
	ImageButton img_bt1, img_bt2, img_bt3, img_bt4, img_bt5, img_bt6, img_bt7, img_bt8, img_bt9;

	public static final RunHeFragment newInstance(int note_id) {
		RunHeFragment fragment = new RunHeFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.news_runhe_fragment, container, false);
		img_bt1 = (ImageButton) view.findViewById(R.id.img_bt1);
		img_bt2 = (ImageButton) view.findViewById(R.id.img_bt2);
		img_bt3 = (ImageButton) view.findViewById(R.id.img_bt3);
		img_bt4 = (ImageButton) view.findViewById(R.id.img_bt4);
		img_bt5 = (ImageButton) view.findViewById(R.id.img_bt5);
		img_bt6 = (ImageButton) view.findViewById(R.id.img_bt6);
		img_bt7 = (ImageButton) view.findViewById(R.id.img_bt7);
		img_bt8 = (ImageButton) view.findViewById(R.id.img_bt8);
		img_bt9 = (ImageButton) view.findViewById(R.id.img_bt9);

		img_bt1.setOnClickListener(this);
		img_bt2.setOnClickListener(this);
		img_bt3.setOnClickListener(this);
		img_bt4.setOnClickListener(this);
		img_bt5.setOnClickListener(this);
		img_bt6.setOnClickListener(this);
		img_bt7.setOnClickListener(this);
		img_bt8.setOnClickListener(this);
		img_bt9.setOnClickListener(this);

		return view;
	}

	@Override
	public void onClick(View view) {
		Intent intent = new Intent(getActivity(), WebViewActivity.class);

		switch (view.getId()) {
			case R.id.img_bt1:
				intent.setData(Uri.parse(ServerAPIConstant.getApiRootUrl() + ServerAPIConstant.API_GEI_SINGLE_PAGE
						+ "1696"));
				break;
			case R.id.img_bt2:
				intent.setData(Uri.parse(ServerAPIConstant.getApiRootUrl() + ServerAPIConstant.API_GEI_SINGLE_PAGE
						+ "1691"));
				break;
			case R.id.img_bt3:
				intent.setData(Uri.parse(ServerAPIConstant.getApiRootUrl() + ServerAPIConstant.API_GEI_SINGLE_PAGE
						+ "1688"));
				break;
			case R.id.img_bt4:
				intent.setData(Uri.parse(ServerAPIConstant.getApiRootUrl() + ServerAPIConstant.API_GEI_SINGLE_PAGE
						+ "1690"));
				break;
			case R.id.img_bt5:
				intent.setData(Uri.parse(ServerAPIConstant.getApiRootUrl() + ServerAPIConstant.API_GEI_SINGLE_PAGE
						+ "1694"));
				break;
			case R.id.img_bt6:
				intent.setData(Uri.parse(ServerAPIConstant.getApiRootUrl() + ServerAPIConstant.API_GEI_SINGLE_PAGE
						+ "1689"));
				break;
			case R.id.img_bt7:
				intent.setData(Uri.parse(ServerAPIConstant.getApiRootUrl() + ServerAPIConstant.API_GEI_SINGLE_PAGE
						+ "1693"));
				break;
			case R.id.img_bt8:
				intent.setData(Uri.parse(ServerAPIConstant.getApiRootUrl() + ServerAPIConstant.API_GEI_SINGLE_PAGE
						+ "1695"));
				break;
			case R.id.img_bt9:
				intent.setData(Uri.parse(ServerAPIConstant.getApiRootUrl() + ServerAPIConstant.API_GEI_SINGLE_PAGE
						+ "1692"));
				break;
			default:
				break;
		}

		startActivity(intent);
	}
}
