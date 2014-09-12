package com.dovi.fragmentTransaction.layout;

import com.dovi.fragmentTransaction.FragmentTransactionBuilder;
import com.dovi.fragmentTransaction.OnSaveFragmentTransaction;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class FTRelativeLayout extends RelativeLayout {

	private FragmentTransactionBuilder fragmentManger;
	private OnSaveFragmentTransaction mLister;
	
	public FTRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public FTRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public FTRelativeLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
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