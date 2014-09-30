package com.dovi.projectTest.fragments;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;

import com.dovi.fragmentTransaction.FTFragment;
import com.dovi.fragmentTransaction.manager.FragmentTransactionAdapter.Animation;
import com.dovi.projectTest.MainActivity;
import com.dovi.projectTest.adapter.Adapter;
import com.example.projecttest.R;

public class MainContentFragment extends FTFragment implements OnClickListener {

	private ListView mList;
	private Adapter mAdapter;
	private String title;
	private String stack;
	private int count;
	private String tag;
	private MainActivity mActivity;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View mView = inflater.inflate(R.layout.fragment_list, container, false);

		mActivity = (MainActivity) getActivity();
		mList = (ListView) mView.findViewById(R.id.list);

		title = getArguments().getString("title");
		stack = getArguments().getString("stack");
		count = getArguments().getInt("count", 0);
		
		return mView;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		List<String> mListString = new ArrayList<String>();
		for (int i = 0; i < 50; i++) {
			mListString.add(i+" "+title + " | position : "+count);
		}

		mAdapter = new Adapter(getActivity(), mListString, this);
		mList.setAdapter(mAdapter);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		tag = v.getTag().toString();

		count++;
		
		Bundle mBundle = new Bundle();
		mBundle.putString("title", title);
		mBundle.putString("stack", stack);
		mBundle.putInt("count", count);

		mActivity.mFragmentTransactionManager.addFragmentInStack(stack,
				FTFragment.instantiate(mActivity, MainContentFragment.class.getName(), mBundle, Animation.ANIM_RIGHT_TO_LEFT, Animation.ANIM_LEFT_TO_RIGHT));
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub

		if (outState == null) {
			outState = new Bundle();
		}

//		outState.putAll(getArguments());
//		outState.putBoolean("saveState", true);

		super.onSaveInstanceState(outState);
	}
}
