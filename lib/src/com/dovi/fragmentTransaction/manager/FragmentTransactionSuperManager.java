package com.dovi.fragmentTransaction.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.view.View.BaseSavedState;
import android.view.ViewGroup;

import com.dovi.fragmentTransaction.FTFragment;
import com.dovi.fragmentTransaction.exception.FragmentTransactionManagerException;
import com.dovi.fragmentTransaction.manager.FragmentTransactionAdapter.Animation;

public class FragmentTransactionSuperManager {

	private final ViewGroup mViewGroup;
	private final Context mContext;
	private FragmentManager mFragmentManager;

	private HashMap<String, FragmentTransactionAdapter> mStackTags;
	private HashMap<String, Integer> mResouceTags;
	private HashMap<String, Integer> mDetachedNumTags;

	private FragmentTransactionAdapter mCurrentAdapter;

	private String mCurrentTag;
	private int mCurrentRes;

	private SavedStackState mStack;
	private FTFragment fragment;

	private Parcelable[] mRestoredStackAdapter = null;

	public FragmentTransactionSuperManager(Context context, FragmentManager fm, ViewGroup viewGroup) {
		mViewGroup = viewGroup;
		mContext = context;
		mFragmentManager = fm;
		mCurrentTag = null;
		mCurrentRes = 0;
		mStackTags = new HashMap<String, FragmentTransactionAdapter>();
		mResouceTags = new HashMap<String, Integer>();
		mDetachedNumTags = new HashMap<String, Integer>();
	}

	public void setFragmentManager(FragmentManager fm) {
		mFragmentManager = fm;

		if (mStack != null) {
			this.finishInitOnRestoreInstanceState();
		}
	}
	
	public void setChildFragmentManager(String tag, FragmentManager fm) {
		
		isTagIsOK(tag);
		getCurrentAdapter(tag);
		
		if (fm != null) {
			mCurrentAdapter.mFragmentManager = fm;
			mStackTags.put(tag, mCurrentAdapter);
		}
		
	}

	public boolean isContainTag(String tag) {

		if (tag != null && tag.length() > 0) {
			return mStackTags.containsKey(tag);
		}

		return false;
	}

	public void createTag(String tag, int containerId, int detachFragmentLimited) {

		isContainerIdOK(containerId);
		isTagIsOK(tag, false);

		if (!mStackTags.containsKey(tag)) {
			mStackTags.put(tag, new FragmentTransactionAdapter(mContext, mFragmentManager, tag, containerId, detachFragmentLimited));
			mResouceTags.put(tag, containerId);
			mDetachedNumTags.put(tag, detachFragmentLimited);
		}
	}

	public void addFragmentInStack(String tag, FTFragment fragment) {

		isFragmentIsOK(fragment);
		isTagIsOK(tag);

		detachAllFragments(tag);

		getCurrentAdapter(tag);
		mCurrentAdapter.setTransactionAnimation(fragment.mAnimIn);
		mCurrentAdapter.add(mViewGroup, fragment);

		mCurrentAdapter.finishUpdate(mViewGroup);
		mStackTags.put(tag, mCurrentAdapter);
	}

	public void addFragmentAsRootInStack(String tag, FTFragment fragment) {

		isFragmentIsOK(fragment);
		isTagIsOK(tag);

		getCurrentAdapter(tag);

		mCurrentAdapter = new FragmentTransactionAdapter(mContext, mFragmentManager, tag, mCurrentAdapter.mContainerId, mCurrentAdapter.mDetachFragmentLimited);
		mStackTags.remove(tag);
		mStackTags.put(tag, mCurrentAdapter);

		addFragmentInStack(tag, fragment);
	}

	public void showTopFragmentInStack(String tag) {

		isTagIsOK(tag);

		detachAllFragments(tag);

		getCurrentAdapter(tag);

		mCurrentAdapter.attach(mViewGroup, mCurrentAdapter.mCurrentPrimaryItem);
		mCurrentAdapter.finishUpdate(mViewGroup);
		mStackTags.put(tag, mCurrentAdapter);

	}

