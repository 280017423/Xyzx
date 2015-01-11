package com.cn.xyzx.fragment;

import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.cn.xyzx.R;
import com.cn.xyzx.activity.WebViewActivity;
import com.cn.xyzx.adapter.NewsAdapter;
import com.cn.xyzx.bean.NewsModel;
import com.cn.xyzx.bean.ProductCateModel;
import com.cn.xyzx.db.NewsDao;
import com.cn.xyzx.util.ServerAPIConstant;
import com.qianjiang.framework.util.StringUtil;

public class NewsFragment extends FragmentBase implements OnItemClickListener {
	private List<NewsModel> mNewsModels;
	private GridView mGvNews;
	private NewsAdapter mAdapter;

	public static final NewsFragment newInstance(String cateId) {
		NewsFragment fragment = new NewsFragment();
		Bundle bundle = new Bundle();
		bundle.putString(ProductCateModel.CATE_ID, cateId);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		String cateId = getArguments().getString(ProductCateModel.CATE_ID);
		mNewsModels = NewsDao.getNewsModels(cateId);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mView = inflater.inflate(R.layout.info_fragment, container, false);
		mGvNews = (GridView) mView.findViewById(R.id.info_gridView);
		mAdapter = new NewsAdapter(getActivity(), mNewsModels, mImageLoader);
		mGvNews.setNumColumns(2);
		mGvNews.setAdapter(mAdapter);
		mGvNews.setOnItemClickListener(this);
		return mView;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		NewsModel model = mAdapter.getItem(position);
		if (null != model && !StringUtil.isNullOrEmpty(model.getNewsId())) {
			Intent intent = new Intent(getActivity(), WebViewActivity.class);
			intent.setData(Uri.parse(ServerAPIConstant.getApiRootUrl() + ServerAPIConstant.API_GEI_NEWS_DETAIL
					+ model.getNewsId()));
			startActivity(intent);
		}
	}
}
