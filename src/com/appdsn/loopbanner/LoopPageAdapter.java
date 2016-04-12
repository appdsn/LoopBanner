package com.appdsn.loopbanner;

import java.util.List;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

public abstract class LoopPageAdapter<T> extends RecyclingPagerAdapter
{
	protected List<T> mDatas;
	private boolean canLoop = true;//是否可以循环，默认是可以的
	public boolean dataChange = false;
	private final int MULTIPLE_COUNT = 300;// 也就是说最多翻页300倍的次数就会重置位置（此时会出现闪动一次，可能看不见）
	private Context context;
	private int resId;
	private ViewPager viewPager;

	public LoopPageAdapter(Context context, List<T> mDatas, int resId) {
		this(context, mDatas, resId, true);
	}
	public LoopPageAdapter(Context context, List<T> mDatas, int resId,boolean canLoop) {
		super();
		this.context = context;
		this.mDatas = mDatas;
		this.canLoop = canLoop;
		this.resId = resId;
	}
	public int toRealPosition(int position) {
		int realCount = getRealCount();
		if (realCount == 0)
			return 0;
		int realPosition = position % realCount;
		return realPosition;
	}

	@Override
	public int getCount() {
		return canLoop ? getRealCount() * MULTIPLE_COUNT : getRealCount();
	}

	public int getRealCount() {
		return mDatas == null ? 0 : mDatas.size();
	}

	/*
	 * 第一个起始显示的默认位置，一般设在最大位置的中间 这里设置稍微靠前的位置
	 */
	public int getFristItem() {
		return canLoop ? getRealCount() : 0;
	}

	/*
	 * 第一个结尾显示的默认位置，一般设在最大位置的中间 这里设置稍微靠前的位置
	 */
	public int getLastItem() {
		return getRealCount() - 1;
	}

	public void setCanLoop(boolean canLoop) {
		this.canLoop = canLoop;
	}
	
	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
//		dataChange = true;
		super.notifyDataSetChanged();	
	}

	public boolean isCanLoop() {
		return canLoop;
	}
	
	@Override
	public void finishUpdate(ViewGroup container) {
		if (viewPager==null) {
			viewPager = (ViewPager) container;
		}
		int position = viewPager.getCurrentItem();
//		Log.i("123", "finishUpdate_position-" + position);
		if (position == 0 && !dataChange && canLoop) {
			position = getFristItem();// 重置位置
			viewPager.setCurrentItem(position, false);//
			// 这里重写设置显示位置，因为显示的是相同的界面，一般看不出来切换，可能会有点闪动
		} else if (position == getCount() - 1 && !dataChange && canLoop) {
			position = getLastItem();// 重置位置
			viewPager.setCurrentItem(position, false);//
			// 这里重写设置显示位置，因为显示的是相同的界面，一般看不出来切换，可能会有点闪动
		}
		dataChange = false;

	}

	public T getItem(int realPosition) {
		return mDatas.get(realPosition);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup container) {
		int realPosition = toRealPosition(position);
		final ViewHolder viewHolder = getViewHolder(realPosition, convertView,
				container);
		convert(viewHolder, getItem(realPosition), realPosition);
		return viewHolder.getConvertView();

	}

	public abstract void convert(ViewHolder holder, T itemData, int position);

	private ViewHolder getViewHolder(int position, View convertView,
			ViewGroup parent) {
		return ViewHolder.get(context, convertView, parent, resId, position);
	}

}
