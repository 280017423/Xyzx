package com.cn.xyzx.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.xyzx.R;
import com.cn.xyzx.bean.NewsModel;
import com.qianjiang.framework.imageloader.core.DisplayImageOptions;
import com.qianjiang.framework.imageloader.core.DisplayImageOptions.Builder;
import com.qianjiang.framework.imageloader.core.ImageLoader;
import com.qianjiang.framework.imageloader.core.display.SimpleBitmapDisplayer;

public class NewsAdapter extends BaseAdapter {
	private Context mContext;
	private List<NewsModel> mProductList;
	private ImageLoader mImageDownloader;
	private Builder mOptions;

	public NewsAdapter(Context context, List<NewsModel> list, ImageLoader loader) {
		mContext = context;
		mProductList = list;
		mImageDownloader = loader;
		mOptions = new DisplayImageOptions.Builder().cacheInMemory().cacheOnDisc()
				.displayer(new SimpleBitmapDisplayer());
	}

	@Override
	public int getCount() {
		if (null == mProductList) {
			return 0;
		}
		return mProductList.size();
	}

	@Override
	public NewsModel getItem(int position) {
		if (null != mProductList) {
			return mProductList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		GridViewHolder holder = new GridViewHolder();
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.news_list_adapter, null);
			holder.mImageView = (ImageView) convertView.findViewById(R.id.news_list_img);
			holder.mTvTitle = (TextView) convertView.findViewById(R.id.news_list_tv_title);
			holder.mTvSummary = (TextView) convertView.findViewById(R.id.news_list_tv_context);
			convertView.setTag(holder);
		} else {
			holder = (GridViewHolder) convertView.getTag();
		}

		NewsModel productModel = mProductList.get(position);
		mImageDownloader.displayImage(productModel.getPicture(), holder.mImageView,
				mOptions.showImageForEmptyUri(R.drawable.news_default).build());
		holder.mTvTitle.setText(productModel.getTitle());
		holder.mTvSummary.setText(productModel.getSummary());
		return convertView;
	}

	final class GridViewHolder {
		ImageView mImageView;
		TextView mTvTitle;
		TextView mTvSummary;
	}
}
