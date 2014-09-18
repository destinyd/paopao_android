package com.realityandapp.paopao_customer;

import android.app.Application;
import android.content.Context;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by dd on 14-9-18.
 */
public class PaopaoCustomerApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        DisplayImageOptions options;
        ImageLoaderConfiguration config;

        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        config = new ImageLoaderConfiguration.Builder(getApplicationContext())

                // 设置缓存图片的宽度跟高度
                .memoryCacheExtraOptions(480, 800)
                .diskCacheExtraOptions(480, 800, null)

                        // 通过 LruMemoryCache 实现缓存机制
//                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
//                .memoryCacheSize(2 * 1024 * 1024)

                        // 限制缓存文件数量百分比
                .memoryCacheSizePercentage(13)

                .diskCacheSize(50 * 1024 * 1024)

                        // 硬盘缓存文件数量
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .defaultDisplayImageOptions(options)


                .build();

        ImageLoader.getInstance().init(config);


        context = this.getApplicationContext();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public static Context get_context() {
        return context;
    }
}
