package com.dovi.fragmentTransaction.manager;

import java.util.List;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;

import com.dovi.fragmentTransaction.FTFragment;

public class FragmentTransactionManager {

	public FragmentTransactionSuperManager mFragmentsManager = null;
	
	public FragmentTransactionManager(Context context, FragmentManager fm, ViewGroup viewGroup) {
		mFragmentsManager = new FragmentTransactionSuperManager(context, fm, viewGroup);
	}
	
	public void setFragmentManager(FragmentManager fm){
		mFragmentsManager.setFragmentManager(fm);
	}
	
	public boolean isContainTag(String tag) {
		return mFragmentsManager.isContainTag(tag);
	}
	
	public void createTag(String tag, int containerId, int detachFragmentLimited) {
		mFragmentsManager.createTag(tag, containerId, detachFragmentLimited);
	}
	
	public void addFragmentInStack(String tag, FTFragment fragment) {
		mFragmentsManager.addFragmentInStack(tag, fragment);
	}
	
	public void addFragmentAsRootInStack(String tag, FTFragment fragment) {
		mFragmentsManager.addFragmentAsRootInStack(tag, fragment);
	}
	
	public void showTopFragmentInStack(String tag) {
		mFragmentsManager.showTopFragmentInStack(tag);
	}
	
	public int getCountOfFragmentsInStack(String tag){
		return mFragmentsManager.getCountOfFragmentsInStack(tag);
	}
	
	public void removeTopFragmentInStackWithAnimation(String tag, Boolean animation) {
		mFragmentsManager.removeTopFragmentInStackWithAnimation(tag, animation);
	}
	
	public void returnToRootFragmentInStackWithAnimation(String tag, Boolean animation) {
		mFragmentsManager.returnToRootFragmentInStackWithAnimation(tag, animation);
	}
	
	public List<SavedFragment> getListOfFragmentsInStack(String tag){
		return mFragmentsManager.getListOfFragmentsInStack(tag);
	}
	
	public boolean isStackEmpty(String tag) {
		return mFragmentsManager.isStackEmpty(tag);
	}
	
	public String getCurrentStackName(){
		return mFragmentsManager.getCurrentStack();
	}
	
	public String getCurrentStackNameFromContent(int ressourceId) {
		return mFragmentsManager.getCurrentStackNameFromContent(ressourceId);
	}
}
