package com.dovi.fragmentTransaction.manager;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.dovi.fragmentTransaction.FTFragment;
import com.dovi.fragmentTransaction.R;
import com.dovi.fragmentTransaction.manager.FragmentTransactionAdapter.Animation;

public class FragmentTransactionAdapter extends SuperFragmentTransactionAdapter {

	public enum Animation {
		ANIM_NONE(0), ANIM_LEFT_TO_RIGHT(1), ANIM_RIGHT_TO_LEFT(2), ANIM_FADIN(3), ANIM_OPEN(4), ANIM_CLOSE(5);

		private int animation;

		private Animation(int value) {
			animation = value;
		}

		public int getAnimation() {
			return animation;
		}

		public static Animation getAnimation(int val) {
			Animation mAnimation;
			switch (val) {
			case 0:
				mAnimation = Animation.ANIM_NONE;
				break;
			case 1:
				mAnimation = Animation.ANIM_LEFT_TO_RIGHT;
				break;
			case 2:
				mAnimation = Animation.ANIM_RIGHT_TO_LEFT;
				break;
			default:
				mAnimation = Animation.ANIM_NONE;
				break;
			}
			return mAnimation;
		}
	}

	public FragmentTransactionAdapter(Context context, FragmentManager fm, String tag, int containerId, int detachFragmentLimited) {
		super(context, fm, tag, containerId, detachFragmentLimited);
	}

	public void add(ViewGroup viewGroup, FTFragment fragment) {
		getCurrentTransaction();

		if (fragmentsNumb >= 0) {

			if (mDetachFragmentLimited > 0) {

				this.detach(mCurrentPrimaryItem);

				mDetachedFragments.add(mCurrentPrimaryItem);

				if (mDetachedFragments.size() > mDetachFragmentLimited) {
					this.destroyItem(viewGroup, fragmentsNumb - mDetachFragmentLimited, mDetachedFragments.get(0));
					mDetachedFragments.remove(0);
				}

			} else {
				this.destroyItem(viewGroup, fragmentsNumb, mCurrentPrimaryItem);
			}

		}

		fragmentsNumb++;
		this.setPrimaryItem(viewGroup, fragmentsNumb, fragment);

		mCurrentTransaction.add(mContainerId, fragment, this.getFragmentName(fragmentsNumb));
	}

	public void detach(FTFragment fragment) {
		getCurrentTransaction();

		fragment.onSaveInstanceState(fragment.mExtraOutState);
		mCurrentTransaction.detach(fragment);
		mIsCurrentPrimaryItemDetatch = true;
	}

	public void removed(FTFragment fragment) {
		getCurrentTransaction();

		mCurrentTransaction.remove(fragment);
		fragmentsNumb--;
	}

	public void attach(ViewGroup viewGroup, FTFragment fragment) {
		getCurrentTransaction();

		this.setPrimaryItem(viewGroup, fragmentsNumb, fragment);
		mCurrentTransaction.attach(fragment);
	}

	public void setTransactionAnimation(Animation animation) {
		getCurrentTransaction();

		switch (animation) {
			case ANIM_LEFT_TO_RIGHT:
				mCurrentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right);
				break;
			case ANIM_RIGHT_TO_LEFT:
				mCurrentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);
				break;
			case ANIM_FADIN:
				mCurrentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				break;
			case ANIM_OPEN:
				mCurrentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
				break;
			case ANIM_CLOSE:
				mCurrentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
				break;
			default:
				break;
		}

	}

	@Override
	public int getCount() {
		return fragmentsNumb + 1;
	}

	public int getContainerId() {
		return this.mContainerId;
	}

}

abstract class SuperFragmentTransactionAdapter extends PagerAdapter {
	protected static final String TAG = "SuperFragmentStackAdpater";

	protected FragmentManager mFragmentManager;
	protected Context mContext;
	protected String mTag;
	protected int mContainerId;
	protected int mDetachFragmentLimited;

	protected List<FTFragment> mDetachedFragments = new ArrayList<FTFragment>();
	protected ArrayList<SavedFragment> mSavedFragments = new ArrayList<SavedFragment>();

	protected int fragmentsNumb = -1;
	protected FTFragment mCurrentPrimaryItem = null;
	protected boolean mIsCurrentPrimaryItemDetatch;

	protected FragmentTransaction mCurrentTransaction = null;
	protected SavedFragment mSavedFragment;

	protected FTFragment fragment;
	protected Bundle mBundle;

	public SuperFragmentTransactionAdapter(Context context, FragmentManager fm, String tag, int containerId, int detachFragmentLimited) {
		mFragmentManager = fm;
		mContext = context;
		mTag = tag;
		mContainerId = containerId;
		mDetachFragmentLimited = detachFragmentLimited <= 0 ? 0 : detachFragmentLimited;
	}
	
	public void setFragmentManager(FragmentManager fm) {
		mFragmentManager = fm;
	}

