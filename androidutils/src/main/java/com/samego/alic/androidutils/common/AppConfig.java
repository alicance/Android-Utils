package com.samego.alic.androidutils.common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.samego.alic.androidutils.R;

/**
 * 应用辅助配置文件
 * Created by alic on 16-5-17.
 */
public class AppConfig {
    public static DisplayImageOptions imageOptions() {
        /**
         * DisplayImageOptions所有配置简介
         */
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                // 设置图片加载时的默认图片
                .showImageOnLoading(R.drawable.login_face)
                        // 设置图片加载失败的默认图片
                .showImageOnFail(R.drawable.login_face)
                        // 设置图片URI为空时默认图片
                .showImageForEmptyUri(R.drawable.login_face)
                        // 设置是否将View在加载前复位
                .resetViewBeforeLoading(false)
                        // 设置延迟部分时间才开始加载
                        // 默认为0
                .delayBeforeLoading(100)
                        // 设置添加到内存缓存
                        // 默认为false
                .cacheInMemory(false)
                        //设置缓存 默认为falsegrading
                .cacheOnDisk(true)
                        // 设置规模类型的解码图像
                        // 默认为ImageScaleType.IN_SAMPLE_POWER_OF_2
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                        // 设置位图图像解码配置
                        // 默认为Bitmap.Config.ARGB_8888
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                        // 设置选项的图像解码
                .decodingOptions(new BitmapFactory.Options())
                        // 设置自定义显示器
                        // 默认为DefaultConfigurationFactory.createBitmapDisplayer()
                .displayer(new FadeInBitmapDisplayer(300))
                        // 设置自定义的handler
                        // 默认为new Handler()
                .handler(new Handler())
                        // 建立
                .build();
        return options;
    }
}
