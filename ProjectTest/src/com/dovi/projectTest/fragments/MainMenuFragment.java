package com.dovi.projectTest.fragments;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.dovi.fragmentTransaction.FTFragment;
import com.dovi.fragmentTransaction.manager.FragmentTransactionAdapter.Animation;
import com.dovi.projectTest.MainActivity;
import com.dovi.projectTest.adapter.Adapter;
import com.example.projecttest.R;

public class MainMenuFragment extends FTFragment implements OnItemClickListener {

	private ListView mList;
	private Adapter mAdapter;
	private String tag;
	private MainActivity mActivity;
	private List<String> mListString;

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

		mListString = new ArrayList<String>();
		mListString.add("Tab");
		mListString.add("List");
		mListString.add("Sub Menu");

		mAdapter = new Adapter(getActivity(), mListString);
		mList.setAdapter(mAdapter);
		mList.setOnItemClickListener(this);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		
		switch (position) {
		case 0:
			
			if (mActivity.mFragmentTransactionManager.getCountOfFragmentsInStack(MainActivity.CONTENT_TABS) > 0) {
				mActivity.mFragmentTransactionManager.showTopFragmentInStack(MainActivity.CONTENT_TABS);
			} else {
				mActivity.mFragmentTransactionManager.createTag(MainActivity.CONTENT_TABS, R.id.fragmentContent, 1);
				mActivity.mFragmentTransactionManager.addFragmentAsRootInStack(MainActivity.CONTENT_TABS, FTFragment.instantiate(mActivity, TabFragment.class.getName(), null, Animation.ANIM_NONE, Animation.ANIM_NONE));
			}
			mActivity.mMenu.showContent();
			break;
		case 1:
			
			if (mActivity.mFragmentTransactionManager.getCountOfFragmentsInStack(MainActivity.CONTENT_LIST) > 0) {
				mActivity.mFragmentTransactionManager.showTopFragmentInStack(MainActivity.CONTENT_LIST);
			} else {
				mActivity.mFragmentTransactionManager.createTag(MainActivity.CONTENT_LIST, R.id.fragmentContent, 1);
				Bundle mBundle = new Bundle();
				mBundle.putString("title", "List 1");
				mBundle.putString("stack", MainActivity.CONTENT_LIST);
				mActivity.mFragmentTransactionManager.addFragmentAsRootInStack(MainActivity.CONTENT_LIST, FTFragment.instantiate(mActivity, MainContentFragment.class.getName(), mBundle, Animation.ANIM_NONE, Animation.ANIM_NONE));
			}
			mActivity.mMenu.showContent();
			break;
		case 2:
			mActivity.mFragmentTransactionManager.addFragmentInStack(MainActivity.CONTENT_MENU,
					FTFragment.instantiate(mActivity, MainMenuFragment.class.getName(), null, Animation.ANIM_RIGHT_TO_LEFT, Animation.ANIM_LEFT_TO_RIGHT));
			break;
		default:
			break;
		}
		
		
	}
	
}
