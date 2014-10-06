package com.dovi.projectTest.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dovi.fragmentTransaction.FTFragment;
import com.dovi.fragmentTransaction.manager.FragmentTransactionAdapter.Animation;
import com.dovi.fragmentTransaction.manager.FragmentTransactionManager;
import com.dovi.projectTest.MainActivity;
import com.example.projecttest.R;

public class TabFragment extends FTFragment implements OnClickListener {

	private MainActivity mActivity;
	private TextView tab1;
	private TextView tab2;
	private TextView tab3;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mView = inflater.inflate(R.layout.fragment_tab, container, false);
		
		mActivity = (MainActivity)getActivity();
		
		tab1 = (TextView) mView.findViewById(R.id.tab1);
		tab1.setOnClickListener(this);
		tab2 = (TextView) mView.findViewById(R.id.tab2);
		tab2.setOnClickListener(this);
		tab3 = (TextView) mView.findViewById(R.id.tab3);
		tab3.setOnClickListener(this);
		
		if (mActivity != null && mActivity.mFragmentTransactionManager != null) {
			
			mActivity.mFragmentTransactionManager.getCurrentStackNameFromContent(R.id.fragmentContentTab);
			if (mActivity.mFragmentTransactionManager.getCurrentStackNameFromContent(R.id.fragmentContentTab) == null) {
				tab1.performClick();
			} 
		}
		
		return mView;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Bundle mBundle = new Bundle();
		switch (v.getId()) {
		case R.id.tab1:
			
			if (mActivity.mFragmentTransactionManager.getCountOfFragmentsInStack(MainActivity.CONTENT_TAB1) > 0) {
				mActivity.mFragmentTransactionManager.showTopFragmentInStack(MainActivity.CONTENT_TAB1);
			} else {
				mActivity.mFragmentTransactionManager.createTag(MainActivity.CONTENT_TAB1, R.id.fragmentContentTab, 1);
				mActivity.mFragmentTransactionManager.setChildFragmentManager(MainActivity.CONTENT_TAB1, getChildFragmentManager());
				mBundle.putString("title", "Tab1");
				mBundle.putString("stack", MainActivity.CONTENT_TAB1);
				mActivity.mFragmentTransactionManager.addFragmentInStack(MainActivity.CONTENT_TAB1,
						FTFragment.instantiate(mActivity, MainContentFragment.class.getName(), mBundle, Animation.ANIM_NONE, Animation.ANIM_NONE));

			}
			
			break;
		case R.id.tab2:
			
			if (mActivity.mFragmentTransactionManager.getCountOfFragmentsInStack(MainActivity.CONTENT_TAB2) > 0) {
				mActivity.mFragmentTransactionManager.showTopFragmentInStack(MainActivity.CONTENT_TAB2);
			} else {
				mActivity.mFragmentTransactionManager.createTag(MainActivity.CONTENT_TAB2, R.id.fragmentContentTab, 1);
				mActivity.mFragmentTransactionManager.setChildFragmentManager(MainActivity.CONTENT_TAB2, getChildFragmentManager());
				mBundle.putString("title", "Tab2");
				mBundle.putString("stack", MainActivity.CONTENT_TAB2);
				mActivity.mFragmentTransactionManager.addFragmentInStack(MainActivity.CONTENT_TAB2,
						FTFragment.instantiate(mActivity, MainContentFragment.class.getName(), mBundle, Animation.ANIM_NONE, Animation.ANIM_NONE));
			}
			
			break;
		case R.id.tab3:
			
			if (mActivity.mFragmentTransactionManager.getCountOfFragmentsInStack(MainActivity.CONTENT_TAB3) > 0) {
				mActivity.mFragmentTransactionManager.showTopFragmentInStack(MainActivity.CONTENT_TAB3);
			} else {
				mActivity.mFragmentTransactionManager.createTag(MainActivity.CONTENT_TAB3, R.id.fragmentContentTab, 1);
				mActivity.mFragmentTransactionManager.setChildFragmentManager(MainActivity.CONTENT_TAB3, getChildFragmentManager());
				mBundle.putString("title", "Tab3");
				mBundle.putString("stack", MainActivity.CONTENT_TAB3);
				mActivity.mFragmentTransactionManager.addFragmentInStack(MainActivity.CONTENT_TAB3,
						FTFragment.instantiate(mActivity, MainContentFragment.class.getName(), mBundle, Animation.ANIM_NONE, Animation.ANIM_NONE));

			}
			
			break;
		default:
			break;
		}
	}
	
}