	protected void getCurrentTransaction() {
		if (mCurrentTransaction == null) {
			mCurrentTransaction = mFragmentManager.beginTransaction();
		}
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		fragment = null;

		if (mSavedFragments.size() > position) {
			mSavedFragment = mSavedFragments.get(position);
			if (mSavedFragment != null) {
				
				fragment = FTFragment.instantiate(mContext, mSavedFragment.mPath, mSavedFragment.mBundle, Animation.getAnimation(mSavedFragment.mAnimIn), Animation.getAnimation(mSavedFragment.mAnimOut));
				fragment.setMenuVisibility(true);
				fragment.setUserVisibleHint(true);
				
				this.getCurrentTransaction();
				mCurrentTransaction.add(mContainerId, fragment, this.getFragmentName(fragmentsNumb));
				mCurrentTransaction.detach(fragment);
				
			}
			mSavedFragments.remove(position);
		} else {
			int val = position - mSavedFragments.size();

			if (mDetachedFragments.size() > val) {
				fragment = mDetachedFragments.get(val);
				mDetachedFragments.remove(val);

				if (mSavedFragments.size() > 0) {
					mSavedFragment = mSavedFragments.get(mSavedFragments.size() - 1);
					if (mSavedFragment != null) {
						
						FTFragment fragmentTemp = FTFragment.instantiate(mContext, mSavedFragment.mPath, mSavedFragment.mBundle, Animation.getAnimation(mSavedFragment.mAnimIn), Animation.getAnimation(mSavedFragment.mAnimOut));
						fragmentTemp.setMenuVisibility(false);
						fragmentTemp.setUserVisibleHint(false);
						mDetachedFragments.add(fragmentTemp);

						this.getCurrentTransaction();
						mCurrentTransaction.add(mContainerId, fragmentTemp, this.getFragmentName(fragmentsNumb));
						mCurrentTransaction.detach(fragmentTemp);
					}
					mSavedFragments.remove(mSavedFragments.size() - 1);
				}

			}
		}

		return fragment;
	}

	@SuppressLint("Recycle")
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		fragment = (FTFragment) object;

		getCurrentTransaction();

		while (mSavedFragments.size() <= position) {
			mSavedFragments.add(null);
		}
		fragment.getView();

