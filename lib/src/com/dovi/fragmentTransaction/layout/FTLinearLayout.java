package com.dovi.fragmentTransaction.layout;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.dovi.fragmentTransaction.FragmentTransactionBuilder;
import com.dovi.fragmentTransaction.OnSaveFragmentTransaction;

public class FTLinearLayout extends LinearLayout {

	private FragmentTransactionBuilder fragmentManger;
	private OnSaveFragmentTransaction mLister;
	
	public FTLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FTLinearLayout(Context context) {
		super(context);
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		// TODO Auto-generated method stub
		fragmentManger = mLister.onSaveInstanceState();
		
		return fragmentManger.mFragmentsStack.onSaveInstanceState(super.onSaveInstanceState());
	}
	
	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		// TODO Auto-generated method stub
		fragmentManger = new FragmentTransactionBuilder(getContext(), null, null);
		super.onRestoreInstanceState(fragmentManger.mFragmentsStack.onRestoreInstanceState(state));
	}
	
	public FragmentTransactionBuilder getFragmentManger(FragmentManager mFragmentManager, OnSaveFragmentTransaction lister) {
		
		if (fragmentManger == null) {
			fragmentManger = new FragmentTransactionBuilder(getContext(), mFragmentManager, this);
		} else {
			fragmentManger.setFragmentManager(mFragmentManager);
		}
		
		if (mLister == null) {
			mLister = lister;
		}
		
		return fragmentManger;
	}

}