	public void removeTopFragmentInStackWithAnimation(String tag, Boolean animation) {

		isTagIsOK(tag);

		getCurrentAdapter(tag);

		removeTopFragmentInStackWithAnimation(animation);

	}

	public void returnToFragmentAtPositionInStackWithAnimation(String tag, int position, Boolean animation) {

		isTagIsOK(tag);

		getCurrentAdapter(tag);
		
		position = position < 0 ? 0 : position; 
		
		if (position >= 0 && position <= mCurrentAdapter.getCount()) {
			mCurrentAdapter.removed(mCurrentAdapter.mCurrentPrimaryItem);
			
			fragment = null;
			while (mCurrentAdapter.getCount() > position && mCurrentAdapter.getCount() > 0) {
				
				if (fragment != null) {
					mCurrentAdapter.removed(fragment);
				}
				
				fragment = (FTFragment) mCurrentAdapter.instantiateItem(mViewGroup, mCurrentAdapter.getCount());
				
			}
			
			if (fragment != null) {
				mCurrentAdapter.setPrimaryItem(mViewGroup, mCurrentAdapter.getCount(), fragment);

				mCurrentAdapter.attach(mViewGroup, mCurrentAdapter.mCurrentPrimaryItem);
			}
			
			mCurrentAdapter.finishUpdate(mViewGroup);
			mStackTags.put(mCurrentTag, mCurrentAdapter);
		}
	}

	public void returnToRootFragmentInStackWithAnimation(String tag, Boolean animation) {

		isTagIsOK(tag);

		getCurrentAdapter(tag);

		if (mCurrentAdapter.mSavedFragments.size() > 0) {

			SavedFragment mSavedFragment = mCurrentAdapter.mSavedFragments.get(0);
			mCurrentAdapter.mSavedFragments = new ArrayList<SavedFragment>();
			mCurrentAdapter.mSavedFragments.add(mSavedFragment);

			mCurrentAdapter.mDetachedFragments = new ArrayList<FTFragment>();
			mCurrentAdapter.fragmentsNumb = 1;

		} else if (mCurrentAdapter.mDetachedFragments.size() > 0) {

			FTFragment mFTFragment = mCurrentAdapter.mDetachedFragments.get(0);
			mCurrentAdapter.mDetachedFragments = new ArrayList<FTFragment>();
			mCurrentAdapter.mDetachedFragments.add(mFTFragment);
			mCurrentAdapter.fragmentsNumb = 1;
		}

		removeTopFragmentInStackWithAnimation(animation);
	}

	public List<SavedFragment> getListOfFragmentsInStack(String tag) {

		isTagIsOK(tag);

		getCurrentAdapter(tag);

		List<SavedFragment> mFragments = new ArrayList<SavedFragment>();

		if (mCurrentAdapter.mSavedFragments.size() > 0) {
			mFragments = new ArrayList<SavedFragment>(mCurrentAdapter.mSavedFragments);
		}

		FTFragment mFTFragment;

		for (int i = 0; i < mCurrentAdapter.mDetachedFragments.size(); i++) {

			mFTFragment = mCurrentAdapter.mDetachedFragments.get(i);
			mFragments.add(new SavedFragment(mFTFragment.getClass().getName(), mFTFragment.mExtraOutState, mFTFragment.mAnimIn.getAnimation(),
					mFTFragment.mAnimOut.getAnimation()));

		}

		if (mCurrentAdapter.mCurrentPrimaryItem != null && mCurrentAdapter.mCurrentPrimaryItem.isDetached()) {

			mCurrentAdapter.mCurrentPrimaryItem.onSaveInstanceState(mCurrentAdapter.mCurrentPrimaryItem.mExtraOutState);
			mFragments.add(new SavedFragment(mCurrentAdapter.mCurrentPrimaryItem.getClass().getName(), mCurrentAdapter.mCurrentPrimaryItem.mExtraOutState,
					mCurrentAdapter.mCurrentPrimaryItem.mAnimIn.getAnimation(), mCurrentAdapter.mCurrentPrimaryItem.mAnimOut.getAnimation()));

		}

		return mFragments;
	}

