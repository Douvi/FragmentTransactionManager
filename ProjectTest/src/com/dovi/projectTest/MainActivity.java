package com.dovi.projectTest;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.dovi.fragmentTransaction.FTFragment;
import com.dovi.fragmentTransaction.OnSaveFragmentTransaction;
import com.dovi.fragmentTransaction.layout.FTRelativeLayout;
import com.dovi.fragmentTransaction.manager.FragmentTransactionAdapter.Animation;
import com.dovi.fragmentTransaction.manager.FragmentTransactionManager;
import com.dovi.projectTest.fragments.MainMenuFragment;
import com.dovi.projectTest.fragments.TabFragment;
import com.example.projecttest.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class MainActivity extends ActionBarActivity implements OnSaveFragmentTransaction {

	public static final String CONTENT_MENU = "menu";
	public static final String CONTENT_TABS = "tabs";
	public static final String CONTENT_LIST = "list";
	public static final String CONTENT_TAB1 = "tab1";
	public static final String CONTENT_TAB2 = "tab2";
	public static final String CONTENT_TAB3 = "tab3";
	
	private SlidingMenu mMenu;
	private FTRelativeLayout mRelativeLayout;
	public FragmentTransactionManager mFragmentTransactionManager;
	

	public String currentStack;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);

		setContentView(R.layout.main_activity);

		mRelativeLayout = (FTRelativeLayout) findViewById(R.id.root);
	
		mMenu = new SlidingMenu(this);
		mMenu.setMode(SlidingMenu.LEFT);
		mMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		mMenu.setShadowWidth(10);
		mMenu.setShadowDrawable(R.drawable.shadow);
		mMenu.setBehindOffset(130);
		mMenu.setFadeDegree(0.35f);
		mMenu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
		mMenu.setMenu(R.layout.menu_activity);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		mFragmentTransactionManager = mRelativeLayout.getFragmentManger(getSupportFragmentManager(), this);
		
		if (!mFragmentTransactionManager.isContainTag(CONTENT_MENU)) {
			mFragmentTransactionManager.createTag(CONTENT_MENU, R.id.menuContent, 1);
			mFragmentTransactionManager.createTag(CONTENT_TABS, R.id.fragmentContent, 1);
			
			mFragmentTransactionManager.addFragmentInStack(CONTENT_MENU, FTFragment.instantiate(this, MainMenuFragment.class.getName(), null, Animation.ANIM_NONE, Animation.ANIM_NONE));
			
//			Bundle mBundle = new Bundle();
//			mBundle.putString("title", "Tab1");
//			mBundle.putString("stack", "ContentTab1");
			mFragmentTransactionManager.addFragmentInStack(CONTENT_TABS, FTFragment.instantiate(this, TabFragment.class.getName(), null, Animation.ANIM_NONE, Animation.ANIM_NONE));
		}
	}

	@Override
	public void onBackPressed() {
		currentStack = mFragmentTransactionManager.getCurrentStackNameFromContent(R.id.fragmentContent);
		
		if (mMenu.isMenuShowing()) {

			if (mFragmentTransactionManager.isStackEmpty(CONTENT_MENU)) {
				mMenu.showContent();
			} else {
				mFragmentTransactionManager.removeTopFragmentInStackWithAnimation(CONTENT_MENU, true);
			}
			
		} 
//		else if (currentStack.endsWith("ContentTab1") || currentStack.endsWith("ContentTab2") || currentStack.endsWith("ContentTab3")) {
//			
//			if (mFragmentTransactionManager.isStackEmpty(currentStack)) {
//				super.onBackPressed();
//			}else {
//				
//				if (currentStack.endsWith("ContentTab3")) {
//					mFragmentTransactionManager.returnToFragmentAtPositionInStackWithAnimation("ContentTab3", 1, true);
//				} else {
//					mFragmentTransactionManager.removeTopFragmentInStackWithAnimation(currentStack, true);
//					
//					if (currentStack.endsWith("ContentTab1")) {
//						List<SavedFragment> mList = mFragmentTransactionManager.getListOfFragmentsInStack("ContentTab3");		
//						mFragmentTransactionManager.setListOfFragmentsInStack("ContentTab2", mList, true);
//					}
//				}
//				
//				
//			}
//			
//		} 
		else {
			super.onBackPressed();
		}

	}
	
	@Override
	public FragmentTransactionManager onSaveInstanceState() {
		// TODO Auto-generated method stub
		return mFragmentTransactionManager;
	}

}
