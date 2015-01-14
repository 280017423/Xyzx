package com.cn.xyzx.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.xyzx.R;
import com.cn.xyzx.bean.LeaderModel;
import com.qianjiang.framework.imageloader.core.DisplayImageOptions;
import com.qianjiang.framework.imageloader.core.DisplayImageOptions.Builder;
import com.qianjiang.framework.imageloader.core.ImageLoader;
import com.qianjiang.framework.imageloader.core.display.SimpleBitmapDisplayer;

public class LeadListAdapter extends BaseAdapter {

	private Context mContext;
	private List<LeaderModel> mLeaderList;
	private ImageLoader mImageDownloader;
	private Builder mOptions;

	public LeadListAdapter(Context context, List<LeaderModel> list, ImageLoader loader) {
		this.mContext = context;
		mLeaderList = list;
		mImageDownloader = loader;
		mOptions = new DisplayImageOptions.Builder().cacheInMemory().cacheOnDisc()
				.displayer(new SimpleBitmapDisplayer());
	}

	@Override
	public int getCount() {
		if (null == mLeaderList) {
			return 0;
		}
		return mLeaderList.size();
	}

	@Override
	public LeaderModel getItem(int position) {
		if (null == mLeaderList) {
			return null;
		}
		return mLeaderList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		GridViewHolder holder = new GridViewHolder();
		LeaderModel leaderModel = mLeaderList.get(position);
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.view_leader_item, null);

			holder.mIvLeader = (ImageView) convertView.findViewById(R.id.lead_img);
			holder.mTvSummary = (TextView) convertView.findViewById(R.id.lead_tv);
			holder.mTvTitle = (TextView) convertView.findViewById(R.id.new_title_01);
			holder.mTvSubTitle = (TextView) convertView.findViewById(R.id.new_title_02);
			convertView.setTag(holder);
		} else {
			holder = (GridViewHolder) convertView.getTag();
		}
		mImageDownloader.displayImage(leaderModel.getPicture(), holder.mIvLeader,
				mOptions.showImageForEmptyUri(R.drawable.news_default).build());
		holder.mTvSummary.setText(leaderModel.getSummary());
		holder.mTvTitle.setText(leaderModel.getTitle());
		holder.mTvSubTitle.setText(leaderModel.getSubtitle());
		return convertView;
	}

	private final class GridViewHolder {
		ImageView mIvLeader;
		TextView mTvSummary;
		TextView mTvTitle;
		TextView mTvSubTitle;
	}

}
