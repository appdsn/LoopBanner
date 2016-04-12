package com.appdsn.loopbanner;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

public abstract class SimplePageAdapter extends LoopPageAdapter<String>{

	public SimplePageAdapter(Context context, List<String> mDatas) {
		super(context, mDatas, R.layout.layout_banner_item, true);
		// TODO Auto-generated constructor stub
	}
	public SimplePageAdapter(Context context, List<String> mDatas,boolean canLoop) {
		super(context, mDatas, R.layout.layout_banner_item, canLoop);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void convert(ViewHolder holder, final String itemData, int position) {
		// TODO Auto-generated method stub
		ImageView imageView = (ImageView) holder.getConvertView();
		ImageLoader.getInstance().displayImage(itemData, imageView);
		imageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onItemClick(itemData);
			}
		});
	}
	
	public abstract void onItemClick(String itemData);

}
