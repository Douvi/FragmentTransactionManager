package com.dovi.fragmentTransaction.manager;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class SavedFragment implements Parcelable {

	public Bundle mBundle;
	public int mAnimIn;
	public int mAnimOut;
	public String mPath;

	public SavedFragment(String path, Bundle bundle, int animIn, int animOut) {
		this.mBundle = bundle;
		this.mAnimIn = animIn;
		this.mAnimOut = animOut;
		this.mPath = path;
	}

	public void writeToParcel(Parcel out, int flags) {
		out.writeString(mPath);
		out.writeBundle(mBundle);
		out.writeInt(mAnimIn);
		out.writeInt(mAnimOut);
		System.out.println("");
	}

	public static final Parcelable.Creator<SavedFragment> CREATOR = new Parcelable.Creator<SavedFragment>() {
		public SavedFragment createFromParcel(Parcel in) {
			return new SavedFragment(in);
		}

		public SavedFragment[] newArray(int size) {
			return new SavedFragment[size];
		}
	};

	private SavedFragment(Parcel in) {
		mPath = in.readString();
		mBundle = in.readBundle();
		mAnimIn = in.readInt();
		mAnimOut = in.readInt();
		System.out.println("");
	}

	@Override
	public int describeContents() {
		return 0;
	}
}
