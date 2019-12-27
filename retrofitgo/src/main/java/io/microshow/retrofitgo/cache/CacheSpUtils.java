package io.microshow.retrofitgo.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import io.microshow.retrofitgo.RetrofitClient;

/**
 * sp 缓存数据
 * Created by Super on 2019/12/25.
 */
public class CacheSpUtils {

    public static final String PREFS_NAME = "RETROFITGO_CACHE_DATAS";

    public static SharedPreferences getSharedPreferences() {
        return RetrofitClient.getInstance().getContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static void saveCacheData(String cacheKey, String datas) {
        if (!TextUtils.isEmpty(cacheKey) && !TextUtils.isEmpty(datas)) {
            SharedPreferences mSharedPreferences = getSharedPreferences();
            SharedPreferences.Editor edit = mSharedPreferences.edit();
            edit.putString(cacheKey, datas);
            edit.commit();
        }
    }

    public static String getCacheData(String cacheKey) {
        return getSharedPreferences().getString(cacheKey, null);
    }

}
