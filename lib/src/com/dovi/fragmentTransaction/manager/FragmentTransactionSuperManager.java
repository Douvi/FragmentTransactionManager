package com.dovi.fragmentTransaction.manager;

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

public class FragmentTransactionSuperManager {

	private final ViewGroup mViewGroup;
	private final Context mContext;
	private FragmentManager mFragmentManager;

	private HashMap<String, FragmentTransactionAdapter> mStackTags;
	private HashMap<String, Integer> mRessouseTags;
	private HashMap<String, Integer> mDetachedNumTags;

	private FragmentTransactionAdapter mCurrentAdapter;

	private String mCurrentTag;
	private int mCurrentRes;

	private SavedStackState mStack;
	private Boolean isRestoringState;
	private FTFragment fragment;

	private Parcelable[] mRestoredStackAdapter = null;

	public FragmentTransactionSuperManager(Context context, FragmentManager fm, ViewGroup viewGroup) {
		mViewGroup = viewGroup;
		mContext = context;
		mFragmentManager = fm;
		mCurrentTag = null;
		mStackTags = new HashMap<String, FragmentTransactionAdapter>();
		mRessouseTags = new HashMap<String, Integer>();
		mDetachedNumTags = new HashMap<String, Integer>();
		isRestoringState = false;
	}

	public void createTag(String tag, int containerId, int detachFragmentLimited) {

		if (containerId <= 0) {
			throw new FragmentTransactionManagerException(
					"The containerId must be init with a ressource id - Here an exemple of what you should do :createTag(\"TagName\", R.layout.your_content)");
		}

		if (!mStackTags.containsKey(tag)) {
			mStackTags.put(tag, new FragmentTransactionAdapter(mContext, mFragmentManager, tag, containerId, detachFragmentLimited));
			mRessouseTags.put(tag, containerId);
			mDetachedNumTags.put(tag, detachFragmentLimited);
		}
	}

	public void addFragmentsInStacks(List<FragmentTransactionItem> list) {

		if (list == null) {
			throw new FragmentTransactionManagerException("the list must not be null");
		}

		for (FragmentTransactionItem fragmentTransactionItem : list) {

			detachAllFragments(fragmentTransactionItem.tag);
			getCurrentAdapter(fragmentTransactionItem.tag);

			mCurrentAdapter.setTransactionAnimation(fragmentTransactionItem.fragment.mAnimIn);
			mCurrentAdapter.add(mViewGroup, fragmentTransactionItem.fragment);
			mStackTags.put(fragmentTransactionItem.tag, mCurrentAdapter);
		}

		mCurrentAdapter.finishUpdate(mViewGroup);
	}

	public void addFragmentInStack(String tag, FTFragment fragment) {

		if (fragment == null) {
			throw new FragmentTransactionManagerException("the fragment must not be null  - Here an exemple of what you should do :");
		}

		if (!mStackTags.containsKey(tag)) {
			throw new FragmentTransactionManagerException("The tag : '" + tag
					+ "' must be init before add it a Fragment - Here an exemple of what you should do : 'createTag(String tag, int ressource)' ");
		}

		detachAllFragments(tag);
		getCurrentAdapter(tag);

		mCurrentAdapter.setTransactionAnimation(fragment.mAnimIn);
		mCurrentAdapter.add(mViewGroup, fragment);

		mCurrentAdapter.finishUpdate(mViewGroup);
		mStackTags.put(tag, mCurrentAdapter);
	}
	
	public void addFragmentAsRootInStack(String tag, FTFragment fragment) {

		if (fragment == null) {
			throw new FragmentTransactionManagerException("the fragment must not be null  - Here an exemple of what you should do :");
		}

		if (!mStackTags.containsKey(tag)) {
			throw new FragmentTransactionManagerException("The tag : '" + tag
					+ "' must be init before add it a Fragment - Here an exemple of what you should do : 'createTag(String tag, int ressource)' ");
		}

		mCurrentAdapter = mStackTags.get(tag);
		mCurrentAdapter = new FragmentTransactionAdapter(mContext, mFragmentManager, tag, mCurrentAdapter.mContainerId, mCurrentAdapter.mDetachFragmentLimited);
		mStackTags.remove(tag);
		mStackTags.put(tag, mCurrentAdapter);
		
		addFragmentInStack(tag, fragment);
	}

