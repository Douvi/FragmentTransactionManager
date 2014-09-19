package com.dovi.projectTest;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.dovi.fragmentTransaction.FTFragment;
import com.dovi.fragmentTransaction.OnSaveFragmentTransaction;
import com.dovi.fragmentTransaction.layout.FTRelativeLayout;
import com.dovi.fragmentTransaction.manager.FragmentTransactionAdapter.Animation;
import com.dovi.fragmentTransaction.manager.FragmentTransactionManager;
import com.dovi.projectTest.fragments.MainContentFragment;
import com.dovi.projectTest.fragments.MainMenuFragment;
import com.example.projecttest.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class MainActivity extends ActionBarActivity implements OnClickListener, OnSaveFragmentTransaction {

	private SlidingMenu mMenu;
	private FTRelativeLayout mRelativeLayout;
	public FragmentTransactionManager mFragmentTransactionManager;
	public TextView tab1;
	public TextView tab2;
	public TextView tab3;

	public String currentStack;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);

		setContentView(R.layout.main_activity);

		mRelativeLayout = (FTRelativeLayout) findViewById(R.id.root);
		tab1 = (TextView) findViewById(R.id.tab1);
		tab1.setOnClickListener(this);
		tab2 = (TextView) findViewById(R.id.tab2);
		tab2.setOnClickListener(this);
		tab3 = (TextView) findViewById(R.id.tab3);
		tab3.setOnClickListener(this);

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
		
		if (!mFragmentTransactionManager.isContainTag("Menu")) {
			mFragmentTransactionManager.createTag("Menu", R.id.menuContent, 1);
			mFragmentTransactionManager.createTag("ContentTab1", R.id.fragmentContent, 1);
			
			mFragmentTransactionManager.addFragmentInStack("Menu", FTFragment.instantiate(this, MainMenuFragment.class.getName(), null, Animation.ANIM_NONE, Animation.ANIM_NONE));
			
			Bundle mBundle = new Bundle();
			mBundle.putString("title", "Tab1");
			mBundle.putString("stack", "ContentTab1");
			mFragmentTransactionManager.addFragmentInStack("ContentTab1", FTFragment.instantiate(this, MainContentFragment.class.getName(), mBundle, Animation.ANIM_NONE, Animation.ANIM_NONE));
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		currentStack = mFragmentTransactionManager.getCurrentStackNameFromContent(R.id.fragmentContent);
		
		if (mMenu.isMenuShowing()) {

			if (mFragmentTransactionManager.isStackEmpty("Menu")) {
				mMenu.showContent();
			} else {
				mFragmentTransactionManager.removeTopFragmentInStackWithAnimation("Menu", true);
			}
			
		} else if (currentStack.endsWith("ContentTab1") || currentStack.endsWith("ContentTab2") || currentStack.endsWith("ContentTab3")) {
			
			if (mFragmentTransactionManager.isStackEmpty()) {
				super.onBackPressed();
			}else {
				mFragmentTransactionManager.removeTopFragmentInStackWithAnimation(true);
			}
			
		} else {
			super.onBackPressed();
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Bundle mBundle = new Bundle();
		switch (v.getId()) {
		case R.id.tab1:
			
			if (mFragmentTransactionManager.getCountOfFragmentsInStack("ContentTab1") > 0) {
				mFragmentTransactionManager.showTopFragmentInStack("ContentTab1");
			} else {
				mFragmentTransactionManager.createTag("ContentTab1", R.id.fragmentContent, 1);
				mBundle.putString("title", "Tab1");
				mBundle.putString("stack", "ContentTab1");
				mFragmentTransactionManager.addFragmentInStack("ContentTab1",
						FTFragment.instantiate(this, MainContentFragment.class.getName(), mBundle, Animation.ANIM_NONE, Animation.ANIM_NONE));

			}
			
			break;
		case R.id.tab2:
			
			if (mFragmentTransactionManager.getCountOfFragmentsInStack("ContentTab2") > 0) {
				mFragmentTransactionManager.showTopFragmentInStack("ContentTab2");
			} else {
				mFragmentTransactionManager.createTag("ContentTab2", R.id.fragmentContent, 1);
				mBundle.putString("title", "Tab2");
				mBundle.putString("stack", "ContentTab2");
				mFragmentTransactionManager.addFragmentInStack("ContentTab2",
						FTFragment.instantiate(this, MainContentFragment.class.getName(), mBundle, Animation.ANIM_NONE, Animation.ANIM_NONE));

			}
			
			break;
		case R.id.tab3:
			
			if (mFragmentTransactionManager.getCountOfFragmentsInStack("ContentTab3") > 0) {
				mFragmentTransactionManager.showTopFragmentInStack("ContentTab3");
			} else {
				mFragmentTransactionManager.createTag("ContentTab3", R.id.fragmentContent, 1);
				mBundle.putString("title", "Tab3");
				mBundle.putString("stack", "ContentTab3");
				mFragmentTransactionManager.addFragmentInStack("ContentTab3",
						FTFragment.instantiate(this, MainContentFragment.class.getName(), mBundle, Animation.ANIM_NONE, Animation.ANIM_NONE));

			}
			
			break;
		default:
			break;
		}
	}

	@Override
	public FragmentTransactionManager onSaveInstanceState() {
		// TODO Auto-generated method stub
		return mFragmentTransactionManager;
	}

}
