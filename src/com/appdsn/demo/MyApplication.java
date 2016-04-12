package com.appdsn.demo;

import java.io.File;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class MyApplication extends Application {

	public void onCreate() {
		super.onCreate();
		initImageLoader(getApplicationContext());
	}

	public void initImageLoader(Context context) {
		// 自定义缓存文件的目录
		File cacheDir = null;
		// 如果DisplayImageOption没有传递给ImageLoader.displayImage(…)方法，那么从配置默认显示选项
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory(true) // default 设置下载的图片是否缓存在内存中
				.cacheOnDisk(true) // default 设置下载的图片是否缓存在SD卡中
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT) // default
				// 设置图片以如何的编码方式显示
				.bitmapConfig(Bitmap.Config.RGB_565) // default 设置图片的解码类型
				.build();

		// 以下是所有设置项，只需设置自己需要的，不设置会使用默认值
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context)
				.memoryCacheSizePercentage(10) // 20%最大内存
//				.diskCache(new UnlimitedDiskCache(cacheDir)) // default// 可以自定义缓存路径					
				.diskCacheSize(50 * 1024 * 1024) // 50 Mb sd卡(本地)缓存的最大值
				.defaultDisplayImageOptions(options)// default
				// .writeDebugLogs() // 打印debug log
				.build(); // 开始构建

		// 全局初始化此配置,通常使用默认config就可以了创建默认的ImageLoader配置参数
		// ImageLoaderConfiguration config = ImageLoaderConfiguration
		// .createDefault(this);
		// 必须初始化这个配置. 否则会出现错误
		ImageLoader.getInstance().init(config);
	}

}