		fragment.onSaveInstanceState(fragment.mExtraOutState);
		mSavedFragments.set(position,
				new SavedFragment(fragment.getClass().getName(), fragment.mExtraOutState, fragment.mAnimIn.getAnimation(), fragment.mAnimOut.getAnimation()));
	}

	public void destroyItemWithOutSaving(ViewGroup container, int position, Object object) {
		// fragment = (FSFragment) object;
		getCurrentTransaction();
		fragmentsNumb--;
		mSavedFragments.remove(fragmentsNumb);
		// fragment.getView();
	}

	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		fragment = (FTFragment) object;

		mIsCurrentPrimaryItemDetatch = false;

		if (fragment != mCurrentPrimaryItem) {
			if (mCurrentPrimaryItem != null) {
				mCurrentPrimaryItem.setMenuVisibility(false);
				mCurrentPrimaryItem.setUserVisibleHint(false);
			}
			if (fragment != null) {
				fragment.setMenuVisibility(true);
				fragment.setUserVisibleHint(true);
			}
			mCurrentPrimaryItem = fragment;
		}

		fragment = null;
	}

	public Boolean isPrimaryItemEmpty() {
		return mCurrentPrimaryItem == null;
	}

	public FTFragment getPrimaryItemEmpty() {
		return mCurrentPrimaryItem;
	}

	@Override
	public void finishUpdate(ViewGroup container) {
		super.finishUpdate(container);

		if (mCurrentTransaction != null) {
			// mCurrentTransaction.commit();
			mCurrentTransaction.commitAllowingStateLoss();
			mCurrentTransaction = null;
			mFragmentManager.executePendingTransactions();
		}
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return ((FTFragment) object).getView() == view;
	}

	protected String getFragmentName(int position) {
		return "Tag:" + mTag + "-Container:" + mContainerId + "-Position:" + position;
	}

	@Override
	public Parcelable saveState() {
		Log.i("NAME_TAG_METHOD", "");
		Log.i("NAME_TAG_METHOD", "		++++++++++++ BEGIN saveState BEGIN ++++++++++++");
		Bundle state = null;
		if (mSavedFragments.size() > 0) {
			state = new Bundle();
			SavedFragment[] savedFragmentArray = new SavedFragment[mSavedFragments.size()];
			mSavedFragments.toArray(savedFragmentArray);
			state.putParcelableArray("fragments", savedFragmentArray);
		} 
		
		Log.i("NAME_TAG", "		--------> SAVED_FRAGMENT NB : "+mSavedFragments.size());

		if (state == null) {
			state = new Bundle();
		}
		state.putInt("fragmentsNumb", fragmentsNumb);
		state.putInt("fragment-detached-nb", mDetachedFragments.size());

		if (mDetachedFragments.size() > 0) {

			for (int i = 0; i < mDetachedFragments.size(); i++) {
				fragment = mDetachedFragments.get(i);
				fragment.onSaveInstanceState(fragment.mExtraOutState);
				state.putBundle("fragment-detached-bundle" + i, fragment.mExtraOutState);
				state.putInt("fragment-detached-animIn" + i, fragment.mAnimIn.getAnimation());
				state.putInt("fragment-detached-animOut" + i, fragment.mAnimOut.getAnimation());
				state.putString("fragment-detached-path" + i, fragment.getClass().getName());
				mCurrentTransaction.remove(fragment);
			}
		}
		
		Log.i("NAME_TAG", "		--------> SAVED_DETACHED_FRAGMENT NB : "+mDetachedFragments.size());

		if (mCurrentPrimaryItem != null) {
			mCurrentPrimaryItem.onSaveInstanceState(mCurrentPrimaryItem.mExtraOutState);
			state.putBundle("primary-fragment-bundle", mCurrentPrimaryItem.mExtraOutState);
			state.putInt("primary-fragment-animIn", mCurrentPrimaryItem.mAnimIn.getAnimation());
			state.putInt("primary-fragment-animOut", mCurrentPrimaryItem.mAnimOut.getAnimation());
			state.putString("primary-fragment-path", mCurrentPrimaryItem.getClass().getName());
			state.putBoolean("primary-fragment-type", mCurrentPrimaryItem.isDetached());
			
			Log.i("NAME_TAG", "		--------> SAVED_CURRENT_FRAGMENT : YES");
		} else {
			Log.i("NAME_TAG", "		--------> SAVED_CURRENT_FRAGMENT : NO");
		}
		
		Log.i("NAME_TAG_METHOD", "		++++++++++++ END saveState END ++++++++++++");
		return state;
	}

	@Override
	public void restoreState(Parcelable state, ClassLoader loader) {
		Log.i("NAME_TAG_METHOD", "++++++++++++ BEGIN restoreState BEGIN ++++++++++++");
		
		if (state != null) {
			Bundle bundle = (Bundle) state;
			bundle.setClassLoader(loader);

			mSavedFragments = new ArrayList<SavedFragment>();
			mDetachedFragments = new ArrayList<FTFragment>();
			mCurrentPrimaryItem = null;
			

			Parcelable[] savedFragmentArray = bundle.getParcelableArray("fragments");
			if (savedFragmentArray != null) {
				for (int i = 0; i < savedFragmentArray.length; i++) {
					mSavedFragments.add((SavedFragment) savedFragmentArray[i]);
				}
				Log.i("NAME_TAG", "		--------> RESTORED_FRAGMENT NB : "+savedFragmentArray.length);
			} else {
				Log.i("NAME_TAG", "		--------> RESTORED_FRAGMENT NB : 0");
			}


			mBundle = bundle;
			
			int nb = mBundle.getInt("fragment-detached-nb");
			fragmentsNumb = mBundle.getInt("fragmentsNumb");
			
			for (int i = 0; i < nb; i++) {
				fragment = FTFragment.instantiate(mContext, mBundle.getString("fragment-detached-path" + i), mBundle.getBundle("fragment-detached-bundle" + i), Animation.getAnimation(mBundle.getInt("fragment-detached-animIn" + i)), Animation.getAnimation(mBundle.getInt("fragment-detached-animOut" + i)));

				if (fragment != null) {
					fragment.setMenuVisibility(false);
					fragment.setUserVisibleHint(false);
					mDetachedFragments.add(fragment);
					
					mCurrentTransaction.add(mContainerId, fragment, this.getFragmentName(i + mSavedFragments.size()));
					mCurrentTransaction.detach(fragment);
				}
			}
			
			Log.i("NAME_TAG", "		--------> RESTORED_DETACHED_FRAGMENT NB : "+nb);
			
			fragment = FTFragment.instantiate(mContext, mBundle.getString("primary-fragment-path"), mBundle.getBundle("primary-fragment-bundle"), Animation.getAnimation(mBundle.getInt("primary-fragment-animIn", 0)), Animation.getAnimation(mBundle.getInt("primary-fragment-animOut", 0)));
			
			if (fragment != null) {
				fragment.setMenuVisibility(false);
				fragment.setMenuVisibility(false);
				mCurrentPrimaryItem = fragment;
				
				mCurrentTransaction.remove(mCurrentPrimaryItem);
				mCurrentTransaction.add(mContainerId, mCurrentPrimaryItem, this.getFragmentName(fragmentsNumb));
				mIsCurrentPrimaryItemDetatch = mBundle.getBoolean("primary-fragment-type");
				
				if (mIsCurrentPrimaryItemDetatch) {
					mCurrentTransaction.detach(mCurrentPrimaryItem);
				}

				Log.i("NAME_TAG", "		--------> RESTORED_CURRENT_FRAGMENT : YES");
			} else {
				Log.i("NAME_TAG", "		--------> RESTORED_CURRENT_FRAGMENT : NO");
			}
			
			mBundle = null;
		}
		
		Log.i("NAME_TAG_METHOD", "++++++++++++ BEGIN restoreState BEGIN ++++++++++++");
	}
}
