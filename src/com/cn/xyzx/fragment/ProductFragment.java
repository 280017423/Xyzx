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
import com.cn.xyzx.adapter.ProductAdapter;
import com.cn.xyzx.bean.ProductCateModel;
import com.cn.xyzx.bean.ProductModel;
import com.cn.xyzx.db.ProductDao;
import com.cn.xyzx.util.ServerAPIConstant;
import com.qianjiang.framework.util.StringUtil;

public class ProductFragment extends FragmentBase implements OnItemClickListener {
	private List<ProductModel> mProductModels;
	private GridView mGvProduct;
	private ProductAdapter mAdapter;

	public static final ProductFragment newInstance(String cateId) {
		ProductFragment fragment = new ProductFragment();
		Bundle bundle = new Bundle();
		bundle.putString(ProductCateModel.CATE_ID, cateId);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		String cateId = getArguments().getString(ProductCateModel.CATE_ID);
		mProductModels = ProductDao.getProductModels(cateId);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mView = inflater.inflate(R.layout.fragment_info, container, false);
		mGvProduct = (GridView) mView.findViewById(R.id.info_gridView);
		mAdapter = new ProductAdapter(getActivity(), mProductModels, mImageLoader);
		mGvProduct.setNumColumns(3);
		mGvProduct.setAdapter(mAdapter);
		mGvProduct.setOnItemClickListener(this);
		return mView;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		ProductModel model = mAdapter.getItem(position);
		if (null != model && !StringUtil.isNullOrEmpty(model.getProductId())) {
			Intent intent = new Intent(getActivity(), WebViewActivity.class);
			intent.setData(Uri.parse(ServerAPIConstant.getApiRootUrl() + ServerAPIConstant.API_GEI_PRODUCT_DETAIL
					+ model.getProductId()));
			startActivity(intent);
		}
	}
}
