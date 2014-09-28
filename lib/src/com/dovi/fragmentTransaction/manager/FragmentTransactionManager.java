package com.dovi.fragmentTransaction.manager;

import java.util.List;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;

import com.dovi.fragmentTransaction.FTFragment;
import com.dovi.fragmentTransaction.layout.FTLinearLayout;
import com.dovi.fragmentTransaction.layout.FTRelativeLayout;

public class FragmentTransactionManager {

	public FragmentTransactionSuperManager mFMSuper = null;
	
	/**
	 * To initiate FragmentTransactionManager, this method should be invoked only into a Class which extends ViewGroup or its children.
	 * For more details check out {@link FTLinearLayout} or {@link FTRelativeLayout}.
	 * 
	 * @param context
	 * @param fm
	 * @param viewGroup
	 */
	public FragmentTransactionManager(Context context, FragmentManager fragmentManager, ViewGroup viewGroup) {
		mFMSuper = new FragmentTransactionSuperManager(context, fragmentManager, viewGroup);
	}
	
	/**
	 * To re-initiate FragmentTransactionManager, this method should be invoked only into a Class which extends ViewGroup or its children,
	 * when your ViewGroup is reloading.
	 * 
	 * <pre>
	 * {@code
	 * protected void onRestoreInstanceState(Parcelable state) {
	 * 
	 * 	fragmentManger = new FragmentTransactionManager(getContext(), null, null);
	 * 	super.onRestoreInstanceState(fragmentManger.mFragmentsManager.onRestoreInstanceState(state));
	 * 
	 * }
	 * 
	 * public FragmentTransactionManager getFragmentManger(FragmentManager mFragmentManager, OnSaveFragmentTransaction lister) {
	 * 
	 * 	if (fragmentManger == null) {
	 * 		fragmentManger = new FragmentTransactionManager(getContext(), mFragmentManager, this);
	 * 	} else {
	 * 		fragmentManger.setFragmentManager(mFragmentManager);
	 * 	}
	 * 	
	 * 	if (mListener == null) {
	 * 		mListener = listener;
	 * 	}
	 * 
	 * 	return fragmentManger;
	 * 
	 * }
	 * </pre>
	 * For more details check out {@link FTLinearLayout} or {@link FTRelativeLayout}.
	 * 
	 * @param fragmentManager
	 */
	public void setFragmentManager(FragmentManager fragmentManager){
		mFMSuper.setFragmentManager(fragmentManager);
	}
	
	/**
	 * Is to check if tag exit into  FragmentTransactionManager
	 * 
	 * @param tag - Name of the stack
	 * @return true is the tag exist otherwise false
	 */
	public boolean isContainTag(String tag) {
		return mFMSuper.isContainTag(tag);
	}
	
	/**
	 * To create a new stack of Fragments with a tag and resource to show your Fragments.
	 * 
	 * Each stack are created with two lists and one current fragment. 
	 * 	- List with all destroyed Fragments, meaning before destroyed the fragments, FragmentTrasactionManager will call 'onSaveInstanceState(Bundle outState)' 
	 * to get the last instance of the Fragment. When it will re-init the Fragment, all informations (State Bundle) will be set into 'getArguments()' and 'mExtraOutState' of your Fragment.
	 * 	- List with all detached Fragments, meaning before detach the fragment, FragmentTrasactionManager will call 'onSaveInstanceState(Bundle outState)' 
	 * to get the last instance of the Fragment. When it will attach the Fragment to the UI, all information (State Bundle) will be set into 'mExtraOutState' and NOT 'getArguments()' of your Fragment. 
	 * !!! WARNING 'getArguments()' WILL NOT BE SET WILL THE LAST INFO. !!!!
	 *  - Current fragment is the last Fragment added into the tag, it could be Attach or Detach to the UI
	 * 
	 * @param tag - Name of the stack
	 * @param containerId - In which resource you want to show the Fragment 
	 * @param detachFragmentLimited - Set number of fragments you allow to be detached form the UI before be save into a list. 
	 */
	public void createTag(String tag, int containerId, int detachFragmentLimited) {
		mFMSuper.createTag(tag, containerId, detachFragmentLimited);
	}
	
