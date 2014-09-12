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
import com.dovi.fragmentTransaction.FragmentTransactionAdapter.Animation;
import com.dovi.projectTest.MainActivity;
import com.dovi.projectTest.adapter.Adapter;
import com.example.projecttest.R;

public class MainMenuFragment extends FTFragment implements OnClickListener {

	private ListView mList;
	private Adapter mAdapter;
	private String tag;
	private MainActivity mActivity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View mView = inflater.inflate(R.layout.fragment_list, container, false);

		mActivity = (MainActivity) getActivity();
		mList = (ListView) mView.findViewById(R.id.list);

		return mView;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		List<String> mListString = new ArrayList<String>();
		mListString.add("People");
		mListString.add("Animal");
		mListString.add("Cake");
		mListString.add("Car");

		mAdapter = new Adapter(getActivity(), mListString, this);
		mList.setAdapter(mAdapter);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		tag = v.getTag().toString();

		Bundle mBundle = new Bundle();
		mBundle.putString("title", tag);

		mActivity.mFragmentTransactionBuilder.addFragmentInStack("Menu",
				FTFragment.instantiate(mActivity, SubMenuFagment.class.getName(), mBundle, Animation.ANIM_RIGHT_TO_LEFT, Animation.ANIM_LEFT_TO_RIGHT));
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub

		if (outState == null) {
			outState = new Bundle();
		}

//		outState.putAll(getArguments());
//		outState.putString("clicked", click);

		super.onSaveInstanceState(outState);
	}
}
