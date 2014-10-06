package com.dovi.fragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.dovi.fragmentTransaction.manager.FragmentTransactionAdapter.Animation;

public class FTFragment extends Fragment {
	private static FTFragment f;
	public Bundle mExtraOutState = null;
	public Animation mAnimIn = Animation.ANIM_NONE;
	public Animation mAnimOut = Animation.ANIM_NONE;
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		mExtraOutState = outState;
		super.onSaveInstanceState(mExtraOutState);
	}
	
	public static FTFragment instantiate(Context context, String fragment, Bundle bundle, Animation animIn, Animation animOut) {
		
		if (fragment == null || fragment.length() == 0) {
			return null;
		}
		
		if (bundle == null) {
			bundle = new Bundle();
		}
	
		f = (FTFragment) FTFragment.instantiate(context, fragment, bundle);
		f.mAnimIn = animIn;
		f.mAnimOut = animOut;
		f.mExtraOutState = bundle;
		
		return f;
	}
	
	public void fragmentIsOn(){
		
	}
	
	
}
