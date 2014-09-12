package com.dovi.projectTest;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.dovi.fragmentTransaction.FTFragment;
import com.dovi.fragmentTransaction.FragmentTransactionAdapter.Animation;
import com.dovi.fragmentTransaction.FragmentTransactionBuilder;
import com.dovi.fragmentTransaction.OnSaveFragmentTransaction;
import com.dovi.fragmentTransaction.layout.FTRelativeLayout;
import com.dovi.projectTest.fragments.MainContentFragment;
import com.dovi.projectTest.fragments.MainMenuFragment;
import com.example.projecttest.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class MainActivity extends ActionBarActivity implements OnClickListener, OnSaveFragmentTransaction {

	private SlidingMenu mMenu;
	private FTRelativeLayout mRelativeLayout;
	public FragmentTransactionBuilder mFragmentTransactionBuilder;
	public TextView tab1;
	public TextView tab2;
	public TextView tab3;

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

		mFragmentTransactionBuilder = mRelativeLayout.getFragmentManger(getSupportFragmentManager(), this);
		
		if (mFragmentTransactionBuilder.isNeededToRestoreState()) {
			mFragmentTransactionBuilder.restoreState();
		} else {
			mFragmentTransactionBuilder.createTag("Menu", R.id.menuContent, 1);
			mFragmentTransactionBuilder.createTag("ContentTab1", R.id.fragmentContent, 1);
			
			mFragmentTransactionBuilder.addFragmentInStack("Menu", FTFragment.instantiate(this, MainMenuFragment.class.getName(), null, Animation.ANIM_NONE, Animation.ANIM_NONE));
			
			Bundle mBundle = new Bundle();
			mBundle.putString("title", "Tab1");
			mFragmentTransactionBuilder.addFragmentInStack("ContentTab1", FTFragment.instantiate(this, MainContentFragment.class.getName(), mBundle, Animation.ANIM_NONE, Animation.ANIM_NONE));
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		if (mMenu.isMenuShowing()) {

			if (mFragmentTransactionBuilder.isStackEmpty("Menu")) {
				mMenu.showContent();
			} else {
				mFragmentTransactionBuilder.removeTopFragmentInStackWithAnimation("Menu", true);
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
			
			if (mFragmentTransactionBuilder.getCountOfFragmentsInStack("ContentTab1") > 0) {
				mFragmentTransactionBuilder.showTopFragmentInStack("ContentTab1");
			} else {
				mFragmentTransactionBuilder.createTag("ContentTab1", R.id.fragmentContent, 1);
				mBundle.putString("title", "Tab1");
				mFragmentTransactionBuilder.addFragmentInStack("ContentTab1",
						FTFragment.instantiate(this, MainContentFragment.class.getName(), mBundle, Animation.ANIM_NONE, Animation.ANIM_NONE));

			}
			
			break;
		case R.id.tab2:
			
			if (mFragmentTransactionBuilder.getCountOfFragmentsInStack("ContentTab2") > 0) {
				mFragmentTransactionBuilder.showTopFragmentInStack("ContentTab2");
			} else {
				mFragmentTransactionBuilder.createTag("ContentTab2", R.id.fragmentContent, 1);
				mBundle.putString("title", "Tab2");
				mFragmentTransactionBuilder.addFragmentInStack("ContentTab2",
						FTFragment.instantiate(this, MainContentFragment.class.getName(), mBundle, Animation.ANIM_NONE, Animation.ANIM_NONE));

			}
			
			break;
		case R.id.tab3:
			
			if (mFragmentTransactionBuilder.getCountOfFragmentsInStack("ContentTab3") > 0) {
				mFragmentTransactionBuilder.showTopFragmentInStack("ContentTab3");
			} else {
				mFragmentTransactionBuilder.createTag("ContentTab3", R.id.fragmentContent, 1);
				mBundle.putString("title", "Tab3");
				mFragmentTransactionBuilder.addFragmentInStack("ContentTab3",
						FTFragment.instantiate(this, MainContentFragment.class.getName(), mBundle, Animation.ANIM_NONE, Animation.ANIM_NONE));

			}
			
			break;
		default:
			break;
		}
	}

	@Override
	public FragmentTransactionBuilder onSaveInstanceState() {
		// TODO Auto-generated method stub
		return mFragmentTransactionBuilder;
	}

}
