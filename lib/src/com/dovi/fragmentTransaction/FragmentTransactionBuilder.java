package com.dovi.fragmentTransaction;

import java.util.List;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;

public class FragmentTransactionBuilder {

	public FragmentTransactionManager mFragmentsStack = null;
	
	public FragmentTransactionBuilder(Context context, FragmentManager fm, ViewGroup viewGroup) {
		mFragmentsStack = new FragmentTransactionManager(context, fm, viewGroup);
	}
	
	public void setFragmentManager(FragmentManager fm){
		mFragmentsStack.setFragmentManager(fm);
	}
	
	public boolean isNeededToRestoreState() {
		return mFragmentsStack.isRestoringState();
	}
	
	public void restoreState(){
		mFragmentsStack.setRestoringState(false);
	}
	
	public void createTag(String tag, int containerId, int detachFragmentLimited) {
		mFragmentsStack.createTag(tag, containerId, detachFragmentLimited);
	}
	
	public void addFragmentsInStacks(List<FragmentTransactionItem> list) {
		mFragmentsStack.addFragmentsInStacks(list);
	}
	
	public void addFragmentInStack(String tag, FTFragment fragment) {
		mFragmentsStack.addFragmentInStack(tag, fragment);
	}
	
	public void showTopFragmentInStack(String tag) {
		mFragmentsStack.showTopFragmentInStack(tag);
	}
	
	public int getCountOfFragmentsInStack(String tag){
		return mFragmentsStack.getCountOfFragmentsInStack(tag);
	}
	
	public void removeTopFragmentInStackWithAnimation(String tag, Boolean animation) {
		mFragmentsStack.removeTopFragmentInStackWithAnimation(tag, animation);
	}
	
	public void removeTopFragmentInStackWithAnimation(Boolean animation) {
		mFragmentsStack.removeTopFragmentInStackWithAnimation(animation);
	}
	
	public boolean isStackEmpty(String tag) {
		return mFragmentsStack.isStackEmpty(tag);
	}
	
	public boolean isStackEmpty() {
		return mFragmentsStack.isStackEmpty();
	}
	
	public String getCurrentStackName(){
		return mFragmentsStack.getCurrentStack();
	}
}