	public void setListOfFragmentsInStack(String tag, List<SavedFragment> fragments, boolean replacePrimaryFragmentIsDetach) {

		isTagIsOK(tag);

		getCurrentAdapter(tag);

		mCurrentAdapter.mSavedFragments = new ArrayList<SavedFragment>();
		mCurrentAdapter.mDetachedFragments = new ArrayList<FTFragment>();
		
		FTFragment mFragment = null;
		SavedFragment mSavedFragment = null;

		if (mCurrentAdapter.mCurrentPrimaryItem != null && mCurrentAdapter.mCurrentPrimaryItem.isDetached() && replacePrimaryFragmentIsDetach) {

			mCurrentAdapter.fragmentsNumb = fragments.size();
			
			for (int i = 0; i < fragments.size(); i++) {

				if (i == fragments.size() - 1) {

					mSavedFragment = fragments.get(i);
					mFragment = FTFragment.instantiate(mContext, mSavedFragment.mPath, mSavedFragment.mBundle, Animation.getAnimation(mSavedFragment.mAnimIn),
							Animation.getAnimation(mSavedFragment.mAnimOut));
					mFragment.setMenuVisibility(false);
					mFragment.setUserVisibleHint(false);

					mCurrentAdapter.getCurrentTransaction();
					mCurrentAdapter.mCurrentTransaction.add(mCurrentAdapter.mContainerId, mFragment, mCurrentAdapter.getFragmentName(i));
					mCurrentAdapter.mCurrentTransaction.detach(mFragment);
					mCurrentAdapter.mCurrentPrimaryItem = mFragment;
					mCurrentAdapter.mIsCurrentPrimaryItemDetatch = true;
					mCurrentAdapter.finishUpdate(mViewGroup);

				} else if (i >= ((fragments.size() - mCurrentAdapter.mDetachFragmentLimited) - 1)) {
					mSavedFragment = fragments.get(i);
					mFragment = FTFragment.instantiate(mContext, mSavedFragment.mPath, mSavedFragment.mBundle, Animation.getAnimation(mSavedFragment.mAnimIn),
							Animation.getAnimation(mSavedFragment.mAnimOut));
					mFragment.setMenuVisibility(false);
					mFragment.setUserVisibleHint(false);

					mCurrentAdapter.getCurrentTransaction();
					mCurrentAdapter.mCurrentTransaction.add(mCurrentAdapter.mContainerId, mFragment, mCurrentAdapter.getFragmentName(i));
					mCurrentAdapter.mCurrentTransaction.detach(mFragment);
					mCurrentAdapter.finishUpdate(mViewGroup);
					mCurrentAdapter.mDetachedFragments.add(mFragment);

				} else {
					mCurrentAdapter.mSavedFragments.add(fragments.get(i));
				}
			}

		} else {

			mCurrentAdapter.fragmentsNumb = fragments.size();
			
			for (int i = 0; i < fragments.size(); i++) {

				if (i >= (fragments.size() - mCurrentAdapter.mDetachFragmentLimited)) {
					mSavedFragment = fragments.get(i);
					mFragment = FTFragment.instantiate(mContext, mSavedFragment.mPath, mSavedFragment.mBundle, Animation.getAnimation(mSavedFragment.mAnimIn),
							Animation.getAnimation(mSavedFragment.mAnimOut));
					mFragment.setMenuVisibility(false);
					mFragment.setUserVisibleHint(false);

					mCurrentAdapter.getCurrentTransaction();
					mCurrentAdapter.mCurrentTransaction.add(mCurrentAdapter.mContainerId, mFragment, mCurrentAdapter.getFragmentName(i));
					mCurrentAdapter.mCurrentTransaction.detach(mFragment);
					mCurrentAdapter.finishUpdate(mViewGroup);
					mCurrentAdapter.mDetachedFragments.add(mFragment);

				} else {
					mCurrentAdapter.mSavedFragments.add(fragments.get(i));
				}
			}
		}
	}