	public void showTopFragmentInStack(String tag) {

		if (!mStackTags.containsKey(tag)) {
			throw new FragmentTransactionManagerException("The tag : '" + tag
					+ "' must be init before add it a Fragment - Here an exemple of what you should do : 'createTag(String tag, int ressource)' ");
		}

		detachAllFragments(tag);
		getCurrentAdapter(tag);

		mCurrentAdapter.attach(mViewGroup, mCurrentAdapter.mCurrentPrimaryItem);
		mCurrentAdapter.finishUpdate(mViewGroup);
		mStackTags.put(tag, mCurrentAdapter);

	}

	private void getCurrentAdapter(String tag) {
		mCurrentAdapter = mStackTags.get(tag);
		mCurrentTag = tag;
		mCurrentRes = mCurrentAdapter.getContainerId();
	}

	private void detachAllFragments(String tag) {
		if (mCurrentTag != null) {

			int idRess = mRessouseTags.get(tag);

			Iterator<Entry<String, Integer>> it = mRessouseTags.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, Integer> pairs = it.next();

				if (tag != pairs.getKey() && idRess == pairs.getValue()) {

					mCurrentAdapter = mStackTags.get(pairs.getKey());

					if (!mCurrentAdapter.mIsCurrentPrimaryItemDetatch) {
						mCurrentAdapter.detach(mCurrentAdapter.mCurrentPrimaryItem);
						mCurrentAdapter.finishUpdate(mViewGroup);
						mStackTags.put(pairs.getKey(), mCurrentAdapter);
					}
				}
			}
		}
	}

	public void removeTopFragmentInStackWithAnimation(String tag, Boolean animation) {

		if (!mStackTags.containsKey(tag)) {
			throw new FragmentTransactionManagerException("The tag : '" + tag
					+ "' must be init before add it a Fragment - Here an exemple of what you should do : 'createTag(String tag, int ressource)' ");
		}

		if (!mCurrentTag.equals(tag)) {
			mCurrentTag = tag;

			mCurrentAdapter = mStackTags.get(tag);
			removeTopFragmentInStackWithAnimation(animation);

		} else {
			removeTopFragmentInStackWithAnimation(animation);
		}

	}

	public void removeTopFragmentInStackWithAnimation(Boolean animation) {

		if (!isStackEmpty()) {

			if (animation) {
				mCurrentAdapter.setTransactionAnimation(mCurrentAdapter.mCurrentPrimaryItem.mAnimOut);
			}

			mCurrentAdapter.removed(mCurrentAdapter.mCurrentPrimaryItem);
			fragment = (FTFragment) mCurrentAdapter.instantiateItem(mViewGroup, mCurrentAdapter.getCount() - 1);

			mCurrentAdapter.setPrimaryItem(mViewGroup, mCurrentAdapter.getCount() - 1, fragment);

			mCurrentAdapter.attach(mViewGroup, mCurrentAdapter.mCurrentPrimaryItem);
			mCurrentAdapter.finishUpdate(mViewGroup);

			mStackTags.put(mCurrentTag, mCurrentAdapter);
		}

	}

	public boolean isStackEmpty(String tag) {
		if (!mCurrentTag.equals(tag)) {
			mCurrentTag = tag;

			if (mStackTags.containsKey(tag)) {
				mCurrentAdapter = mStackTags.get(tag);
				return isStackEmpty();
			} else {
				return true;
			}
		} else {
			return isStackEmpty();
		}
	}

	public boolean isStackEmpty() {
		if (mCurrentAdapter != null) {
			return (mCurrentAdapter.getCount() -1) <= 0;
		}
		return true;
	}

	public int getCountOfFragmentsInStack(String tag) {

		if (mStackTags.containsKey(tag)) {
			return mStackTags.get(tag).getCount();
		} else {
			return 0;
		}
	}

	public void setFragmentManager(FragmentManager fm) {
		mFragmentManager = fm;

		if (mStack != null) {
			isRestoringState = true;
			this.finishInitOnRestoreInstanceState();
		}
	}

	public boolean isRestoringState() {
		return isRestoringState;
	}

	public void setRestoringState(boolean state) {
		isRestoringState = state;
	}
	
	public String getCurrentStack(){
		return mCurrentTag;
	}
	
	public String getCurrentStackNameFromContent(int ressourceId) {
		
		if (mRessouseTags.get(mCurrentTag) == ressourceId && !mCurrentAdapter.mIsCurrentPrimaryItemDetatch) {
			return mCurrentTag;
		} else {
			
			for (String tag : mRessouseTags.keySet()) {
				
				if (mRessouseTags.get(tag) == ressourceId) {
					
					getCurrentAdapter(tag);
					if (!mCurrentAdapter.mIsCurrentPrimaryItemDetatch) {
						return mCurrentTag;
					}
				}
			}
			
			return "";
		}
	}

	public Parcelable onSaveInstanceState(Parcelable parcelable) {
		mStack = new SavedStackState(parcelable);

		mStack.currentTag = mCurrentTag;

		mStack.stackTags = new String[mStackTags.size()];
		int i = 0;
		for (String key : mStackTags.keySet()) {
			mStack.stackTags[i] = key;
			i++;
		}

		mStack.stackAdapter = new Parcelable[mStackTags.size()];
		i = 0;
		for (FragmentTransactionAdapter adapter : mStackTags.values()) {
			adapter.getCurrentTransaction();
			mStack.stackAdapter[i] = adapter.saveState();
			adapter.finishUpdate(mViewGroup);
			i++;
		}

		mStack.stackRessourses = new int[mRessouseTags.size()];
		i = 0;
		for (int res : mRessouseTags.values()) {
			mStack.stackRessourses[i] = res;
			i++;
		}

		mStack.stackDetached = new int[mDetachedNumTags.size()];
		i = 0;
		for (int nb : mDetachedNumTags.values()) {
			mStack.stackDetached[i] = nb;
			i++;
		}

		return mStack;
	}

	public Parcelable onRestoreInstanceState(Parcelable state) {

		if (!(state instanceof SavedStackState)) {
			// super.onRestoreInstanceState(state);
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
		mRessouseTags = new HashMap<String, Integer>();
		mDetachedNumTags = new HashMap<String, Integer>();
		
		for (int i = 0; i < mStack.stackTags.length; i++) {

			mCurrentTag = mStack.stackTags[i];
			mCurrentRes = mStack.stackRessourses[i];
			numDetach = mStack.stackDetached[i];
			
			mRessouseTags.put(mCurrentTag, mCurrentRes);
			mDetachedNumTags.put(mCurrentTag, numDetach);

			mCurrentAdapter = new FragmentTransactionAdapter(mContext, mFragmentManager, mCurrentTag, mCurrentRes, numDetach);
			mCurrentAdapter.getCurrentTransaction();
			mCurrentAdapter.restoreState(mRestoredStackAdapter[i], SavedStackState.class.getClassLoader());
			mStackTags.put(mCurrentTag, mCurrentAdapter);
			mCurrentAdapter.finishUpdate(mViewGroup);
		}

		mStack = null;
	}

	public static class SavedStackState extends BaseSavedState {
		Parcelable[] stackAdapter;
		String[] stackTags;
		int[] stackRessourses;
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
			out.writeInt(stackRessourses.length);
			out.writeIntArray(stackRessourses);
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
			this.stackRessourses = new int[in.readInt()];
			in.readIntArray(stackRessourses);
			this.stackDetached = new int[in.readInt()];
			in.readIntArray(stackDetached);
		}
	}
}
