package com.cn.xyzx.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;

import com.cn.xyzx.R;
import com.cn.xyzx.adapter.ProductCateAdapter;
import com.cn.xyzx.bean.ProductCateModel;
import com.cn.xyzx.db.DbDao;
import com.cn.xyzx.fragment.ProductFragment;
import com.cn.xyzx.req.ProductReq;
import com.cn.xyzx.util.ActionResult;
import com.cn.xyzx.widget.LineTabIndicator;
import com.qianjiang.framework.widget.LoadingUpView;

public class ProductActivity extends FragmentActivityBase implements OnClickListener {

	private ViewPager mVpProduct;
	private LineTabIndicator mLineTabIndicator;
	private ProductCateAdapter mPageAdapter;
	private LoadingUpView mLoadingUpView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product);
		initVariables();
		initViews();
		getProduct();
	}

	private void initVariables() {
		mLoadingUpView = new LoadingUpView(this);
	}

	private void initViews() {
		mVpProduct = (ViewPager) findViewById(R.id.viewpager_infoCenter);
		mLineTabIndicator = (LineTabIndicator) findViewById(R.id.line_tab_indicator);
	}

	private void getProduct() {
		List<ProductCateModel> list = DbDao.getModels(ProductCateModel.class);
		if (null == list || list.isEmpty()) {
			showLoadingUpView(mLoadingUpView);
		} else {
			intFragments(list);
		}
		new AsyncLogin().execute();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.bt_Bottom_back:
				finish();
				break;
			default:
				break;
		}
	}

	class AsyncLogin extends AsyncTask<Void, Void, ActionResult> {

		@Override
		protected void onPostExecute(ActionResult result) {
			if (result != null && ActionResult.RESULT_CODE_SUCCESS.equals(result.ResultCode)) {
			} else {
				showErrorMsg(result);
			}
			List<ProductCateModel> list = DbDao.getModels(ProductCateModel.class);
			intFragments(list);
			dismissLoadingUpView(mLoadingUpView);
		}

		@Override
		protected ActionResult doInBackground(Void... params) {
			return ProductReq.getProductInfo();
		}
	}

	private void intFragments(List<ProductCateModel> list) {
		if (null == list || list.isEmpty()) {
			return;
		}
		List<Fragment> fragments = new ArrayList<Fragment>();
		for (ProductCateModel model : list) {
			ProductFragment product = ProductFragment.newInstance(model.getCateId());
			fragments.add(product);
		}
		mPageAdapter = new ProductCateAdapter(getSupportFragmentManager(), fragments, list);
		mVpProduct.setAdapter(mPageAdapter);
		mLineTabIndicator.setViewPager(mVpProduct);
	}
}
