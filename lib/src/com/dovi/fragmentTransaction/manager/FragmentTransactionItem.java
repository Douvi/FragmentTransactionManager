package com.dovi.fragmentTransaction.manager;

import com.dovi.fragmentTransaction.FTFragment;

public class FragmentTransactionItem {

		public String tag;
		public FTFragment fragment;
		
		public FragmentTransactionItem(String tag, FTFragment fragment) {
			super();
			this.tag = tag;
			this.fragment = fragment;
		}

}
