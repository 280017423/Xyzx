package com.cn.xyzx.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.xyzx.R;
import com.cn.xyzx.bean.VideoModel;
import com.qianjiang.framework.imageloader.core.DisplayImageOptions;
import com.qianjiang.framework.imageloader.core.DisplayImageOptions.Builder;
import com.qianjiang.framework.imageloader.core.ImageLoader;
import com.qianjiang.framework.imageloader.core.display.SimpleBitmapDisplayer;

public class VideoAdapter extends BaseAdapter {
	private Context mContext;
	private List<VideoModel> mProductList;
	private ImageLoader mImageDownloader;
	private Builder mOptions;

	public VideoAdapter(Context context, List<VideoModel> list, ImageLoader loader) {
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
	public VideoModel getItem(int position) {
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
			convertView = View.inflate(mContext, R.layout.info_center_adapter, null);
			holder.imageView = (ImageView) convertView.findViewById(R.id.info_center_img);
			holder.textView = (TextView) convertView.findViewById(R.id.info_center_tv);
			convertView.setTag(holder);
		} else {
			holder = (GridViewHolder) convertView.getTag();
		}

		VideoModel productModel = mProductList.get(position);
		mImageDownloader.displayImage(productModel.getPicture(), holder.imageView,
				mOptions.showImageForEmptyUri(R.drawable.news_default).build());
		holder.textView.setText(productModel.getTitle());

		return convertView;
	}

	final class GridViewHolder {
		ImageView imageView;
		TextView textView;
	}
}
