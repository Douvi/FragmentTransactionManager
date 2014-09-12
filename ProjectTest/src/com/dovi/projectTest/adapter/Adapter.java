package com.dovi.projectTest.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.projecttest.R;

public class Adapter extends ArrayAdapter<String> {

	protected Context mContext = null;
	protected int mLayout;
	protected List<String> mList = null;
	
	private TextView mTextView;
	private OnClickListener mListener;

	public Adapter(Context context, List<String> list, OnClickListener listener) {
		super(context, R.layout.adapter, list);
		mContext = context;
		mList = list;
		mLayout = R.layout.adapter;
		mListener = listener;
	}
	
	@Override
	public View getView(int position, View row,  ViewGroup parent) {
		
		if (row == null) {
			final LayoutInflater inflater = LayoutInflater.from(mContext);
			row = inflater.inflate(mLayout, parent, false);
		}
		
		String text = mList.get(position);
		mTextView = (TextView) row.findViewById(R.id.text);

		mTextView.setText(text);
		
		row.setTag(text);
		row.setOnClickListener(mListener);
		
		return(row);
	}
	
	@Override
	public int getCount(){
		return mList.size();
	}
	
}
