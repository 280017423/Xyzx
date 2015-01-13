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
	private ImageButton mIbtn1;
	private ImageButton mIbtn2;
	private ImageButton mIbtn3;
	private ImageButton mIbtn4;
	private ImageButton mIbtn5;
	private ImageButton mIbtn6;
	private ImageButton mIbtn7;
	private ImageButton mIbtn8;
	private ImageButton mIbtn9;

	public static final RunHeFragment newInstance(int note_id) {
		RunHeFragment fragment = new RunHeFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.news_runhe_fragment, container, false);
		mIbtn1 = (ImageButton) view.findViewById(R.id.img_bt1);
		mIbtn2 = (ImageButton) view.findViewById(R.id.img_bt2);
		mIbtn3 = (ImageButton) view.findViewById(R.id.img_bt3);
		mIbtn4 = (ImageButton) view.findViewById(R.id.img_bt4);
		mIbtn5 = (ImageButton) view.findViewById(R.id.img_bt5);
		mIbtn6 = (ImageButton) view.findViewById(R.id.img_bt6);
		mIbtn7 = (ImageButton) view.findViewById(R.id.img_bt7);
		mIbtn8 = (ImageButton) view.findViewById(R.id.img_bt8);
		mIbtn9 = (ImageButton) view.findViewById(R.id.img_bt9);

		mIbtn1.setOnClickListener(this);
		mIbtn2.setOnClickListener(this);
		mIbtn3.setOnClickListener(this);
		mIbtn4.setOnClickListener(this);
		mIbtn5.setOnClickListener(this);
		mIbtn6.setOnClickListener(this);
		mIbtn7.setOnClickListener(this);
		mIbtn8.setOnClickListener(this);
		mIbtn9.setOnClickListener(this);
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