	/**
	 * Add a new Fragment into the stack and show to UI
	 * 
	 * Each stack are created with two lists and one current fragment. 
	 * 	- List with all destroyed Fragments, meaning before destroyed the fragments, FragmentTrasactionManager will call 'onSaveInstanceState(Bundle outState)' 
	 * to get the last instance of the Fragment. When it will re-init the Fragment, all informations (State Bundle) will be set into 'getArguments()' and 'mExtraOutState' of your Fragment.
	 * 	- List with all detached Fragments, meaning before detach the fragment, FragmentTrasactionManager will call 'onSaveInstanceState(Bundle outState)' 
	 * to get the last instance of the Fragment. When it will attach the Fragment to the UI, all information (State Bundle) will be set into 'mExtraOutState' and NOT 'getArguments()' of your Fragment. 
	 * !!! WARNING 'getArguments()' WILL NOT BE SET WILL THE LAST INFO. !!!!
	 *  - Current fragment is the last Fragment added into the tag, it could be Attach or Detach to the UI
	 * 
	 * 
	 * @param tag - Name of the stack
	 * @param fragment - Fragment will be added into the stack. Your fragment have to extant FTFragment
	 */
	public void addFragmentInStack(String tag, FTFragment fragment) {
		mFMSuper.addFragmentInStack(tag, fragment);
	}
	
	/**
	 * Clean all lists and add a new Fragment into the stack and show to UI
	 * 
	 * Each stack are created with two lists and one current fragment. 
	 * 	- List with all destroyed Fragments, meaning before destroyed the fragments, FragmentTrasactionManager will call 'onSaveInstanceState(Bundle outState)' 
	 * to get the last instance of the Fragment. When it will re-init the Fragment, all informations (State Bundle) will be set into 'getArguments()' and 'mExtraOutState' of your Fragment.
	 * 	- List with all detached Fragments, meaning before detach the fragment, FragmentTrasactionManager will call 'onSaveInstanceState(Bundle outState)' 
	 * to get the last instance of the Fragment. When it will attach the Fragment to the UI, all information (State Bundle) will be set into 'mExtraOutState' and NOT 'getArguments()' of your Fragment. 
	 * !!! WARNING 'getArguments()' WILL NOT BE SET WILL THE LAST INFO. !!!!
	 *  - Current fragment is the last Fragment added into the tag, it could be Attach or Detach to the UI
	 * 
	 * 
	 * @param tag - Name of the stack
	 * @param fragment - Fragment will be added into the stack. Your fragment have to extant FTFragment
	 */
	public void addFragmentAsRootInStack(String tag, FTFragment fragment) {
		mFMSuper.addFragmentAsRootInStack(tag, fragment);
	}
	
	/**
	 * Show the last Fragment of the stack to UI
	 * 
	 * Each stack are created with two lists and one current fragment. 
	 * 	- List with all destroyed Fragments, meaning before destroyed the fragments, FragmentTrasactionManager will call 'onSaveInstanceState(Bundle outState)' 
	 * to get the last instance of the Fragment. When it will re-init the Fragment, all informations (State Bundle) will be set into 'getArguments()' and 'mExtraOutState' of your Fragment.
	 * 	- List with all detached Fragments, meaning before detach the fragment, FragmentTrasactionManager will call 'onSaveInstanceState(Bundle outState)' 
	 * to get the last instance of the Fragment. When it will attach the Fragment to the UI, all information (State Bundle) will be set into 'mExtraOutState' and NOT 'getArguments()' of your Fragment. 
	 * !!! WARNING 'getArguments()' WILL NOT BE SET WILL THE LAST INFO. !!!!
	 *  - Current fragment is the last Fragment added into the tag, it could be Attach or Detach to the UI
	 * 
	 * 
	 * @param tag - Name of the stack
	 */
	public void showTopFragmentInStack(String tag) {
		mFMSuper.showTopFragmentInStack(tag);
	}
	
	/**
	 * Give the total of Fragments into the stack (number of Fragment into the 2 lists + Current Fragment).
	 * 
	 * @param tag - Name of the stack
	 * @return if nothing is set into Stack or do not exit, it will give back 0
	 */
	public int getCountOfFragmentsInStack(String tag){
		return mFMSuper.getCountOfFragmentsInStack(tag);
	}
	
	/**
	 * Check is the stack is Empty
	 * 
	 * @param tag - Name of the stack
	 * @return if nothing is set into Stack or do not exit, it will give back true
	 */
	public boolean isStackEmpty(String tag) {
		return mFMSuper.isStackEmpty(tag);
	}
	