	private void removeTopFragmentInStackWithAnimation(Boolean animation) {

		if (!isStackEmpty()) {

			if (animation) {
				mCurrentAdapter.setTransactionAnimation(mCurrentAdapter.mCurrentPrimaryItem.mAnimOut);
			}

			mCurrentAdapter.removed(mCurrentAdapter.mCurrentPrimaryItem);
			fragment = (FTFragment) mCurrentAdapter.instantiateItem(mViewGroup, mCurrentAdapter.getCount());

			mCurrentAdapter.setPrimaryItem(mViewGroup, mCurrentAdapter.getCount(), fragment);

			mCurrentAdapter.attach(mViewGroup, mCurrentAdapter.mCurrentPrimaryItem);
			mCurrentAdapter.finishUpdate(mViewGroup);

			mStackTags.put(mCurrentTag, mCurrentAdapter);
		}

	}

	public boolean isStackEmpty(String tag) {

		isTagIsOK(tag);

		getCurrentAdapter(tag);

		return isStackEmpty();
	}

	private boolean isStackEmpty() {

		if (mCurrentAdapter != null) {
			return (mCurrentAdapter.getCount()) <= 0;
		}
		return true;
	}

	public int getCountOfFragmentsInStack(String tag) {

		if (tag != null && tag.length() > 0 && mStackTags.containsKey(tag)) {
			return mStackTags.get(tag).getCount() + 1;
		} else {
			return 0;
		}
	}

	public String getCurrentStackNameFromContent(int resourceId) {

		if (mResouceTags.get(mCurrentTag) == resourceId && !mCurrentAdapter.mIsCurrentPrimaryItemDetatch) {
			return mCurrentTag;
		} else {

			for (String tag : mResouceTags.keySet()) {

				if (mResouceTags.get(tag) == resourceId) {

					getCurrentAdapter(tag);
					if (!mCurrentAdapter.mIsCurrentPrimaryItemDetatch) {
						return mCurrentTag;
					}
				}
			}

			return null;
		}
	}

	public Parcelable onSaveInstanceState(Parcelable parcelable) {
		mStack = new SavedStackState(parcelable);

		mStack.currentTag = mCurrentTag;

		mStack.stackTags = new String[mStackTags.size()];
		mStack.stackAdapter = new Parcelable[mStackTags.size()];
		mStack.stackResources = new int[mStackTags.size()];
		mStack.stackDetached = new int[mStackTags.size()];

		int i = 0;
		
		for (Entry<String, FragmentTransactionAdapter> entry : mStackTags.entrySet()) {
			String tag = entry.getKey();
			mCurrentAdapter = entry.getValue();
			mCurrentAdapter.getCurrentTransaction();

			mStack.stackTags[i] = tag;
			mStack.stackAdapter[i] = mCurrentAdapter.saveState();
			mStack.stackResources[i] = mResouceTags.get(tag);
			mStack.stackDetached[i] = mDetachedNumTags.get(tag);

			mCurrentAdapter.finishUpdate(mViewGroup);

			i++;
		}

		return mStack;
	}

	public Parcelable onRestoreInstanceState(Parcelable state) {

		if (!(state instanceof SavedStackState)) {
			return state;
		}

		mStack = (SavedStackState) state;

		if (mFragmentManager != null) {
			this.finishInitOnRestoreInstanceState();
		}

		return ((SavedStackState) state).getSuperState();
	}

	public void finishInitOnRestoreInstanceState() {
		int numDetach;
		mCurrentTag = mStack.currentTag;
		mRestoredStackAdapter = mStack.stackAdapter;

		mStackTags = new HashMap<String, FragmentTransactionAdapter>();
		mResouceTags = new HashMap<String, Integer>();
		mDetachedNumTags = new HashMap<String, Integer>();

		for (int i = 0; i < mStack.stackTags.length; i++) {

			mCurrentTag = mStack.stackTags[i];
			mCurrentRes = mStack.stackResources[i];
			numDetach = mStack.stackDetached[i];

			mResouceTags.put(mCurrentTag, mCurrentRes);
			mDetachedNumTags.put(mCurrentTag, numDetach);

			mCurrentAdapter = new FragmentTransactionAdapter(mContext, mFragmentManager, mCurrentTag, mCurrentRes, numDetach);
			mCurrentAdapter.getCurrentTransaction();
			mCurrentAdapter.restoreState(mRestoredStackAdapter[i], SavedStackState.class.getClassLoader());
			mCurrentAdapter.finishUpdate(mViewGroup);
			mStackTags.put(mCurrentTag, mCurrentAdapter);

		}

		mStack = null;
	}

