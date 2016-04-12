package com.appdsn.loopbanner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;


@SuppressLint("ClickableViewAccessibility") @SuppressWarnings("rawtypes")
public class LoopViewPager extends ViewPager {
	private OnPageChangeListener mOuterPageChangeListener;
	private LoopPageAdapter mAdapter;
    public boolean isCanScroll = true;//是否可以手指滑动页面，默认是可以滑动的,只有当数据为1时才不可以滑动
 
	public void setAdapter(LoopPageAdapter adapter) {
        super.setAdapter(adapter);
        mAdapter = adapter;
        setCurrentItem(adapter.getFristItem(), false);
        if( adapter.getRealCount()<=1){
    	   isCanScroll=false;
        }
       
    }

    public int getRealItem() {
        return mAdapter != null ? mAdapter.toRealPosition(super.getCurrentItem()) : 0;
    }
   
    public void setCanScroll(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isCanScroll) {
            return super.onTouchEvent(ev);
        } else
            return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isCanScroll)
            return super.onInterceptTouchEvent(ev);
        else
            return false;
    }

    public LoopViewPager(Context context) {
        this(context,null);
    }

    public LoopViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        addOnPageChangeListener(onPageChangeListener);
    }
    
    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        mOuterPageChangeListener = listener;
    }
    
    private OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {
        private float mPreviousPosition = -1;

        @Override
        public void onPageSelected(int position) {
        	if (mAdapter==null) {
				return;
			}
            int realPosition = mAdapter.toRealPosition(position);
            if (mPreviousPosition != realPosition) {
                mPreviousPosition = realPosition;
                if (mOuterPageChangeListener != null) {
                    mOuterPageChangeListener.onPageSelected(realPosition);
                }
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
        	if (mAdapter==null) {
				return;
			}
        	int realPosition = mAdapter.toRealPosition(position);

            if (mOuterPageChangeListener != null) {
                if (realPosition != mAdapter.getRealCount() - 1) {
                    mOuterPageChangeListener.onPageScrolled(realPosition,
                            positionOffset, positionOffsetPixels);
                } else {
                    if (positionOffset > .5) {
                        mOuterPageChangeListener.onPageScrolled(0, 0, 0);
                    } else {
                        mOuterPageChangeListener.onPageScrolled(realPosition,
                                0, 0);
                    }
                }
            }
           
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (mOuterPageChangeListener != null) {
                mOuterPageChangeListener.onPageScrollStateChanged(state);
            }
        }
    };


}