	/**
	 * It will remove the current Fragment and init the Fragment, from the Detach's list or if it's empty from Saved's list, and show to the UI
	 * 
	 * Each stack are created with two lists and one current fragment. 
	 * 	- List with all destroyed Fragments, meaning before destroyed the fragments, FragmentTrasactionManager will call 'onSaveInstanceState(Bundle outState)' 
	 * to get the last instance of the Fragment. When it will re-init the Fragment, all informations (State Bundle) will be set into 'getArguments()' and 'mExtraOutState' of your Fragment.
	 * 	- List with all detached Fragments, meaning before detach the fragment, FragmentTrasactionManager will call 'onSaveInstanceState(Bundle outState)' 
	 * to get the last instance of the Fragment. When it will attach the Fragment to the UI, all information (State Bundle) will be set into 'mExtraOutState' and NOT 'getArguments()' of your Fragment. 
	 * !!! WARNING 'getArguments()' WILL NOT BE SET WILL THE LAST INFO. !!!!
	 *  - Current fragment is the last Fragment added into the tag, it could be Attach or Detach to the UI
	 * 
	 * 
	 * @param tag - Name of the stack
	 * @param animation - Allows or not animation
	 */
	public void removeTopFragmentInStackWithAnimation(String tag, Boolean animation) {
		mFMSuper.removeTopFragmentInStackWithAnimation(tag, animation);
	}
	
	/**
	 * It will return at the fragment position you set and show to the UI.
	 * 
	 * Each stack are created with two lists and one current fragment. 
	 * 	- List with all destroyed Fragments, meaning before destroyed the fragments, FragmentTrasactionManager will call 'onSaveInstanceState(Bundle outState)' 
	 * to get the last instance of the Fragment. When it will re-init the Fragment, all informations (State Bundle) will be set into 'getArguments()' and 'mExtraOutState' of your Fragment.
	 * 	- List with all detached Fragments, meaning before detach the fragment, FragmentTrasactionManager will call 'onSaveInstanceState(Bundle outState)' 
	 * to get the last instance of the Fragment. When it will attach the Fragment to the UI, all information (State Bundle) will be set into 'mExtraOutState' and NOT 'getArguments()' of your Fragment. 
	 * !!! WARNING 'getArguments()' WILL NOT BE SET WILL THE LAST INFO. !!!!
	 *  - Current fragment is the last Fragment added into the tag, it could be Attach or Detach to the UI
	 * 
	 * 
	 * @param tag - Name of the stack
	 * @param position - Position of the fragment you want to go back.
	 * @param animation - Allows or not animation
	 */
	public void returnToFragmentAtPositionInStackWithAnimation(String tag, int position, Boolean animation) {
		mFMSuper.returnToFragmentAtPositionInStackWithAnimation(tag, position, animation);
	}
	
	/**
	 * Return at the first Fragment of the stack and show to the UI.
	 * 
	 * Each stack are created with two lists and one current fragment. 
	 * 	- List with all destroyed Fragments, meaning before destroyed the fragments, FragmentTrasactionManager will call 'onSaveInstanceState(Bundle outState)' 
	 * to get the last instance of the Fragment. When it will re-init the Fragment, all informations (State Bundle) will be set into 'getArguments()' and 'mExtraOutState' of your Fragment.
	 * 	- List with all detached Fragments, meaning before detach the fragment, FragmentTrasactionManager will call 'onSaveInstanceState(Bundle outState)' 
	 * to get the last instance of the Fragment. When it will attach the Fragment to the UI, all information (State Bundle) will be set into 'mExtraOutState' and NOT 'getArguments()' of your Fragment. 
	 * !!! WARNING 'getArguments()' WILL NOT BE SET WILL THE LAST INFO. !!!!
	 *  - Current fragment is the last Fragment added into the tag, it could be Attach or Detach to the UI
	 * 
	 * 
	 * @param tag - Name of the stack
	 * @param animation - Allows or not animation
	 */
	public void returnToRootFragmentInStackWithAnimation(String tag, Boolean animation) {
		mFMSuper.returnToRootFragmentInStackWithAnimation(tag, animation);
	}
	
	/**
	 * Get all the Fragments from Saved's list, Detach's list and if Current Fragment is Detach.
	 * 
	 * @param tag - Name of the stack
	 * @return list of Fragments
	 */
	public List<SavedFragment> getListOfFragmentsInStack(String tag){
		return mFMSuper.getListOfFragmentsInStack(tag);
	}
	
	/**
	 * replace all the fragments in Stack. If the current Fragment of the Stack is Detach it will be replace by the last item of the list
	 * 
	 * @param tag - Name of the stack
	 * @param fragments - list of Fragments
	 */
	public void setListOfFragmentsInStack(String tag, List<SavedFragment> fragments) {
		mFMSuper.setListOfFragmentsInStack(tag, fragments);
	}
	
	/**
	 * Get the name of the stack (Tag) which is currently show in the UI.  
	 * 
	 * @param resourceId - Id of the resource
	 * @return if no tag is currently show on the UI it will be null.
	 */
	public String getCurrentStackNameFromContent(int resourceId) {
		return mFMSuper.getCurrentStackNameFromContent(resourceId);
	}
}
