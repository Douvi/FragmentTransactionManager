package com.dovi.fragmentTransaction.layout;

import com.dovi.fragmentTransaction.OnSaveFragmentTransaction;
import com.dovi.fragmentTransaction.manager.FragmentTransactionManager;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class FTRelativeLayout extends RelativeLayout {

	private FragmentTransactionManager fragmentManger;
	private OnSaveFragmentTransaction mLister;

	public FTRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public FTRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FTRelativeLayout(Context context) {
		super(context);
	}

	@Override
	protected Parcelable onSaveInstanceState() {

		fragmentManger = mLister.onSaveInstanceState();

		return fragmentManger.mFMSuper.onSaveInstanceState(super.onSaveInstanceState());

	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		// TODO Auto-generated method stub

		fragmentManger = new FragmentTransactionManager(getContext(), null, null);
		super.onRestoreInstanceState(fragmentManger.mFMSuper.onRestoreInstanceState(state));

	}

	public FragmentTransactionManager getFragmentManger(FragmentManager mFragmentManager, OnSaveFragmentTransaction lister) {

		if (fragmentManger == null) {
			fragmentManger = new FragmentTransactionManager(getContext(), mFragmentManager, this);
		} else {
			fragmentManger.setFragmentManager(mFragmentManager);
		}

		if (mLister == null) {
			mLister = lister;
		}

		return fragmentManger;
	}

}