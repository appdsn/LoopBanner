package com.appdsn.loopbanner;

import java.lang.reflect.Field;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.PageTransformer;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * 页面翻转控件，极方便的广告栏 支持无限循环，自动翻页，翻页特效
 * 
 */
@SuppressWarnings("rawtypes")
public class LoopBanner extends RelativeLayout {

	private LoopPageAdapter pageAdapter;
	private LoopViewPager viewPager;
	private long autoTurningTime = 5000;// 自动翻页时间
	private boolean isTurning = false;// 是否正在翻页
	public enum PageIndicatorAlign {
		ALIGN_PARENT_LEFT, ALIGN_PARENT_RIGHT, CENTER_HORIZONTAL
	}

	private BannerPageIndicator pageIndicator;
	private ViewPagerScroller scroller;

	public LoopBanner(Context context) {
		this(context, null);
	}

	public LoopBanner(Context context, AttributeSet attrs) {
		super(context, attrs);
		viewPager = new LoopViewPager(context);
		pageIndicator = new BannerPageIndicator(context);
		this.addView(viewPager, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		this.addView(pageIndicator, new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		setPageIndicatorAlign(PageIndicatorAlign.CENTER_HORIZONTAL);
		initViewPagerScroller();
	}

	public void setPageAdapter(LoopPageAdapter pageAdapter) {
		this.pageAdapter = pageAdapter;
		viewPager.setAdapter(pageAdapter);
		pageIndicator.setViewPager(viewPager);
	}

	public LoopViewPager getViewPager() {
		return viewPager;
	}

	public BannerPageIndicator getIndicator() {

		return pageIndicator;
	}

	public void setCanLoop(boolean canLoop) {
		pageAdapter.setCanLoop(canLoop);
		pageAdapter.notifyDataSetChanged();
		viewPager.setCurrentItem(pageAdapter.getFristItem(), false);// 重置位置
	}

	/**
	 * 通知数据变化 如果只是增加数据建议使用 notifyDataSetAdd()
	 */
	public void notifyDataSetChanged() {
		pageIndicator.notifyDataSetChanged();
		pageAdapter.notifyDataSetChanged();
		viewPager.setCurrentItem(pageAdapter.getFristItem(), false);// 重置位置
		int realCount = pageAdapter.getRealCount();
		if (realCount <= 1) {
			viewPager.setCanScroll(false);
		} else {
			viewPager.setCanScroll(true);
		}

	}

	
	private Runnable adSwitchTask = new Runnable() {
		@Override
		public void run() {
			postDelayed(adSwitchTask, autoTurningTime);
			if (pageAdapter==null) {
				return;
			}
			int realCount = pageAdapter.getRealCount();
			boolean canLoop = pageAdapter.isCanLoop();
			int page = viewPager.getCurrentItem() + 1;
			if (realCount >1) {
				if (!canLoop&&page==realCount) {
					viewPager.setCurrentItem(0);
				} else {
					viewPager.setCurrentItem(page);
				}
			}
			

		}
	};

	/***
	 * 开始翻页
	 * 
	 * @param autoTurningTime
	 *            自动翻页时间
	 * @return
	 */
	public void startTurning(long autoTurningTime) {
		// 设置可以翻页
		this.autoTurningTime = autoTurningTime;
		// 如果是正在翻页的话先停掉
		if (isTurning) {
			stopTurning();
		}
		// 开启翻页
		isTurning = true;
		postDelayed(adSwitchTask, autoTurningTime);

	}

	public void stopTurning() {
		isTurning=false;
		removeCallbacks(adSwitchTask);
	}

	/**
	 * 设置底部指示器是否可见
	 * 
	 * @param visible
	 */
	public LoopBanner setIndicatorVisible(boolean visible) {
		pageIndicator.setVisibility(visible ? View.VISIBLE : View.GONE);
		return this;
	}

	/**
	 * 指示器的方向
	 * 
	 * @param align
	 *            三个方向：居左 （RelativeLayout.ALIGN_PARENT_LEFT），居中
	 *            （RelativeLayout.CENTER_HORIZONTAL），居右
	 *            （RelativeLayout.ALIGN_PARENT_RIGHT）
	 * @return
	 */
	public LoopBanner setPageIndicatorAlign(PageIndicatorAlign align) {
		LayoutParams layoutParams = (LayoutParams) pageIndicator
				.getLayoutParams();
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,
				RelativeLayout.TRUE);
		switch (align) {
		case ALIGN_PARENT_LEFT:
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT,
					RelativeLayout.TRUE);
			break;
		case CENTER_HORIZONTAL:
			layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL,
					RelativeLayout.TRUE);
			break;
		case ALIGN_PARENT_RIGHT:
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,
					RelativeLayout.TRUE);
			break;
		default:
			break;
		}
		pageIndicator.setLayoutParams(layoutParams);
		return this;
	}

	/**
	 * 设置ViewPager的滚动速度
	 * 
	 * @param scrollDuration
	 */
	public void setScrollDuration(int scrollDuration) {
		scroller.setScrollDuration(scrollDuration);
	}

	/**
	 * 设置ViewPager的滑动速度
	 * */
	private void initViewPagerScroller() {
		try {
			Field mScroller = ViewPager.class.getDeclaredField("mScroller");
			mScroller.setAccessible(true);
			scroller = new ViewPagerScroller(getContext());
			mScroller.set(viewPager, scroller);

		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 自定义翻页动画效果
	 * 
	 * @param transformer
	 * @return
	 */
	public void setPageTransformer(PageTransformer transformer) {
		viewPager.setPageTransformer(true, transformer);
	}

	// 触碰控件的时候，翻页应该停止，离开的时候如果之前是开启了翻页的话则重新启动翻页
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		if (action == MotionEvent.ACTION_UP
				|| action == MotionEvent.ACTION_CANCEL
				|| action == MotionEvent.ACTION_OUTSIDE) {
			// 开始翻页
			if (isTurning) {
				startTurning(autoTurningTime);
			}
		} else if (action == MotionEvent.ACTION_DOWN) {
			// 停止翻页
			if (isTurning) {
				stopTurning();
				isTurning=true;
			}
		}
		return super.dispatchTouchEvent(ev);
	}

	
	// 获取当前的页面index
	public int getCurrentItem() {
		if (viewPager != null) {
			return viewPager.getRealItem();
		}
		return -1;
	}

	// 设置当前的页面index
	public void setCurrentItem(int index) {
		if (viewPager != null) {
			viewPager.setCurrentItem(index, false);
		}
	}

	/**
	 * 设置翻页监听器
	 * 
	 * @param onPageChangeListener
	 * @return
	 */
	public void setOnPageChangeListener(
			ViewPager.OnPageChangeListener onPageChangeListener) {
		pageIndicator.setOnOuterPageChangeListener(onPageChangeListener);
	}

}
