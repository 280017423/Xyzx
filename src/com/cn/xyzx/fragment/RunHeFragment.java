package com.cn.xyzx.fragment;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.cn.xyzx.R;
import com.cn.xyzx.bean.Tb_Tree;

public class RunHeFragment extends Fragment implements OnClickListener {
	private static final String tag = "News_RunHe_Fragment";

	Context context;
	View mView;
	ImageButton img_bt1, img_bt2, img_bt3, img_bt4, img_bt5, img_bt6, img_bt7, img_bt8, img_bt9;
	List<Tb_Tree> list_tree = new ArrayList<Tb_Tree>();

	public static final RunHeFragment newInstance(int note_id) {
		RunHeFragment fragment = new RunHeFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("note_id", note_id);
		fragment.setArguments(bundle);
		Log.i(tag, "note_id=" + note_id);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		try {
			context = this.getActivity().getApplicationContext();
			mView = inflater.inflate(R.layout.news_runhe_fragment, container, false);
			int note_id = getArguments().getInt("note_id");
			img_bt1 = (ImageButton) mView.findViewById(R.id.img_bt1);
			img_bt2 = (ImageButton) mView.findViewById(R.id.img_bt2);
			img_bt3 = (ImageButton) mView.findViewById(R.id.img_bt3);
			img_bt4 = (ImageButton) mView.findViewById(R.id.img_bt4);
			img_bt5 = (ImageButton) mView.findViewById(R.id.img_bt5);
			img_bt6 = (ImageButton) mView.findViewById(R.id.img_bt6);
			img_bt7 = (ImageButton) mView.findViewById(R.id.img_bt7);
			img_bt8 = (ImageButton) mView.findViewById(R.id.img_bt8);
			img_bt9 = (ImageButton) mView.findViewById(R.id.img_bt9);

			img_bt1.setOnClickListener(this);
			img_bt2.setOnClickListener(this);
			img_bt3.setOnClickListener(this);
			img_bt4.setOnClickListener(this);
			img_bt5.setOnClickListener(this);
			img_bt6.setOnClickListener(this);
			img_bt7.setOnClickListener(this);
			img_bt8.setOnClickListener(this);
			img_bt9.setOnClickListener(this);

		} catch (Exception e) {
			Log.e(tag, e.getMessage());
			StringWriter stringwriter = new StringWriter();
			e.printStackTrace(new PrintWriter(stringwriter, true));
		}
		return mView;
	}

	@Override
	public void onClick(View view) {
		Intent infoCenter = new Intent();
		Bundle bundle = new Bundle();

		switch (view.getId()) {
			case R.id.img_bt1:
				break;
			case R.id.img_bt2:
				break;
			case R.id.img_bt3:
				break;
			case R.id.img_bt4:
				break;
			case R.id.img_bt5:
				break;
			case R.id.img_bt6:
				break;
			case R.id.img_bt7:
				break;
			case R.id.img_bt8:
				break;
			case R.id.img_bt9:
				break;

			default:
				break;
		}
	}

}