	private void isContainerIdOK(int containerId) {
		if (containerId <= 0) {
			throw new FragmentTransactionManagerException("The containerId if must be init with a resource id");
		}
	}

	private void isTagIsOK(String tag) {
		this.isTagIsOK(tag, true);
	}

	private void isTagIsOK(String tag, boolean isOnList) {
		if (tag == null || tag.length() <= 0) {
			throw new FragmentTransactionManagerException("The tag must not be set to null or empty.");
		}

		if (isOnList) {
			if (!mStackTags.containsKey(tag)) {
				throw new FragmentTransactionManagerException("The tag : '" + tag + "' must be init before into FragmentTrasactionManager");
			}
		}
	}

	private void isFragmentIsOK(FTFragment fragment) {

		if (fragment == null) {
			throw new FragmentTransactionManagerException("the fragment must not be null");
		}

	}

	private void getCurrentAdapter(String tag) {
		mCurrentAdapter = mStackTags.get(tag);
		mCurrentTag = tag;
		mCurrentRes = mCurrentAdapter.getContainerId();
	}

	private void detachAllFragments(String tag) {
		if (mResouceTags != null && mResouceTags.containsKey(tag)) {

			int idRess = mResouceTags.get(tag);

			Iterator<Entry<String, Integer>> it = mResouceTags.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, Integer> pairs = it.next();

				if (tag != pairs.getKey() && idRess == pairs.getValue()) {

					mCurrentAdapter = mStackTags.get(pairs.getKey());

					if (mCurrentAdapter.mCurrentPrimaryItem != null && !mCurrentAdapter.mIsCurrentPrimaryItemDetatch) {
						mCurrentAdapter.detach(mCurrentAdapter.mCurrentPrimaryItem);
						mCurrentAdapter.finishUpdate(mViewGroup);
						mStackTags.put(pairs.getKey(), mCurrentAdapter);
					}
				}
			}
		}
	}

	public static class SavedStackState extends BaseSavedState {
		Parcelable[] stackAdapter;
		String[] stackTags;
		int[] stackResources;
		int[] stackDetached;
		String currentTag;

		public SavedStackState(Parcelable superState) {
			super(superState);
		}

		@Override
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			out.writeString(currentTag);
			out.writeParcelableArray(stackAdapter, flags);
			out.writeInt(stackTags.length);
			out.writeStringArray(stackTags);
			out.writeInt(stackResources.length);
			out.writeIntArray(stackResources);
			out.writeInt(stackDetached.length);
			out.writeIntArray(stackDetached);
		}

		public static final Parcelable.Creator<SavedStackState> CREATOR = ParcelableCompat.newCreator(new ParcelableCompatCreatorCallbacks<SavedStackState>() {
			@Override
			public SavedStackState createFromParcel(Parcel in, ClassLoader loader) {
				return new SavedStackState(in, loader);
			}

			@Override
			public SavedStackState[] newArray(int size) {
				return new SavedStackState[size];
			}
		});

		SavedStackState(Parcel in, ClassLoader loader) {
			super(in);
			if (loader == null) {
				loader = getClass().getClassLoader();
			}

			this.currentTag = in.readString();
			this.stackAdapter = in.readParcelableArray(loader);
			this.stackTags = new String[in.readInt()];
			in.readStringArray(this.stackTags);
			this.stackResources = new int[in.readInt()];
			in.readIntArray(stackResources);
			this.stackDetached = new int[in.readInt()];
			in.readIntArray(stackDetached);
		}
	}
}
